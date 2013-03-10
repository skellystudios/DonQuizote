package donQuizote_v2;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.*;

public class DBDriver
{
  Connection conn;

	/* ### answerCorrect definintions 
	 * 0 = not known
	 * 1 = definitely true
	 * 2 = definitely false
	 */

  public static void main(String[] args) {
	  
	  DBDriver db = new  DBDriver();
	  
	  String[] qas = {"q1 blah blah","a","b","c","d"};
	  Question q = db.lookupQuestion(qas);
	  

	  for (int i = 0 ; i < q.answer.length; i++){
		  System.out.print(q.answer[i]);
		  System.out.println(q.answerCorrect[i]);
	  }

	  q.answerCorrect[1] = 1;
	  db.insertOrUpdate(q);
	  
	  q = db.lookupQuestion(qas);
	  System.out.println(q.question);
	  for (int i = 0 ; i < q.answer.length; i++){
		  System.out.println(q.answer[i]);
		  System.out.print(q.answerCorrect[i]);
	  }
	  
  }
  
  
  public DBDriver(){
	    try
	    {
	      Class.forName("com.mysql.jdbc.Driver").newInstance();
	      String url = "jdbc:mysql://localhost/donquizote";
	      conn = DriverManager.getConnection(url, "root", "");
	    }
	    catch (ClassNotFoundException ex) {System.out.println(ex.getMessage());}
	    catch (IllegalAccessException ex) {System.out.println(ex.getMessage());}
	    catch (InstantiationException ex) {System.out.println(ex.getMessage());}
	    catch (SQLException ex)           {System.out.println(ex.getMessage());}
  }

  public void addQuestion(String q, String a, String b, String c, String d )
  {	  
//    String query = "IF NOT EXISTS " +
//    		"(SELECT * FROM  questions WHERE " +
//    		"question = '"+q+"') INSERT INTO questions (question, a, b, c, d) " +
//    		"VALUES ('"+q+"','"+a+"','"+b+"','"+c+"','"+d+"')";
//    
    
    String query = "INSERT IGNORE INTO questions (question, a, b, c, d) " +
    		"VALUES ('"+URLEncoder.encode(q)+"','"+a+"','"+b+"','"+c+"','"+d+"')";
    
    System.out.println("#DB: Running query "+query+" ");
    
    Integer i = -1;
    ResultSet rs = update(query);
    
  }
    
    // Update the database from a question object. Maybe should return the inserted ID?
  	public void insertOrUpdate(Question q){
  		
  		
  		if (q.qid == null) q.qid =  lookupID(q.question);
  		// Already a q, do an update
  		if (q.qid > 0) {
  			// Do an update
 			 String query = "REPLACE INTO questions (qid, question, a, b, c, d, acorrect, bcorrect, ccorrect, dcorrect, correct) " +
   		    		"VALUES ('"+q.qid+","+URLEncoder.encode(q.question)+"','"+q.answer[0]+"','"+q.answer[1]+"','"+q.answer[2]+"','"+q.answer[3]+
   		    		"','"+q.answerCorrect[0]+"','"+q.answerCorrect[1]+"','"+q.answerCorrect[2]+"','"+q.answerCorrect[3]+
   		    		"','"+(q.correct()?"1":"0")+"')";
   		  	 update(query);
  			
  		}
  		else 
  		{
  			// Do an insert
  			 String query = "INSERT IGNORE INTO questions (question, a, b, c, d) " +
  		    		"VALUES ('"+URLEncoder.encode(q.question)+"','"+q.answer[0]+"','"+q.answer[1]+"','"+q.answer[2]+"','"+q.answer[3]+"')";
  		    update(query);
  			
  		}
  	}
  
  	private ResultSet query(String query){
    	ResultSet rs = null;
    	try {	
    		Statement st = conn.createStatement();
    		rs = st.executeQuery(query);
    	} 
    	catch (SQLException ex){
    	      System.err.println(ex.getMessage());
    	    }
    	return rs;
   }
    
    
    private ResultSet update(String query){
    	try {
    		Statement st = conn.createStatement();
    		st.executeUpdate(query);
    		 
    		return null;
    	}
    	catch (SQLException ex)
    	    {
    	    System.out.println(ex.getMessage());
    	    }
    	return null;
    	    
    }

	public int lookupID(String q) {

		int i = -1;
		String queryString = "SELECT qID FROM questions WHERE question='"+URLEncoder.encode(q)+"'";
		ResultSet rs = query(queryString);
	    try {
			while (rs.next())
			  {
			    String s = rs.getString("qID");
			    i = Integer.parseInt(s);
			  }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}
	
	public Question lookupQuestion(String[] qastrings) {
		
		int id = lookupID(qastrings[0]);
		// If not already in, add it
		if (id == -1) {
			 addQuestion(qastrings[0], qastrings[1],qastrings[2], qastrings[3],	qastrings[4]);
			 id = lookupID(qastrings[0]);
		}
		
		Question q = new Question();
		q.answer = new String[4];
		q.answerCorrect = new Integer[4];
		
		String queryString = "SELECT * FROM questions WHERE qid ="+id;
		ResultSet rs = query(queryString);
	    try {
			while (rs.next())
			  {
			    q.qid = rs.getInt("qID");
			    
			    q.question = URLDecoder.decode(rs.getString("question"));
			    
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

  
  
  