package donQuizote_v2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;

import net.gencsoy.tesjeract.EANYCodeChar;
import net.gencsoy.tesjeract.Tesjeract;

import org.junit.Test;

public class TesjeractTest {
	static {
		if (System.getProperty("os.name").toLowerCase().startsWith("windows"))
			System.loadLibrary("tessdll");

		System.loadLibrary("tesjeract");
	}

	// @Test
	public void testConstructor() {
		new Tesjeract("eng");
	}

	private File getTiffFile() {
		URL resource = getClass().getResource("test2.bmp");
		String tiffFileName = resource.getFile().replaceAll("%20", " ");
		File tiffFile = new File(tiffFileName);
		assertTrue(tiffFileName, tiffFile.exists());
		return tiffFile;
	}

	 
	private String EANYCodeChartoString(EANYCodeChar[] ecc){
		String s = "";
		for (int j=0; j < ecc.length; j++){
			for (EANYCodeChar c:ecc) {
				if (c.blanks != 0)
					System.out.print("_");
			
				System.out.print((char) c.char_code);
			}
			s = s.concat(Character.toString(Character.toChars(ecc[j].char_code)[0]));
			System.out.println(j + ": " + ecc[j].blanks + " " + ecc[j].char_code + " " + Character.toString(Character.toChars(ecc[j].char_code)[0]));
		}
		return s;
	}
	
	//@Test
	public String recognizeTiffImage() throws IOException {
		File tiff = getTiffFile();
		MappedByteBuffer buf = new FileInputStream(tiff).getChannel().map(MapMode.READ_ONLY, 0, tiff.length());
		Tesjeract tess = new Tesjeract("eng");
		EANYCodeChar[] words = tess.recognizeAllWords(buf);
		
		// If you get STACK_OVER_FLOW error in native, try to increase native stack by -Xss10m.
		//assertEquals("There should be 352 chars in sample tiff image", 352, words.length);
	
		int i = 0;
		//for (char c : "The(quick)".toCharArray()) {
		//assertEquals((int) c, words[i++].char_code);
		//}
		return EANYCodeChartoString(words);
	}

	//@Test
	public void getLanguages() {
		assertFalse(Tesjeract.getLanguages().isEmpty());
	}
}
