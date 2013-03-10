package donQuizote_v2;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;


import javax.swing.SwingWorker;
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

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.exception.NestableRuntimeException;  
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NameList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;




public class BOSSLookup extends SwingWorker implements Lookup {

	
	static Document doc;
	private static final Logger log = Logger.getLogger(BOSSLookup.class); 
	protected static String yahooServer = "http://yboss.yahooapis.com/ysearch/";  
	private static String consumer_key = "dj0yJmk9S2I5N2FiMnlMd2IyJmQ9WVdrOVlXdzJUa1pxTjJFbWNHbzlNakV3TkRnMk1USTJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD1lZg--";  
	private static String consumer_secret = "1dc3def4ac55ca4df44d149303f96644519f68bb";  

	/** The HTTP request object used for the connection */  
	private static StHTTPRequest httpRequest = new StHTTPRequest();  
	private static final String ENCODE_FORMAT = "UTF-8";  
	private static final String callType = "web";  
	private static final int HTTP_STATUS_OK = 200; 
	
	private AnswerEngine ae;
	private String queryString;
	
	public BOSSLookup(AnswerEngine ae, String queryString1){
		this.ae = ae;
		this.queryString = queryString1;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BOSSLookup bl = new BOSSLookup(null, null);
		String[] testQs = { "Who which director directed the film Psycho?", "Steven Spielberg", "Quentin Tarantino" ,
				"Hitchcock", "The Coen Brothers"
				};
		//bl.getAnswer(testQs);
		String query = "(Which famous director directed the film Reservoir Dogs)  +(Steven Spielberg)";
		int i = bl.getNumberOfResults(query);
		System.out.println("Number of results: " + i);
		
	}
	
	public int getNumberOfResults(String s){
		
		String output = getSearchResults(s); 
		
		JSONObject json = (JSONObject) JSONSerializer.toJSON(output);
	    String count = json.getJSONObject("bossresponse").getJSONObject("web").getString("totalresults");
	   // System.out.println("#BOSS count is " + count);  
		
		if (count == "") count = "0";

		System.out.println("# BOSS - Queried [" + s + "] and got " + count + " results.");
	
		try {
				return Integer.parseInt(count);
			} catch (Exception e) {
				// Just means we didn't get results
			}
		return 0;	

		
	}

	public static String getSearchResults(String query){
		
		try {
		// Start with call Type  
		String params = callType;  
		  
		// Add query  
		params = params.concat("?q=");  
		  
		// Encode Query string before concatenating  
		query = URLEncoder.encode(query);
		params = params.concat(URLEncoder.encode(query, "UTF-8"));  
		  
		// Create final URL  
		String url = yahooServer + params;  
		  
		// Create oAuth Consumer   
		OAuthConsumer consumer = new DefaultOAuthConsumer(consumer_key, consumer_secret);  
		  
		// Set the HTTP request correctly  
		httpRequest.setOAuthConsumer(consumer);  
		  
		  
		log.info("sending get request to" + URLDecoder.decode(url, ENCODE_FORMAT));  
		int responseCode = httpRequest.sendGetRequest(url);   
		  
		// Send the request  
		if(responseCode == HTTP_STATUS_OK) {  
		log.info("Response ");  
		} else {  
		log.error("Error in response due to status code = " + responseCode);  
		}  
		
		String response = httpRequest.getResponseBody();  
		System.out.println(response);  

		return response;
				
		} catch (Exception e) {
			System.out.println(e);
			// We don't really care
		}
		
		return null;
	

	}



	public String guess() 	{ return "A";		} // Placeholder
	public Integer	 confidence() { return 100; } // Placeholder
	public String results() { return "BB";} // Placeholder


	@Override
	protected Object doInBackground() throws Exception {
		
		int results = getNumberOfResults(queryString);
		ae.addAnswer(queryString, results);
		
		return null;
	}


	@Override
	public void setAnswerEngine(AnswerEngine ae) {
		this.ae = ae;	
	}
	
	public void nothing(){
	}
	
}



	/* NOW JUNK??
	 * 
	public String[] getAnswer(String[] qAs) {	
		
		
		 //FIRST - query first the question and +"answer" and then just "answer"
		 
		
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
		
		
		 //SECOND - Do some processing and report back
		 
		
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
		*/
	
