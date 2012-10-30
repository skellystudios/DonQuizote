package donQuizote_v2;

import java.sql.*;

public class DBDriver
{
  Connection conn;

	/* ### answerCorrect definintions 
	 * 0 = not known
	 * 1 = definitely true
	 * 2 = definitely false
	 */

  public DBDriver()
  {
    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      String url = "jdbc:mysql://localhost/donquizote";
      conn = DriverManager.getConnection(url, "root", "");
    }
    catch (ClassNotFoundException ex) {System.err.println(ex.getMessage());}
    catch (IllegalAccessException ex) {System.err.println(ex.getMessage());}
    catch (InstantiationException ex) {System.err.println(ex.getMessage());}
    catch (SQLException ex)           {System.err.println(ex.getMessage());}
  }

  public int addQuestion(String q, String a, String b, String c, String d )
  {	  
//    String query = "IF NOT EXISTS " +
//    		"(SELECT * FROM  questions WHERE " +
//    		"question = '"+q+"') INSERT INTO questions (question, a, b, c, d) " +
//    		"VALUES ('"+q+"','"+a+"','"+b+"','"+c+"','"+d+"')";
//    
    
    String query = "INSERT IGNORE INTO questions (question, a, b, c, d) " +
    		"VALUES ('"+q+"','"+a+"','"+b+"','"+c+"','"+d+"')";
    
    System.out.println("Running query "+query+" #DBDriver");
    
    update(query);
    
    
    return 0;
  }
    
    // Update the database from a question object. Maybe should return the inserted ID?
  	public void insertOrUpdate(Question q){
  		
  		
  		if (q.qid == null) q.qid =  lookupID(q.question);
  		// Already a q, do an update
  		if (q.qid > 0) {
  			// Do an update
 			 String query = "REPLACE INTO questions (question, a, b, c, d, acorrect, bcorrect, ccorrect, dcorrect, correct) " +
   		    		"VALUES ('"+q.question+"','"+q.answer[0]+"','"+q.answer[1]+"','"+q.answer[2]+"','"+q.answer[3]+
   		    		"','"+q.answerCorrect[0]+"','"+q.answerCorrect[1]+"','"+q.answerCorrect[2]+"','"+q.answerCorrect[3]+
   		    		"','"+(q.correct?"True":"False")+"')";
   		    update(query);
  			
  		}
  		else 
  		{
  			// Do an insert
  			 String query = "INSERT IGNORE INTO questions (question, a, b, c, d) " +
  		    		"VALUES ('"+q.question+"','"+q.answer[0]+"','"+q.answer[1]+"','"+q.answer[2]+"','"+q.answer[3]+"')";
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
    
    
    private void update(String query){
    	try {
    		Statement st = conn.createStatement();
    		st.executeUpdate(query);
    	}
    	catch (SQLException ex)
    	    {
    	    System.err.println(ex.getMessage());
    	    }
    	    
    }

	public int lookupID(String q) {

		int i = 0;
		String queryString = "SELECT qID FROM questions WHERE question='"+q+"'";
		ResultSet rs = query(queryString);
	    try {
			while (rs.next())
			  {
			    String s = rs.getString("qID");
			    System.out.println(s);
			    i = Integer.parseInt(s);
			  }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}

	public Question lookupQuestion(String string) {
		// TODO Auto-generated method stub
		return null;
	}
    
  }

  
  
  