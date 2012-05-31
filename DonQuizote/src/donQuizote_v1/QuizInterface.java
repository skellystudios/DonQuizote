package donQuizote_v1;

public interface QuizInterface{
	
	public void StartGame();
	
	public Question getQuestion();
	
	public Result answer(int i);
	
	public boolean isSoon();


}
