package donQuizote_v2;


import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import donQuizote_v2.FiniteStateMachine.State;


public class DonQuizote {

	// Desiderata
	public QuizController controller;
	private OCREngine ocr;
	private DatabaseDriver db;
	private BufferedImage[] qAImages;
	private Lookup lookup;
	public DQWindow dqwindow;
	public int qID;
	public FiniteStateMachine fsm;
	private Boolean testingMode = false;
	private String[] testQs = { "Who which director directed the film Psycho?",
			"Alfred Hitchcock", "The Cohen Brothers", "Steven Spielburg",
			"Quentin Tarrentino" };
	private static DonQuizote dq;
	private Overlay overlay;
	public AutomaticWorker automator;
	public Question q;
	public AnswerEngine ae;
	
	public static void main(String[] args) {
		dq = new DonQuizote();
	}
	
	public static DonQuizote getInstance(){
		return dq;
	}

	// Main setup
	public DonQuizote() {

		// Load the OCR Engine
		if (testingMode) { ocr = new FakeOCR();} // Fake OCR machine so I can do this on a mac.  
		else {ocr = new TesjeractOCR(); }
		
		// Prepare the interface
		dqwindow = new DQWindow(this);
		// Load the controller
		controller = new QuizController(this);
		// Connect to the Database
		db = new DatabaseDriver();
		
		// Add a lookup engine
		ae = new AnswerEngine();
		//lookup = new BingLookup(); // Fuck bing
		//lookup = new EntireWebLookup();
		lookup = new BingAzureLookup();
		
		
		// Keep track of the state
		fsm = new FiniteStateMachine(this, controller);
		
		
		automator = new AutomaticWorker(this);
	}

	public void startProcessing(){
		
		try {automator.execute();}
		catch (Exception e){	e.printStackTrace(); }
		
	}
	
	public void toggleOverlay() {
		if (overlay == null){
			overlay = new Overlay();
			overlay.addRectangles(controller.areas);} 	
		overlay.setVisible(!overlay.isVisible());
	}
	
	
	// This standalone method will select the question fo	
	public int answerQuestion() {

		System.out.println("#DQ: QuestionTime");
		
		// Clear the previous question's data
		cleanUpOldQuestion();

		// Get the question test from OCR or test source
		String[] questionAndAnswers = getQAString();
		System.out.println("#DQ: Got QAs");
		
		
		// Get rid of quote marks and change all | and / into L, because it probably should be.
		String [] questionAndAnswersCorrected = SpellCorrector.correctChars(questionAndAnswers);
				
		// Correct Spelling errors
		 questionAndAnswersCorrected = SpellCorrector.correct(questionAndAnswersCorrected);
		
		
		
		// See if we have a question for the current strings. If not, then make one. Then return the question
		// Get out a question from the DB
		q = db.getQuestion(questionAndAnswersCorrected);
		
		// Lookup the answer using single engine only
		int winner = ae.answerQuestion(q);		
		return winner;
	}

	private String[] getQAString() {

		String[] questionAndAnswers;

		if (!testingMode) {
			// Get the selected images from the controller
			qAImages = controller.getQAImages();

			questionAndAnswers = new String[qAImages.length];
			int i = 0;
			//DQWindow.displayImage(qAImages[0]);
			ocr.recognise(qAImages[0]);
		
			for (BufferedImage b : qAImages) {
				// Perform OCR
				String recognised = ocrBufferedImage(b);

				// Add to Array
				questionAndAnswers[i++] = recognised;
			}
			
			
			
			
		} else {
			// Use a test question
			questionAndAnswers = testQs;
		}

		return questionAndAnswers;
	}
	
	public String ocrBufferedImage(BufferedImage b){
		
		String output = null; 
		
		try {
			output = ocr.recognise(b);
		} catch (Exception ex) {
			System.out.println("Too fast");
			try {Thread.currentThread().sleep(100);} catch (Exception e) {}

			output = ocr.recognise(b);
		}
		return output;
	}

	private void cleanUpOldQuestion() {
		qID = 0;
		dqwindow.setQID("");
	}

	public void updateText(String s) {
		dqwindow.updateText(s);
	}
	
	public void updateState(String s) {
		dqwindow.updateState(s);
	}

	public void updateQuestion(){
		db.update(q);
	}

}
