package donQuizote_v2;

import java.sql.*;

public class DBDriver
{
  Connection conn;


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
    
    
    private ResultSet query(String query){
    	
    	ResultSet rs = null;
    	    try
    	    {
    	      Statement st = conn.createStatement();
    	       rs = st.executeQuery(query);
    	    
    	    }
    	    catch (SQLException ex)
    	    {
    	      System.err.println(ex.getMessage());
    	    }
    	    
    	    return rs;
    	    
    }
    
    
    private void update(String query){
    	
    	
    	    try
    	    {
    	      Statement st = conn.createStatement();
   	
    	     st.executeUpdate(query);
    	    }
    	    catch (SQLException ex)
    	    {
    	      System.err.println(ex.getMessage());
    	    }
    	    
    }

	public int lookupID(String q) {
		// TODO Auto-generated method stub
		
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
    
  }

  
  
  