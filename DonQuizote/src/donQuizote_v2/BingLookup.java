package donQuizote_v2;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


class BingLookup implements Lookup
{
	// Make the parser factory and the answer holders
	static XPathFactory factory = null;
	static XPath xpath = null;
	static XPathExpression expr = null;
	private int numberOfAnswers = 4;
	
//	// Config proxy
//	private static String proxy = "gbprdisa1.europe1.com";
//	private static String port = "8080";
	
	
	public BingLookup(){
	}
	
	public String getAnswer(String[] qAs){

		String output = "";
			try {
		
				System.out.printf("Looking up question\n");
				numberOfAnswers = qAs.length - 1;
				//Make a nice array for people
				double[] hits = new double[numberOfAnswers]; for (int i = 0; i < hits.length; i++){ hits[i] = 0;}
				double[] qAndAHits = new double[numberOfAnswers];
				double[] aHits = new double[numberOfAnswers];
				int totalHits = 0;
		
				
				for (int i = 0; i < numberOfAnswers; i++){
					// Build and send the request.
					Document[] answerDoc = query(qAs[0], qAs[i+1]);
					//Assign the variables
					qAndAHits[i] = ReturnHits(answerDoc[0]);
					aHits[i] = ReturnHits(answerDoc[1]);
					totalHits += qAndAHits[i];
				}
				
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

				
				output = "Winner is " + winner + " with "  + sDsFromMean + " sDsFromMean => " + finalconf + " confidence";
				System.out.printf(output);
				
				}
			catch (Exception e){ System.out.printf("Error in the bing lookup #BLookup");}
			
				return output;
		
		
		}
	
	
	public String guess() 	{ return "A";		} // Placeholder
	public Integer	 confidence() { return 100; } // Placeholder
	public String results() { return "BB";} // Placeholder
	 
	

	private Document[] query(String question, String answer){
		try {
			Document[] outputDocs = new Document[2];
			System.out.println("Question " + question + " *Bing");
			System.out.println("Answer " + answer + " *Bing");
			String queryString = question+" %2B\""+answer+"\"";
			String requestURL = BuildRequest(queryString);
			System.out.printf("Searching for: +" + question + " \"" + answer + "\" :");
			outputDocs[0] = GetResponse(requestURL);
				if(outputDocs[0] != null)	{ System.out.printf(" " +ReturnHits(outputDocs[0])+ " hits\n");  } 
				else {System.out.printf(" No hits"); }

			queryString = "\""+answer+"\"";
			requestURL = BuildRequest(queryString);
			System.out.printf("Searching for: "+ queryString + " :");
			outputDocs[1] = GetResponse(requestURL);
				if(outputDocs[1] != null)	{ System.out.printf(" " +ReturnHits(outputDocs[1])+ " hits\n");  } 
				else {System.out.printf(" No hits"); }
			
			return outputDocs;
			
		}
		
		catch (Exception e){ System.out.printf("Something cocked up");}
		return null;
	}
	
	private String BuildRequest(String searchTerms)
	{
		// Replace the following string with the AppId you received from the
		// Live Search Developer Center.
		String AppId = "0FDEE14F80C71CE0BA92540B22FF5FCA29FEF449";
		String requestString = "http://api.search.live.net/xml.aspx?"

			// Common request fields (required)
			+ "AppId=" + AppId
			+ "&Query=" + searchTerms
			+ "&Sources=Web"

			// Common request fields (optional)
			+ "&Version=2.0"
			+ "&Market=en-gb"
			+ "&Adult=Moderate"

			// Web-specific request fields (optional)
			+ "&Web.Count=1"
			+ "&Web.Offset=0"
			//+ "&Web.FileType=DOC"
			+ "&Web.Options=DisableHostCollapsing+DisableQueryAlterations"
			;

		return requestString;
	}

	private Document GetResponse(String requestURL) throws ParserConfigurationException, SAXException, 
IOException 
	{
//		Properties systemProperties = System.getProperties();
//		systemProperties.setProperty("http.proxyHost",proxy);
//		systemProperties.setProperty("http.proxyPort",port);
//		THIS IS ONLY NECESSARY IF YOU NEED TO DO THIS FROM BEHIND A PROXY SERVER
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = null;
		DocumentBuilder db = dbf.newDocumentBuilder();

		if (db != null)
		{              
			doc = db.parse(requestURL);
		}

		return doc;
	}

	private double ReturnHits(Document doc) throws XPathExpressionException
	{
	double total = 0;
	factory = XPathFactory.newInstance();
		xpath = factory.newXPath();
		xpath.setNamespaceContext(new APINameSpaceContext());
		NodeList errors = (NodeList) xpath.evaluate("//api:Error",doc,XPathConstants.NODESET);

		if(errors != null && errors.getLength() > 0 )
		{
			// There are errors in the response. Display error details.
			DisplayErrors(errors);
			total = 0;
		}
		else
		{
			String hitsString = (String)xpath.evaluate("//web:Web/web:Total",doc,XPathConstants.STRING);
			//System.out.printf("DEBUG " + hitsString + "\n");
			 total = Double.parseDouble(hitsString);
		}
		return total;
	}
	
	@SuppressWarnings("unused")
	private static void DisplayResponse(Document doc) throws XPathExpressionException
	{
		factory = XPathFactory.newInstance();
		xpath = factory.newXPath();
		xpath.setNamespaceContext(new APINameSpaceContext());
		NodeList errors = (NodeList) xpath.evaluate("//api:Error",doc,XPathConstants.NODESET);

		if(errors != null && errors.getLength() > 0 )
		{
			// There are errors in the response. Display error details.
			DisplayErrors(errors);
		}
		else
		{
			DisplayResults(doc);
		}
	}

	private static void DisplayResults(Document doc) throws XPathExpressionException 
	{
		String version = (String)xpath.evaluate("//@Version",doc,XPathConstants.STRING);
		String searchTerms = (String)xpath.evaluate("//api:SearchTerms",doc,XPathConstants.STRING);
		int total = Integer.parseInt((String)xpath.evaluate("//web:Web/web:Total",doc,XPathConstants.STRING));
		int offset = Integer.parseInt((String)xpath.evaluate("//web:Web/web:Offset",doc,
XPathConstants.STRING));
		NodeList results = (NodeList)xpath.evaluate("//web:Web/web:Results/web:WebResult",doc,
XPathConstants.NODESET); 

		// Display the results header.
		System.out.println("Live Search API Version " + version);
		System.out.println("Web results for " + searchTerms);
		System.out.println("Displaying " + (offset+1) + " to " + (offset + 
results.getLength()) + " of " + total + " results ");
		System.out.println();

		// Display the Web results.
		StringBuilder builder = new StringBuilder();

		for(int i = 0 ; i < results.getLength(); i++)
		{
			NodeList childNodes = results.item(i).getChildNodes();

			for (int j = 0; j < childNodes.getLength(); j++) 
			{
				if(!childNodes.item(j).getLocalName().equalsIgnoreCase("DisplayUrl"))
				{
					String fieldName = childNodes.item(j).getLocalName();

					if(fieldName.equalsIgnoreCase("DateTime"))
					{
						fieldName = "Last Crawled";
					}

					builder.append(fieldName + ":" + childNodes.item(j).getTextContent());
					builder.append("\n");
				}
			}

			builder.append("\n");
		}

		System.out.println(builder.toString());
	}

	private static void DisplayErrors(NodeList errors) 
	{
		System.out.println("Live Search API Errors:");
		System.out.println();

		for (int i = 0; i < errors.getLength(); i++) 
		{
			NodeList childNodes = errors.item(i).getChildNodes();

			for (int j = 0; j < childNodes.getLength(); j++) 
			{
				System.out.println(childNodes.item(j).getLocalName() + ":" + childNodes.item(j).getTextContent());
			}

			System.out.println();
		}
	}
	

}