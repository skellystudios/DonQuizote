package donQuizote_v2;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.awt.image.WritableRaster;
import java.util.HashMap;
//import sun.awt.image.ToolkitImage;

public class QuizController {

	// The things they carried
	private Writable outputWriter;
	private Robot robot;
	private final int NumberOfAreas = 1;
	private Rectangle[] areas = new Rectangle[NumberOfAreas];

	// Viewing areas, provided as an offset from the game's frame, and a % width/height
	private Rectangle totalArea;
	
	private Rectangle answerAArea() 	{return translatedRectangle(89,543,303,34,874,648);}
	private Rectangle answerBArea() 	{return translatedRectangle(518,543,303,34,874,648);}
	private Rectangle answerCArea() 	{return translatedRectangle(89,601,303,34,874,648);}
	private Rectangle answerDArea() 	{return translatedRectangle(518,601,303,34,874,648);}
	private Rectangle questionArea()	{return translatedRectangle(62,462,585,60,874,648);}
		
	private Rectangle skipButton() 		{return translatedRectangle(634,455,42,31,703,520);}
	private Rectangle playButton() 		{return translatedRectangle(598,287,66,34,700,519);}
	private Rectangle fffTimer() 		{return translatedRectangle(122,100,32,21,701,521);}
	private Rectangle splitPurple() 	{return translatedRectangle(680,123,20,28,702,519);}
	private Rectangle playForRealButton() {return translatedRectangle(136,257,145,101,700,519);}
	private Rectangle playForFreeButton() {return translatedRectangle(438,268,105,77,700,519);}
	private Rectangle checkStartScreenGreen() {return translatedRectangle(443,325,48,35,799,519);}
	
	private Rectangle splitBlackContinueCollect() {return translatedRectangle(42,399,25,21,705,519);}
	
	
	/*(
	private Rectangle answerAArea() {return translatedRectangle((float)80/744,(float)457/546,(float)250/744,(float) 27/546);}
	private Rectangle answerBArea() {return translatedRectangle((float)436/744,(float)457/546,(float)250/744,(float) 27/546);}
	private Rectangle answerCArea() {return translatedRectangle((float)80/744,(float)507/546,(float)250/744,(float) 27/546);}
	private Rectangle answerDArea() {return translatedRectangle((float)436/744,(float)507/546,(float)250/744,(float) 27/546);}
	private Rectangle questionArea() {return translatedRectangle((float) 100/965, r(float)501/708, (float) 787/965, (float)69/708);}
	*/
	
	
	// Invertion table
	private final short[] invertTable; 
	{ invertTable = new short[256];
	for (int i = 0; i < 256; i++) {
	invertTable[i] = (short) (255 - i); } }
	
	// Instantiate this	
	public QuizController(Writable w){
		try{ robot = new Robot(); } catch (Exception e){};
		this.outputWriter = w;
	}
	
	// Get one or more areas, and use them to set the Area[] array. Maybe need to parameterise this for # of areas
	// Current usage is to just capture the whole game area and segregate off manually later
	public void setAreas(){
		outputWriter.updateText("Selecting OCR areas");
		GetAreaWorker worker = new GetAreaWorker(NumberOfAreas){
		@Override
		 protected void done2(){
			try {
				areas = areaOutput;
				totalArea = areas[0];
		    	}catch(Exception e){ e.printStackTrace();   }
		      }
		    };
		  try { worker.execute(); }
		  catch (Exception e){	e.printStackTrace(); }		
	}

	// Specifically return an array of images with the questions and the answers
	public BufferedImage[] getQAImages() {
		BufferedImage[] qAImages = new BufferedImage[5];
		qAImages[0] = getInvertedImage(questionArea());
		qAImages[1] = getInvertedImage(answerAArea());
		qAImages[2] = getInvertedImage(answerBArea());
		qAImages[3] = getInvertedImage(answerCArea());
		qAImages[4] = getInvertedImage(answerDArea());
		
		return qAImages;
	}
	
	private BufferedImage getInvertedImage(Rectangle captureArea){
		return invertImage(getImage(captureArea));
	}
	
	// Grab the area image from the Robot AND INVERT IT
	private BufferedImage getImage(Rectangle captureArea){
		try { 
			BufferedImage image = robot.createScreenCapture(captureArea);
			return image;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Invert an image
	private BufferedImage invertImage(final BufferedImage src) {
		final int w = src.getWidth();
		final int h = src.getHeight();
		final BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		final BufferedImageOp invertOp = new LookupOp(new ShortLookupTable(0, invertTable), null);
		return invertOp.filter(src, dst);
	}
	
	// Match the rectangles to fit within the 'total area' rectangle
	private Rectangle translatedRectangle(int x, int y, int w, int h, int masterX, int masterY){
		//System.out.println("translated rectangle, totalArea size " + totalArea.height + " x " + totalArea.height);
		float translatedX = (float) x/masterX; 
		float translatedY = (float) y/masterY;
		float translatedW = (float) w/masterX; System.out.println("#QC TransW " + translatedW);
		float translatedH = (float) h/masterY;
		
		int newX = (int)(totalArea.x+translatedX*totalArea.width);
		int newY = (int) (totalArea.y+translatedY*totalArea.height);
		int newWidth = (int) (translatedW*totalArea.width);
		int newHeight = (int) (translatedH*totalArea.height);
		System.out.println("#QC X " + newX + " Y " + newY + " W " + newWidth + " H" + newHeight);
		return new Rectangle(newX, newY, newWidth, newHeight);	
	}
	
	/*
	  The following are sets of checks used by the FSM to check where we are
	*/
	public Boolean isStartPage(){
		return (getModalColour(getImage(checkStartScreenGreen())) == -16747628);
	}
	
	public int startPageColour(){
		return getModalColour(getImage(checkStartScreenGreen()));
	}
	
	// 
	public int getModalColour(BufferedImage image){
	
		// We get an average colour by shrinking the image to 1x1 and letting the GPU doing the averaging
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
	
	
	// Use this to copy a buffered image (properly, like)
	static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	// Press this to use play money only
	public void pressDemoButton() {
		clickAreaCentre(playForFreeButton());
	}
	
	public void clickAreaCentre(Rectangle r){
		int x = (int) (r.x + 0.5*r.width);
		int y = (int) (r.y + 0.5*r.height);
		robot.mouseMove(x, y);
		robot.mousePress(1);
		robot.mouseRelease(1);
		
		
	}
	
}
