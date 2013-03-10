package donQuizote_v2;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.awt.image.WritableRaster;
import java.util.HashMap;
//import sun.awt.image.ToolkitImage;

import com.mortennobel.imagescaling.ResampleOp;

public class QuizController {

	// The things they carried
	private Writable outputWriter;
	private Robot robot;
	private final int NumberOfAreas = 1;
	public HashMap<String, Rectangle> areas = new HashMap<String, Rectangle>();
	// Viewing areas, provided as an offset from the game's frame, and a % width/height
	private Rectangle totalArea;
	private Rectangle adminArea;
	private DQWindow dqwindow;
	private DonQuizote dq;
		
	
	// Instantiate this	
	public QuizController(DonQuizote dq){
		try{ robot = new Robot(); } catch (Exception e){};
		this.dq = dq;
		this.dqwindow = dq.dqwindow;
		
	}
	
	// Retrieve the rectangle for an area
	public Rectangle getArea(String s){	
		return areas.get(s);
		}
	
	// After the main area has been selected, define the interesting regions
	private void setupAreas(){
		System.out.println("#QC Set-up subsequent areas");
		areas.put("answerA", translatedRectangle(89,543,303,33,874,648));
		areas.put("answerB", translatedRectangle(518,543,303,33,874,648));
		areas.put("answerC", translatedRectangle(89,601,303,33,874,648));
		areas.put("answerD", translatedRectangle(413,482,237,26,699,517));
		areas.put("question", translatedRectangle(50,372,592,43,700,517));
		areas.put("skipButton", translatedRectangle(634,455,42,31,703,520));
		areas.put("playButton", translatedRectangle(598,287,66,34,700,519));
		areas.put("fffTimer", translatedRectangle(122,100,32,21,701,521));
		areas.put("checkStartScreenGreen", translatedRectangle(463,325,48,35,799,519));
		areas.put("twoWayTrafficLogo", translatedRectangle(20,450,70,15,700,519));
		// What is this?
		areas.put("splitPurple", translatedRectangle(680,123,20,28,702,519));
		// TODO: Choose the best of these please
		areas.put("playForRealButton", translatedRectangle(136,257,145,101,700,519));
		areas.put("playForFreeButton", translatedRectangle(438,268,105,77,700,519));

		// TODO: How useful is this?
		areas.put("splitBlackContinueCollect", translatedRectangle(42,399,25,21,705,519));

	}
	
	// Click a named area from the areas map
	public void click(String s){
		clickAreaCentre(getArea(s)); 
		}
	
	// Get an area, and use it to set the play area (totalArea). 
	// Current usage is to just capture the whole game area and segregate off manually later
	public void setAreas(){
	
		GetAreaWorker worker = new GetAreaWorker(NumberOfAreas){
		@Override
		 protected void done2(){
			try {
				// Set the total area
				totalArea = areaOutput[0];
				System.out.println("#QC Set totalarea");
				 // Set up the rest of the areas
				setupAreas();
		    	}
			catch(Exception e){ e.printStackTrace();   }
		      }
		    };
		  try { worker.execute(); }
		  catch (Exception e){	e.printStackTrace(); }	
	}

	public void setAdminAreas(){
		GetAreaWorker worker = new GetAreaWorker(1){
		@Override
		 protected void done2(){
			try {
				adminArea = areaOutput[0];
		    	}catch(Exception e){ e.printStackTrace();   }
		      }
		    };
		  try { worker.execute(); }
		  catch (Exception e){	e.printStackTrace(); }		
	}
	
	// Specifically return an array of images with the questions and the answers
	public BufferedImage[] getQAImages() {
		BufferedImage[] qAImages = new BufferedImage[5];
		
		qAImages[1] = getInvertedImage(getArea("answerA"));
		qAImages[2] = getInvertedImage(getArea("answerB"));
		qAImages[3] = getInvertedImage(getArea("answerC"));
		qAImages[4] = getInvertedImage(getArea("answerD"));
		qAImages[0] = getInvertedImage(getArea("question"));
		
		return qAImages;
	}
	
	
	
	// Grab the area image from the Robot
	public BufferedImage getImage(Rectangle captureArea){
		try { 
			BufferedImage image = robot.createScreenCapture(captureArea);
			return image;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public BufferedImage getInvertedImage(Rectangle captureArea){
		
		return getImage(captureArea);
		
		//return invertImage(getImage(captureArea));
	}
	
	// Invert an image
	private static BufferedImage invertImage(final BufferedImage src) {
		final int w = src.getWidth();
		final int h = src.getHeight();
		final BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		final BufferedImageOp invertOp = new LookupOp(new ShortLookupTable(0, invertTable()), null);
		return invertOp.filter(src, dst);
	}
	
	// Match the rectangles to fit within the 'total area' rectangle
	private Rectangle translatedRectangle(int x, int y, int w, int h, int masterX, int masterY){
		//System.out.println("translated rectangle, totalArea size " + totalArea.height + " x " + totalArea.height);
		float translatedX = (float) x/masterX; 
		float translatedY = (float) y/masterY;
		float translatedW = (float) w/masterX; //System.out.println("#QC TransW " + translatedW);
		float translatedH = (float) h/masterY;
		
		int newX = (int)(totalArea.x+translatedX*totalArea.width);
		int newY = (int) (totalArea.y+translatedY*totalArea.height);
		int newWidth = (int) (translatedW*totalArea.width);
		int newHeight = (int) (translatedH*totalArea.height);
		//System.out.println("#QC X " + newX + " Y " + newY + " W " + newWidth + " H" + newHeight);
		return new Rectangle(newX, newY, newWidth, newHeight);	
	}
	

	
	public void clickAreaCentre(Rectangle r){
		int x = (int) (r.x + 0.5*r.width);
		int y = (int) (r.y + 0.5*r.height);
		robot.mouseMove(x, y);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		
		
	}
	
	/*
	 * ALL THE COLOUR GRABBING STUFF
	 */
	
	// Use this to copy a buffered image (properly, like)
	static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	

	
	public int getModalColour(BufferedImage image){
		
		BufferedImage copy = deepCopy(image);
		
		HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();
		// Loop over every pixel in the image and add to total RBG.
		for (int i = 0; i < copy.getWidth() ; i++){
			for (int j = 0; j < copy.getHeight() ; j++){
				
				int rgb = copy.getRGB(i,j);
				Integer counter = m.get(rgb);
				if (counter == null) counter = 0;
				counter++;
				m.put(new Integer(rgb), counter);
			}
		}
		// Get most common colour
		Integer colour = 0; Integer max = 0;
		for (Integer c : m.keySet()) if (max < m.get(c)) { colour = c; max=m.get(c);}
		/*
		
		ToolkitImage small = copy.getS
		int colour = small.getRGB(1, 1);
		*/
		return colour;
	}
	
	public int getMeanColour(BufferedImage image){
		
		// We get an average colour by shrinking the image to 1x1 and letting the GPU doing the averaging
		ResampleOp  resampleOp = new ResampleOp (3,3);
		BufferedImage copy= resampleOp.filter(image, null);
		
		int colour = copy.getRGB(1, 1);
				
		return colour;
	}
	
	public String getRectangeDims(){
		return "" + ((int) adminArea.getX()- totalArea.getX()) +","+  ((int) adminArea.getY()- totalArea.getY()) +","+  ((int) adminArea.getWidth()) +","+  ((int) adminArea.getHeight()) +","+  ((int) totalArea.getWidth()) +","+  ((int) totalArea.getHeight());
	}
	
	public BufferedImage getAdminTest(){
		return getImage(translatedRectangle((int)(adminArea.getX()- totalArea.getX()),(int)(adminArea.getY()- totalArea.getY()),(int)adminArea.getWidth(), (int)adminArea.getHeight(),(int)totalArea.getWidth(),(int)totalArea.getHeight()));
				
	}
	
	public BufferedImage getAdminImage(){
		return getImage(adminArea);
	}
	
	

	//TODO: Rename this, dawg.
	public int startPageColour(){
		return getModalColour(getImage(adminArea));
	}
	
	public int meanPageColour(){
		return getMeanColour(getImage(adminArea));
	}
	
	//TODO: Rename this, dawg.
	public void getColour(){
		//displayImage(controller.getAdminImage()); 
		//displayImage(controller.getAdminTest()); 
		dqwindow.updateText("DQ: area is " + getRectangeDims());
		dqwindow.updateText("DQ: modal colour is " + startPageColour());
		dqwindow.updateText("DQ: mean colour is " + meanPageColour());
		dqwindow.updateText("OCR: " + dq.ocrBufferedImage(getAdminImage()));
	}
	

	// Invertion table
	private static final short[] invertTable()
	{ short[] invertTable = new short[256];
	for (int i = 0; i < 256; i++) {
	invertTable[i] = (short) (255 - i); } 
	return invertTable;
	}
	
}
