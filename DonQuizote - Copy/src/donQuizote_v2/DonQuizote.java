package donQuizote_v2;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.xeustechnologies.googleapi.spelling.SpellChecker;
import org.xeustechnologies.googleapi.spelling.SpellCorrection;
import org.xeustechnologies.googleapi.spelling.SpellResponse;

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
			ocr = new TesjeractOCR();
//			ocr = new FakeOCR();   // Fake OCR machine so I can do this on a mac.
		
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
			
//			LoadAndShow test = new LoadAndShow(b);
//	        JFrame f = new JFrame();
//	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	        f.add(new JScrollPane(test));
//	        f.setSize(400,400);
//	        f.setLocation(200,200);
//	        f.setVisible(true);
	        
	    	//System.out.println("Attempting to recognise #DQ");
			String recognised = ocr.recognise(b);
			System.out.println("Recognised: " + recognised + " #DQ");
			questionAndAnswers[i++] = recognised;
			}
		
			
			
			questionAndAnswers = SpellCorrector.correct(questionAndAnswers);
			BingLookup b = new BingLookup();
			System.out.println(questionAndAnswers[1]+" is answer 1 #DQ");
			String decision = b.getAnswer(questionAndAnswers[0], questionAndAnswers[1], questionAndAnswers[2], questionAndAnswers[3], questionAndAnswers[4]);
			System.out.println(decision);
			//answerEngine.getAnswer(questionAndAnswers);
			dqinterface.updateText(decision);
			
		
		    
		    
	}
	
	
	
		
}
	


