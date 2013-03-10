package donQuizote_v2;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseDriver {
	
	private Connection connection;
	private ResultSet results;
	private PreparedStatement ptmt;
	
	public DatabaseDriver(){		
		initialize();
	}
	
	 public static void main(String[] args) {
		  
		 DatabaseDriver db = new  DatabaseDriver();
		 Question q = db.getQuestion(new String[]{"question' 2", "a", "b", "c", "d"}); 
		 System.out.println(q.qid);
		
	  }
	 
	public void initialize(){
		 try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		    String url = "jdbc:mysql://localhost/donquizote";
		    connection = DriverManager.getConnection(url, "root", "");
		     
		} catch (Exception e) {
			System.out.println("#DatabaseDrive - Could not connect to DB");
			e.printStackTrace();
		}
	}
	
	
	public void addQuestion(String[] qAndAs){  
		try {

	        String queryString = "INSERT IGNORE INTO questions (question, a, b, c, d) VALUES(?,?,?,?,?)";

	        PreparedStatement addStatement = connection.prepareStatement(queryString);
	        addStatement.setString(1, qAndAs[0]);
	        addStatement.setString(2, qAndAs[1]);
	        addStatement.setString(3, qAndAs[2]);
	        addStatement.setString(4, qAndAs[3]);
	        addStatement.setString(5, qAndAs[4]);
	        addStatement.executeUpdate();
	        System.out.println("Data Added Successfully");
	        
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
	}
	
	
	public Question getQuestion(String[] qsAndAnswers){

		// Look up the question - does it exist?
		String queryString = "SELECT * FROM questions WHERE question=?";
		try {
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, qsAndAnswers[0]);
			results = ptmt.executeQuery();
		
			// If no result - add it to the DV, and look it up again
			if (!results.isBeforeFirst()){
				System.out.println("Adding");
				addQuestion(qsAndAnswers);
			}
		
			results = ptmt.executeQuery();
			
		} catch (SQLException e) {
			// Failed to get question
			e.printStackTrace();
		}
		Question q = makeQuestionFromResults(results);
		
		return q;
	}
	
	public Question makeQuestionFromResults(ResultSet rs){
	
		Question q = new Question();
		q.answer = new String[4];
		q.answerCorrect = new Integer[4];
		
	    try {
			while (rs.next())
			  {
			    q.qid = rs.getInt("qID");
			    
			    q.question = rs.getString("question");
			 
			    q.answer[0] = rs.getString("a");
			    q.answer[1] = rs.getString("b");
			    q.answer[2] = rs.getString("c");
			    q.answer[3] = rs.getString("d"); 
			    
			    q.answerCorrect[0] = rs.getInt("acorrect");
			    q.answerCorrect[1] = rs.getInt("bcorrect");
			    q.answerCorrect[2] = rs.getInt("ccorrect");
			    q.answerCorrect[3] = rs.getInt("dcorrect");
			   	
			  }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return q;
	
	}
	
}
