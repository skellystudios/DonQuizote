package donQuizote_v2;

import java.net.URL;
import java.net.URLEncoder;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.Node;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NameList;
import org.w3c.dom.NodeList;




public class EntireWebLookup  {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntireWebLookup ewl = new EntireWebLookup();
		String[] testQs = { "Who which director directed the film Psycho?", "Quentin Tarantino" ,
				"Alfred Hitchcock", "The Cohen Brothers", "Steven Spielberg"
				};
		//ewl.getAnswer(testQs);
		

		
	}
	public int getNumberOfResults(String s){
		
		Document doc = getSearchResults(s); 
		
		try {
		
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile("//QUERY/@ESTIMATE");
	
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
	
			NodeList  nodes = (NodeList) result;
			String count = nodes.item(0).getNodeValue();
			
			System.out.println("# EWLookup - Queried [" + s + "] and got " + count + " results.");
			
			return Integer.parseInt(count);
			
		}
		catch (Exception e)
		{
			System.out.println(e);
			return 0;
		}
	

		
	}

	public static Document getSearchResults(String query){
		
		String url = "http://www.entireweb.com/xmlquery?"
				// The token (stolen!)
				+ "pz=ed5bda2d998e586f6e5aa97940251e27"
				
				// An IP address
				+ "&ip=192.168.0.1"
				
				// In XML please
				+ "&format=xml+"
				
				// SF = clustering, we want 0, n=1 for speed
				 + "&n=&sc=0" 
				
				//And then the query
				+ "&q=" + URLEncoder.encode(query);


		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new URL(url).openStream());
			System.out.println(doc.toString());
			return doc;
			
			
		} catch (Exception e) {
			System.out.println(e);
			// We don't really care
		}
		
		return null;
	

	}

	public String guess() 	{ return "A";		} // Placeholder
	public Integer	 confidence() { return 100; } // Placeholder
	public String results() { return "BB";} // Placeholder

}
