package donQuizote_v2;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class TestClass {

	public static void main(String[] args){
		
			String xml = "<?xml version=\"1.0\" ?> <earth> <country>us</country> </earth>";
		    DocumentBuilderFactory factory = null;
		    DocumentBuilder builder = null;
		    Document ret = null;

		    try {
		      factory = DocumentBuilderFactory.newInstance();
		      builder = factory.newDocumentBuilder();
		    } catch (Exception e) {
		      e.printStackTrace();
		    }

		    try {
		      ret = builder.parse(new InputSource(new StringReader(xml)));
		    } catch (Exception e) {
		      e.printStackTrace();
	
		    }
		    
		  System.out.println(ret);
		  
		
	}
	
}
