package donQuizote_v1;


public interface Lookup{
	
	public void update(String question, String ansA, String ansB, String ansC, String ansD);
	public String guess();
	public int	 confidence();
	public String results();
	
	
	
	}			
				