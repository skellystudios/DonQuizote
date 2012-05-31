package donQuizote_v1;

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
import java.util.Properties;


class BingLookup implements Lookup
{
	// Make the parser factory and the answer holders
	static XPathFactory factory = null;
	static XPath xpath = null;
	static XPathExpression expr = null;
	private String question, ansA, ansB, ansC, bestguess;
	private int confidence;
	
//	// Config proxy
//	private static String proxy = "gbprdisa1.europe1.com";
//	private static String port = "8080";
	
	
	public BingLookup (String question, String ansA, String ansB, String ansC, String ansD){
		getAnswer (question, ansA, ansB, ansC,  ansD);
	}
	public BingLookup(){
	}
	
	
	public void getAnswer(String question, String ansA, String ansB, String ansC, String ansD){


			try {
		
				System.out.printf("Looking up question\n");
		
				//Make a nice array for people
				double[] hits = new double[4];		for (int i = 0; i < hits.length; i++){ hits[i] = 0;}
				
				// Build and send the request.
				Document[] answerDocsA = query(question, ansA);
				hits[0] = ReturnHits(answerDocsA[0]) / ReturnHits(answerDocsA[1]); 
				
				Document[] answerDocsB = query(question, ansB);
				hits[1] = ReturnHits(answerDocsB[0]) / ReturnHits(answerDocsB[1]); 
				
				Document[] answerDocsC = query(question, ansC);
				hits[2] = ReturnHits(answerDocsC[0]) / ReturnHits(answerDocsC[1]); 
				
				Document[] answerDocsD = query(question, ansD);
				hits[3] = ReturnHits(answerDocsD[0]) / ReturnHits(answerDocsD[1]); 
				
				// Which one won?
				double maxHits = 0.0; int maxID = 1;	String winner;
			
				// Identify the winner
				System.out.printf("Who's the winner: A: " + hits[0] + " B: "+ hits[1] + " C: "+ hits[2] + " \n");
					for (int i = 0; i < hits.length; i++){
					if (maxHits <= hits[i]){
						maxHits = hits[i]; 
						maxID = i;
					}
				}
				String[] answerIDs = {"A", "B", "C", "D"}; 
				winner = answerIDs[maxID];
				
				//System.out.printf("Calc Standev\n");
				double stanDev = StandardDeviation.standardDeviationCalculate(hits);
				//System.out.printf("Calc Standev: " + stanDev + "\n");
				double mean = ((hits[0] + hits[1] + hits[2]) / 3);
				//System.out.printf("Calc mean: " + mean + "\n");
				double distFromMean = maxHits - mean;
				//System.out.printf("dist from mean: " + distFromMean + "\n");
				double sDsFromMean = 0;
				if (stanDev != 0) {  sDsFromMean = distFromMean / stanDev;}
				//System.out.printf("sDs from mean: " + sDsFromMean + "\n");
				double confidence = sDsFromMean * sDsFromMean;
				//System.out.printf("Calc conf: " + confidence + "\n"); 
				double finalconf = confidence / (1.333333);   // Why?
				//System.out.printf("Flawed\n");
				//double confidencePc = confidence * 100.0;
				//System.out.printf("Now print\n");
				System.out.printf("Winner is " + winner + " with "  + sDsFromMean + " sDsFromMean => " + finalconf + " confidence");
				
				//DisplayResults(answerDocsC[1]);
				
				}
			catch (Exception e){ System.out.printf("Something cocked up");}
			
		
		
		
		}
	public String guess() 	{ return "A";		} // Placeholder
	public int	 confidence() { return 100; } // Placeholder
	public String results() { return "BB";} // Placeholder
	 
	

	private Document[] query(String question, String answer){
		try {
			Document[] outputDocs = new Document[2];
			String queryString = question+" %2B\""+answer+"\"";
			String requestURL = BuildRequest(queryString);
			System.out.printf("Searching for: "+ question+" +\""+answer+"\"" + " :");
			outputDocs[0] = GetResponse(requestURL);
				if(outputDocs[0] != null)	{ System.out.printf(" " +ReturnHits(outputDocs[0])+ " hits\n");  } 
				else {System.out.printf(" No hits"); }

			queryString = "info \""+answer+"\"";
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
	@Override
	public void getAnswer(String[] qAndAs) {
		getAnswer(qAndAs[0],qAndAs[1],qAndAs[2],qAndAs[3],qAndAs[4]);
		
	}
}