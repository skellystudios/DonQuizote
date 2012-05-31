package donQuizote_v1;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class DonQuizote {
	
	// Desiderata
	private DQInterface dqinterface;
	private QuizController controller;
	private OCREngine ocr;
	private AreaFinder afinder;

	public static void main(String[] args) {
		 new DonQuizote();
	}
	
	// Main thread
	public DonQuizote(){
			
			// Load the OCR Engine
//			ocr = new TesjeractOCR();
			ocr = new FakeOCR();   // Fake OCR machine so I can do this on a mac.
		
			// Prepare the interface
			dqinterface = new DQInterface(this);
			
			// Load the controller 
			controller = new QuizController(dqinterface); 	
			
			// Show the window
			dqinterface.makeWindow();
	}
	
	
	
	// Define the recognition areas
	public void setAreas(){
		controller.setAreas();
		
		}
	
	public void answerQuestion(){
		BufferedImage[] questionAndAnswersImages = controller.getQAImages();
		String[] questionAndAnswers = new String[questionAndAnswersImages.length];
		int i=0;
		for (BufferedImage b : questionAndAnswersImages)
			{
			questionAndAnswers[i++] = ocr.recognise(b);
			}
		//answerEngine.getAnswer(questionAndAnswers);
	}
	
	
	
		
}
	


