package donQuizote_v2;


public interface Lookup{
	
	public String[] getAnswer(String[] qAndAs);
	public String guess();
	public Integer confidence();
	public String results();

	}			
				