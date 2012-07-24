package donQuizote_v2;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.xeustechnologies.googleapi.spelling.SpellChecker;
import org.xeustechnologies.googleapi.spelling.SpellCorrection;
import org.xeustechnologies.googleapi.spelling.SpellResponse;

public class DonQuizote {

	// Desiderata
	private QuizController controller;
	private OCREngine ocr;
	private AreaFinder afinder;
	private DBDriver db;
	private BufferedImage[] qAImages;
	private Lookup lookup;
	private DQWindow dqwindow;
	public int qID;

	private int testingMode = 1;
	private String[] testQs = { "Who which director directed the film Psycho?",
			"Alfred Hitchcock", "The Cohen Brothers", "Steven Spielburg",
			"Quentin Tarrentino" };

	public static void main(String[] args) {
		new DonQuizote();
	}

	// Main thread
	public DonQuizote() {

		// Load the OCR Engine
		if (testingMode == 1) {
			ocr = new FakeOCR(); // Fake OCR machine so I can do this on a mac.
		} else {
			ocr = new TesjeractOCR();
		}

		// Prepare the interface
		dqwindow = new DQWindow(this);

		// Load the controller
		controller = new QuizController(dqwindow);

		// Connect to the Database
		db = new DBDriver();

		// Add a lookup engine
		lookup = new BingLookup();
	}

	// Define the recognition areas
	public void setAreas() {
		controller.setAreas();

	}

	public void answerQuestion() {

		// Clear the previous question's data
		cleanUpOldQuestion();

		// Get the question test from OCR or test source
		String[] questionAndAnswers = getQAString();

		// Correct Spelling errors
		questionAndAnswers = SpellCorrector.correct(questionAndAnswers);

		// Add to the DB for Audit
		db.addQuestion(questionAndAnswers[0], questionAndAnswers[1],
				questionAndAnswers[2], questionAndAnswers[3],
				questionAndAnswers[4]);

		// Get the qID and update the interface
		qID = db.lookupID(questionAndAnswers[0]);
		dqwindow.setQID(qID + "");

		// Lookup the answer using single engine only
		String decision = lookup.getAnswer(Arrays.copyOf(testQs, 4));
		System.out.println(decision);
		updateText(decision);

	}

	private String[] getQAString() {

		String[] questionAndAnswers;

		if (testingMode != 1) {
			// Get the selected images from the controller
			qAImages = controller.getQAImages();

			questionAndAnswers = new String[qAImages.length];
			int i = 0;
			for (BufferedImage b : qAImages) {
				// Perform OCR
				String recognised = ocr.recognise(b);
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

	private void displayImage(BufferedImage b) {
		LoadAndShow test = new LoadAndShow(b);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new JScrollPane(test));
		f.setSize(400, 400);
		f.setLocation(200, 200);
		f.setVisible(true);
	}

}
