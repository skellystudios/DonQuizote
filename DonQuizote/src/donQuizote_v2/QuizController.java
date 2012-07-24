package donQuizote_v2;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;


public class QuizController {

	// The things they carried
	private Writable outputWriter;
	private final short[] invertTable; 
		{ invertTable = new short[256];
		for (int i = 0; i < 256; i++) {
		invertTable[i] = (short) (255 - i); } }
	private Robot robot;
	private final int NumberOfAreas = 5;
	private Rectangle[] areas = new Rectangle[NumberOfAreas];
	
		
		
	public QuizController(Writable w){
		try{ robot = new Robot(); } catch (Exception e){};
		this.outputWriter = w;
		
	}
	
	public void setAreas(){
		
//		AreaFinder finder = new AreaFinder();
//		finder.run();
//		System.out.println("Premature!");
//		questionArea = finder.getArea(); 
		
		outputWriter.updateText("Selecting OCR areas");

		GetAreaWorker worker = new GetAreaWorker(NumberOfAreas){
			
			@Override
			 protected void done2(){
			        try {
			          // Get the number of matches. Note that the 
			          // method get will throw any exception thrown 
			          // during the execution of the worker.
			        	  //System.out.println("Hey1");
			        	//Rectangle[] foundAreas = get();
			       // System.out.println("Captured " + areaOutput.length + " areas out of " + NumberOfAreas + " #QuizController");
			        	
			         areas = areaOutput;
			         
			        }catch(Exception e){
			        	e.printStackTrace();
			          
			        }
			      }
			    };
			    try {
					worker.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
		
	}


	public BufferedImage[] getQAImages() {
		
		BufferedImage[] qAImages = new BufferedImage[areas.length];
		int i = 0;
		for (Rectangle area : areas){
			//System.out.println(area.height);
			qAImages[i] = grabArea(area);
			i++;
		}
			
		return qAImages;
	}
	
	private BufferedImage grabArea(Rectangle captureArea){
			
			try { 
			//System.out.println("D");
			BufferedImage image = robot.createScreenCapture(captureArea);
			BufferedImage flippedImage = invertImage(image);
			return flippedImage;
			} catch(Exception e) {
				//System.out.println("E");
				e.printStackTrace();
				return null;}
	}

	private BufferedImage invertImage(final BufferedImage src) {
		final int w = src.getWidth();
		final int h = src.getHeight();
		final BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		final BufferedImageOp invertOp = new LookupOp(new ShortLookupTable(0, invertTable), null);
		return invertOp.filter(src, dst);
	}
	
	
	
}
