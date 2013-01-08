package donQuizote_v2;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.soap.Node;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.exception.NestableRuntimeException;  
import org.w3c.dom.Document;
import org.w3c.dom.NameList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;




public class BingAzureLookup implements Lookup {

	
	static Document doc;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BingAzureLookup bal = new BingAzureLookup();
		String[] testQs = { "Who which director directed the film Psycho?", "Steven Spielberg", "Quentin Tarantino" ,
				"Hitchcock", "The Coen Brothers"
				};
		bal.getAnswer(testQs);
		

		
	}
	public static int getNumberOfResults(String s){
		
		String count = getSearchResults(s); 
		if (count == "") count = "0";
			
			System.out.println("# EWLookup - Queried [" + s + "] and got " + count + " results.");
			
			return Integer.parseInt(count);
			

		
	}

	public static String getSearchResults(String query){
		
		query = URLEncoder.encode(query);
        String bingUrl = "https://api.datamarket.azure.com/Data.ashx/Bing/Search/v1/Composite?" +
        		"Sources=%27web%27" +
        		"&Query=%27"+query+"%27" +
        		"&WebSearchOptions=%27DisableQueryAlterations%27" +
        		"&Market=%27en-US%27" +
        		"&$top=1" +
        		"&$format=JSON"
        		;

        String accountKey = "amnSjlBAdRgkD1YQcZFdmZS9nSQopCQIWnFtM51kxnw=";
        String aKey = accountKey + ":" + accountKey;

		byte[] accountKeyBytes =  Base64.encodeBase64(aKey.getBytes());
        String accountKeyEnc = new String(accountKeyBytes);
        
        try {
        URL url = new URL(bingUrl);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);          
        InputStream is = urlConnection.getInputStream();
      
        // Read to string
        InputStreamReader isr = new InputStreamReader(is);
        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuilder sb = new StringBuilder();
        while ((numCharsRead = isr.read(charArray)) > 0) {
            sb.append(charArray, 0, numCharsRead);
        }
        
        
        // Parse JSON
        JSONObject json = (JSONObject) JSONSerializer.toJSON(sb.toString());
        String s = json.getJSONObject("d").getJSONArray("results").getJSONObject(0).getString("WebTotal");
        
        
		return s;
				
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
			String query1 = question+" &\""+answer+"\"";
			//String query2 = "\""+answer+"\"";
			String query2 = answer;
			
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

		String[] s = new String[2];
		s[0] = output;
		s[1] = winner;
		return s;
		
	}

	public String guess() 	{ return "A";		} // Placeholder
	public Integer	 confidence() { return 100; } // Placeholder
	public String results() { return "BB";} // Placeholder

}
