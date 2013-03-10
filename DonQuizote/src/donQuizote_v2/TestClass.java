package donQuizote_v2;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class TestClass {

	public static void main(String[] args){
		
		URL url;
		InputStream is = null;
		DataInputStream dis;
		String line;

		try {
		    url = new URL("https://www.google.com/search?q=google&gbv=1&um=1&ie=UTF-8&tmb=isch&source=og&sa=N&tab=wi");
		    is = url.openStream();  // throws an IOException
		    dis = new DataInputStream(new BufferedInputStream(is));

		    while ((line = dis.readLine()) != null) {
		        System.out.println(line);
		    }
		} catch (Exception mue) {
		     mue.printStackTrace();
	
		
		}
		  
		
	}
	
}
