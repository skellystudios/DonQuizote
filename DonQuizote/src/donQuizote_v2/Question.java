package donQuizote_v2;

public class Question {
	
	/* ### answerCorrect definintions 
	 * 0 = not known
	 * 1 = definitely true
	 * 2 = definitely false
	 */
	
	public final int numberofAnswers = 4;
	public String question;
	public String[] answer = new String[numberofAnswers];
	public Boolean correct;
	public Integer[] answerCorrect = new Integer[numberofAnswers];
	public Integer qid;
	
	public int getNextAnswer(){
		for (int i = 0; i<numberofAnswers; i++){
			if ((!correct && answerCorrect[i]!= 2)
					||(correct && answerCorrect[i]== 1) ){
				return i;
			}
		}
		return 3;
	}
}
