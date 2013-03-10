package donQuizote_v2;

public class Question {
	
	/* ### answerCorrect definintions 
	 * 0 = not known
	 * 1 = definitely true
	 * 2 = definitely false
	 */
	
	public final int numberofAnswers = 4;
	
	public Integer qid;
	
	public String question;
	public String[] answer = new String[numberofAnswers];
	public Integer[] answerCorrect = new Integer[numberofAnswers];
	
	public Boolean correct(){
		for (int j : answerCorrect){
			if (j == 1) return true;
		}
		return false;
	}
	
	public Integer correctID(){
		
		int i = 0;
		for (int j : answerCorrect){
			if (j == 1) return i;
			i++;
		}
		
		return -1;
		
	}
	
	
	public int getNextAnswer(){
		for (int i = 0; i<numberofAnswers; i++){
			if ((!correct() && answerCorrect[i]!= 2)
					||(correct() && answerCorrect[i]== 1) ){
				return i;
			}
		}
		return 3;
	}
}
