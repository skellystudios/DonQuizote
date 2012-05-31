package donQuizote_v1;

public class Question {
	
	private String q, a1, a2, a3, a4, tags;
	
	Question(String q, String a1, String a2, String a3, String a4){
		this.q = q;
		this.a1 = a1;
		this.a2 = a2;
		this.a3 = a3;
		this.a4 = a4;
	}
	
	public String question(){
		return q;
	}
	
	public String a1(){
		return a1;
	}
	public String a2(){
		return a2;
	}
	
	public String a3(){
		return a3;
	}
	
	public String a4(){
		return a4;
	}

}
