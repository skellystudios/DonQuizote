package donQuizote_v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnswerEngine {
	
	private boolean methodLocked = false;
	private HashMap<String, Integer> results;
	private int answersWaiting;
	public AnswerEngine(){
		lookup = new BingAzureLookup();
	}
	
	public Lookup lookup;
	
	public void add(Lookup l){
		l.setAnswerEngine(this);
	}
	
	public void addAnswer(String s, Integer i){
		
		while(methodLocked){
			try {Thread.sleep(100);	} 
			catch (InterruptedException e) { e.printStackTrace();} 
		}
		methodLocked = true;
			results.put(s,i);
			answersWaiting--;
		methodLocked = false;
		
	}
	
	public void getResults(List<String> queries){
		
		answersWaiting = 0;
		results = new HashMap<String, Integer>();
		
		
		List<Lookup> lookups = new ArrayList<Lookup>();

		
		for (String query : queries) {
			lookups.add(new BOSSLookup(this, query));
			answersWaiting++;
		}
		
		for (Lookup l : lookups){
			l.execute();
		}
		
		while (answersWaiting > 0){
			snooze(100);
		}
		
		/*
		for (String query : queries) {
		 
			System.out.println(query + ' ' + results.get(query));
		}
		*/
	
	}
	
	public static void main(String[] args0){
		
		AnswerEngine ae = new AnswerEngine();
		Question q = new Question();
		q.answer = new String[]{"Alfred Hitchcock", "The Cohen Brothers", "Steven Spielburg",
				"Quentin Tarrentino" };
		q.question = "Which famous director directed the film Reservoir Dogs";
		q.answerCorrect = new Integer[]{0,0,0,0};
		ae.answerQuestion(q);
	
	}
	
	public static void snooze(int i){
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int answerQuestion(Question q){
		
		System.out.println("AE: " + q.question);
		
		int  numberOfAnswers = 4;
		
		// Let's all save time here, kids
		if (q.correct()){ 
			return q.correctID(); 
		}
	
		// Make a List of the strings we want to query
		List<Integer> answerIDs = new ArrayList<Integer>();
		List<String> answerStrings = new ArrayList<String>();
		List<String> questionAnswerStrings = new ArrayList<String>();
		for (int i = 0; i<q.answer.length; i++){
			if (q.answerCorrect[i] != 2){

				//questionAnswerStrings.add(q.question+"  +\""+q.answer[i]+"\"");
				questionAnswerStrings.add("(" + q.question+")  AND ("+q.answer[i]+")");
				answerStrings.add("("+q.answer[i]+")");
				answerIDs.add(i);
			}
		}
		
		List<String> queries = new ArrayList<String>();
		queries.add(q.question);
		queries.addAll(questionAnswerStrings);
		queries.addAll(answerStrings);
		
		// Pull the results into the mix
		getResults(queries);
	

		// Adjust to a score
		double[] score = new double[numberOfAnswers];		
		for (int i = 0; i < numberOfAnswers; i++){
			
				int singleHits = results.get(answerStrings.get(i));
				System.out.println("#AE Results for " + answerStrings.get(i) + " with "+ singleHits);
				int bothHits = results.get(questionAnswerStrings.get(i));
				System.out.println("#AE Results for " + questionAnswerStrings.get(i) + " with "+ bothHits);
				
				// Put in adjusted hits
				double adjustedHits = singleHits > 0 ?  1000* bothHits / singleHits : 0; 	
				score[i] = adjustedHits;
				
				System.out.println("#AE Results for " + i + ". " + answerStrings.get(i) + ": " + singleHits + " vs " + bothHits + " = " + adjustedHits);
		}
		

		// Which one won?
		double maxHits = 0.0; int maxID = 1;
	
		
		// Identify the winner
		System.out.printf("#AE: Who's the winner: ");
		for (int i = 0; i < numberOfAnswers; i++){
			System.out.printf(answerIDs.get(i) + " " + score[i]);
			if (maxHits <= score[i]){
				maxHits = score[i]; 
				maxID = i;
			}			
		}
	
		int winner = answerIDs.get(maxID);

		String output = "#AE: Winner is " + winner; // + " with "  + sDsFromMean + " sDsFromMean => " + finalconf + " confidence";		 
		System.out.println(output);
		return maxID;		
	}

	/*
	double stanDev = Utilities.standardDeviationCalculate(hits);
	double sum = 0; for (double d : hits)  sum += d;	
	double mean = sum / hits.length;
	double distFromMean = maxHits - mean;
	double sDsFromMean = 0;
	if (stanDev != 0) {  sDsFromMean = distFromMean / stanDev;}
	double confidence = sDsFromMean * sDsFromMean;
	double finalconf = confidence / (1.333333);   // Why?
	 */
	
}
