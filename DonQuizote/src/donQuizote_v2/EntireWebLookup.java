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




public class EntireWebLookup implements Lookup {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntireWebLookup ewl = new EntireWebLookup();
		String[] testQs = { "Who which director directed the film Psycho?", "Quentin Tarantino" ,
				"Alfred Hitchcock", "The Cohen Brothers", "Steven Spielberg"
				};
		ewl.getAnswer(testQs);
		

		
	}
	public static int getNumberOfResults(String s){
		
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
				+ "pz=c5dee9bbe86fc8037e0dff9565e93494"
				
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

	@Override
	public String[] getAnswer(String[] qAs) {	
		
		/*
		 * FIRST - query first the question and +"answer" and then just "answer"
		 */
		
		int numberOfAnswers = qAs.length - 1;
		
		// Hold our results somewhere
		double[] hits = new double[numberOfAnswers]; for (int i = 0; i < hits.length; i++){ hits[i] = 0;}
		double[] qAndAHits = new double[numberOfAnswers];
		double[] aHits = new double[numberOfAnswers];
		int totalHits = 0;
		
		
		for (int i = 0; i < numberOfAnswers; i++){	
			
			// QUESTION AND +"ANSWER"
			
			// Build and send the request.
			String question = qAs[0];
			String answer = qAs[i+1];
			String query1 = question+"+\""+answer+"\"";
			String query2 = "\""+answer+"\"";
			//Assign the variables
			qAndAHits[i] = (double) getNumberOfResults(query1);
			aHits[i] = (double) getNumberOfResults(query2);
			totalHits += qAndAHits[i];
		}
		
		/*
		 * SECOND - Do some processing and report back
		 */
		
		// If we have any q&a hits, then use ratios. Otherwise do just answers
		if (totalHits > 0)
		{
			for (int i = 0; i < numberOfAnswers; i++){
				if (aHits[i] > 0)
				hits[i] = qAndAHits[i] / aHits[i]; 
				else
				hits[i] = 0; 		
			}
		} else {
			for (int i = 0; i < numberOfAnswers; i++){
				hits[i] = aHits[i];
			}
		}
		

		// Which one won?
		double maxHits = 0.0; int maxID = 1;	String winner;
		String[] answerIDs = {"A", "B", "C", "D"}; 
		
		// Identify the winner
		System.out.printf("Who's the winner: ");
			for (int i = 0; i < numberOfAnswers; i++){
				
			System.out.printf(answerIDs[i] + " " + hits[i]);
			if (maxHits <= hits[i]){
				maxHits = hits[i]; 
				maxID = i;
			}
		}
		
		winner = answerIDs[maxID];
		
		
		double stanDev = Utilities.standardDeviationCalculate(hits);
		double sum = 0; for (double d : hits)  sum += d;	
		double mean = sum / hits.length;
		double distFromMean = maxHits - mean;
		double sDsFromMean = 0;
		if (stanDev != 0) {  sDsFromMean = distFromMean / stanDev;}
		double confidence = sDsFromMean * sDsFromMean;
		double finalconf = confidence / (1.333333);   // Why?

		
		String output = "Winner is " + winner + " with "  + sDsFromMean + " sDsFromMean => " + finalconf + " confidence";
		System.out.printf(output);

		String[] s = new String[1];
		s[0] = output;
		s[1] = output;
		return s;
		
	}

	public String guess() 	{ return "A";		} // Placeholder
	public Integer	 confidence() { return 100; } // Placeholder
	public String results() { return "BB";} // Placeholder

}
