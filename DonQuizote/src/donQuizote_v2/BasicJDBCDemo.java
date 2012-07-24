package donQuizote_v2;

import java.sql.*;

public class BasicJDBCDemo
{
  Connection conn;

  public static void main(String[] args)
  {
    new BasicJDBCDemo();
  }
 
  public BasicJDBCDemo()
  {
    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      String url = "jdbc:mysql://localhost/test";
      conn = DriverManager.getConnection(url, "admin", "");
      doTests();
      conn.close();
    }
    catch (ClassNotFoundException ex) {System.err.println(ex.getMessage());}
    catch (IllegalAccessException ex) {System.err.println(ex.getMessage());}
    catch (InstantiationException ex) {System.err.println(ex.getMessage());}
    catch (SQLException ex)           {System.err.println(ex.getMessage());}
  }

  private void doTests()
  {
    doSelectTest();


  }

  private void doSelectTest()
  {
    System.out.println("[OUTPUT FROM SELECT]");
    String query = "SELECT * FROM test";
    try
    {
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery(query);
      while (rs.next())
      {
        String s = rs.getString("test");
   
        System.out.println(s);
      }
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getMessage());
    }
  }

  
}