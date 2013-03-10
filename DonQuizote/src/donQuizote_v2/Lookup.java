package donQuizote_v2;

import javax.swing.SwingWorker;


public interface Lookup{

	int getNumberOfResults(String query1);
	
	public void setAnswerEngine(AnswerEngine ae);
	
	public void execute();
	
}			
				 