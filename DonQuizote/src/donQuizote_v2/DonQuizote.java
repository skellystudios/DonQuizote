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
	private DBDriver db;
	private BufferedImage[] qAImages;
	private Lookup lookup;
	public DQWindow dqwindow;
	public int qID;
	private FiniteStateMachine fsm;
	private Boolean testingMode = false;
	private String[] testQs = { "Who which director directed the film Psycho?",
			"Alfred Hitchcock", "The Cohen Brothers", "Steven Spielburg",
			"Quentin Tarrentino" };
	private static DonQuizote dq;
	private Overlay overlay;

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
		db = new DBDriver();
		// Add a lookup engine
		//lookup = new BingLookup(); // Fuck bing
		//lookup = new EntireWebLookup();
		lookup = new BingAzureLookup();
		// Keep track of the state
		fsm = new FiniteStateMachine(this, controller);
	}

	public void startProcessing(){
		
		fsm.state = State.CHOICEPAGE;
		
		while(!(fsm.killSwitch == true)){	
			doAction();
		}
	}

	public void doAction(){
		/*
		 *  The general idea is that at each point we should check 
		 *  we are where we think, and then perform discrete actions
		 *  until we have made a state transition, and mark what we think that transition is
		 *  
		 *  If the check fails, we should either go into a panic mode and refresh the 
		 *  browser and start again, or we should attempt to guess the most likely state
		 */
		
		
		fsm.checkCaps();
		
		State state = fsm.state;
		System.out.println("#DQ: Arrived at " + state);
		updateState('S' + state.toString());
		
		switch(state){

			case CHOICEPAGE:
				controller.click("playForFreeButton");
				snooze(2000);
				fsm.doTransaction(40);
				break;
			// This is the splash page where we pick PLAY or REAL money 
			case PLAYPAGE:
				controller.click("playButton");	snooze(2000);
				controller.click("skipButton");	snooze(2000);
				controller.click("playForFreeButton"); snooze(2000);
				fsm.doTransaction(4000);
				break;
			case FFF:
				snooze(200);
				controller.click("playButton");
				controller.click("answerA"); snooze(700);
				controller.click("answerB"); snooze(700);
				controller.click("answerC"); snooze(700);
				controller.click("answerD"); 
				snooze(12000);
				fsm.doTransaction(200);
				break;
			case ISCOLLECT:
				snooze(100);
				fsm.doTransaction(200);
				break;
			case SCOREBOARD:
				snooze(1000);
				fsm.doTransaction(400);
				break;
			case QUESTION:
				//OH THIS IS THE BIG ONE
				// Get the mouse out of the way
				controller.click("playButton");
				snooze(4000);
				String winner = dq.answerQuestion();
				if (winner == "A") controller.click("answerA");
				if (winner == "B") controller.click("answerB");
				if (winner == "C") controller.click("answerC");
				if (winner == "D") controller.click("answerD");
				fsm.doTransaction(40);
				break;
			default: 
				snooze(100); // Chill out a little
		}
		
	}
	
	public void snooze(int i){
		fsm.snooze(i);
	}
	
	public void toggleOverlay() {
		if (overlay == null){
			overlay = new Overlay();
			overlay.addRectangles(controller.areas);} 	
		overlay.setVisible(!overlay.isVisible());
	}
	

	
	// This standalone method will select the question fo	
	public String answerQuestion() {

		System.out.println("#DQ: QuestionTime");
		
		// Clear the previous question's data
		cleanUpOldQuestion();

		// Get the question test from OCR or test source
		String[] questionAndAnswers = getQAString();
		System.out.println("#DQ: Got QAs");
		
		// Correct Spelling errors
		String [] questionAndAnswersCorrected = SpellCorrector.correct(questionAndAnswers);
		
		// Also get rid of quote marks and change all | and / into L, because it probably should be.
		questionAndAnswersCorrected = SpellCorrector.correctChars(questionAndAnswersCorrected);
		
		// TODO: FOR THE LOVE OF GOD STRIP OUT QUOTES OR MY DB DRIVER IS GONNA CRY. Probably should do it here for future matching purposes. Porpoises.
		
		/*
		 
		// Add to the DB for Audit
		db.addQuestion(questionAndAnswersCorrected[0], questionAndAnswersCorrected[1],
				questionAndAnswersCorrected[2], questionAndAnswersCorrected[3],
				questionAndAnswersCorrected[4]);

		// Get the qID and update the interface				
		//qID = db.lookupID(questionAndAnswersCorrected[0]);
		// dqwindow.setQID(qID + "");
		
		// Get out a question from the DB
		Question q = db.lookupQuestion(questionAndAnswersCorrected[0]);
		dqwindow.setQID(qID + "");
		
		*/
		
		String[] qAndAWithoutQuestions = SpellCorrector.stripQuestionWords(questionAndAnswersCorrected);
		
		// Lookup the answer using single engine only
		String[] decisions = lookup.getAnswer(qAndAWithoutQuestions);
		System.out.println(decisions[0]);
		updateText(decisions[0]);
		
		return decisions[1];
		
		

	}

	private String[] getQAString() {

		String[] questionAndAnswers;

		if (!testingMode) {
			// Get the selected images from the controller
			qAImages = controller.getQAImages();

			questionAndAnswers = new String[qAImages.length];
			int i = 0;
			//displayImage(qAImages[1]);
			for (BufferedImage b : qAImages) {
				// Perform OCR
				String recognised;
				try {
					recognised = ocr.recognise(b);
				} catch (Exception ex) {
					System.out.println("Too fast");
					try {Thread.currentThread().sleep(100);} catch (Exception e) {}

					 recognised = ocr.recognise(b);
				}
				
				// Add to Array
				questionAndAnswers[i++] = recognised;
			}
		} else {
			// Use a test question
			questionAndAnswers = testQs;
		}

		return questionAndAnswers;
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

	

}
