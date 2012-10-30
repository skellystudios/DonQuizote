package donQuizote_v2;


import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JScrollPane;


public class DonQuizote {

	// Desiderata
	private QuizController controller;
	private OCREngine ocr;
	private DBDriver db;
	private BufferedImage[] qAImages;
	private Lookup lookup;
	private DQWindow dqwindow;
	public int qID;
	private FiniteStateMachine fsm;
	private Boolean testingMode = false;
	private String[] testQs = { "Who which director directed the film Psycho?",
			"Alfred Hitchcock", "The Cohen Brothers", "Steven Spielburg",
			"Quentin Tarrentino" };

	public static void main(String[] args) {
		new DonQuizote();
	}

	// Main thread
	public DonQuizote() {

		// Load the OCR Engine
		if (testingMode) { ocr = new FakeOCR();} // Fake OCR machine so I can do this on a mac.  
		else {ocr = new TesjeractOCR(); }
		
		// Prepare the interface
		dqwindow = new DQWindow(this);
		// Load the controller
		controller = new QuizController(dqwindow);
		// Connect to the Database
		db = new DBDriver();
		// Add a lookup engine
		lookup = new BingLookup();
		
		// Keep track of the state
		fsm = new FiniteStateMachine(controller);
	}

	// Define the recognition areas
	public void setAreas() {
		controller.setAreas();

	}

	public void startProcessing(){
		System.out.println("DQ: colour is " + controller.startPageColour());
		fsm.doAction();
		//fsm.main
		
	}
	
	// This standalone method will select the question fo	
	public void answerQuestion() {

		// Clear the previous question's data
		cleanUpOldQuestion();

		// Get the question test from OCR or test source
		String[] questionAndAnswers = getQAString();

		// Correct Spelling errors
		String [] questionAndAnswersCorrected = SpellCorrector.correct(questionAndAnswers);
		// TODO: FOR THE LOVE OF GOD STRIP OUT QUOTES OR MY DB DRIVER IS GONNA CRY. Probably should do it here for future matching purposes. Porpoises.
		
		
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
		
		/*
		
		// Lookup the answer using single engine only
		String decision = lookup.getAnswer(questionAndAnswersCorrected);
		System.out.println(decision);
		updateText(decision);
		
		*/

	}

	private String[] getQAString() {

		String[] questionAndAnswers;

		if (!testingMode) {
			// Get the selected images from the controller
			qAImages = controller.getQAImages();

			questionAndAnswers = new String[qAImages.length];
			int i = 0;
			displayImage(qAImages[1]);
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

	@SuppressWarnings("unused")
	private void displayImage(BufferedImage b) {
		LoadAndShow test = new LoadAndShow(b);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new JScrollPane(test));
		f.setSize(b.getWidth(), b.getHeight());
		f.setLocation(200, 200);
		f.setVisible(true);
	}

}
