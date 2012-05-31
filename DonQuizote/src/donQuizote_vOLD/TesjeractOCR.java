package donQuizote_v1;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.ImageOutputStream;

import net.gencsoy.tesjeract.EANYCodeChar;
import net.gencsoy.tesjeract.Tesjeract;

import org.junit.Test;

public class TesjeractOCR {
	static {
		if (System.getProperty("os.name").toLowerCase().startsWith("windows"))
			System.loadLibrary("tessdll");

		System.loadLibrary("tesjeract");
		IIORegistry registry = IIORegistry.getDefaultInstance();  
		//registry.registerServiceProvider(new TIFFImageWriterSpi());  
		//registry.registerServiceProvider(new TIFFImageReaderSpi()); 

	}

	// @Test
	public void testConstructor() {
		new Tesjeract("eng");
	}

	private MappedByteBuffer getTiffFile(File f) throws FileNotFoundException, IOException {
		//URL resource = getClass().getResource();
		//String tiffFileName = resource.getFile().replaceAll("%20", " ");
		File tiffFile = f;
		//assertTrue(, tiffFile.exists());
		MappedByteBuffer buf = new FileInputStream(tiffFile).getChannel().map(MapMode.READ_ONLY, 0, tiffFile.length());
		return buf;
	}

	 
	private ByteBuffer imageToTiffBuffer (BufferedImage image) throws IOException
	{
	    ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
	    ImageOutputStream     ios = ImageIO.createImageOutputStream(baos); 

	    // Take the first suitable TIFF writer
	    ImageWriter writer = ImageIO.getImageWritersByFormatName("bmp").next(); 
	    writer.setOutput(ios); 
	    writer.write(image);
	    ios.close();

	    // allocate() doesn't work
	    ByteBuffer buf = ByteBuffer.allocateDirect(baos.size());
	    buf.put(baos.toByteArray());

	    return buf;
	}


	private String EANYCodeChartoString(EANYCodeChar[] ecc){
		String s = "";
		System.out.print("Yah, here");
		for (int j=0; j < ecc.length; j++){
			
				if (ecc[j].blanks != 0)
					s = s.concat(" ");
			
				//System.out.print((char) c.char_code);
			
			s = s.concat(Character.toString(Character.toChars(ecc[j].char_code)[0]));
			System.out.println(j + ": " + ecc[j].blanks + " " + ecc[j].char_code + " " + Character.toString(Character.toChars(ecc[j].char_code)[0]));
		}
		return s;
	}
	
	
	
	
	public String recognizeBuffImage(BufferedImage buf) throws IOException {
		String s = "";
		try {
		
		ByteBuffer bytebuff = imageToTiffBuffer(buf);
		File file = new File("filename.bmp");
		ImageIO.write(buf, "BMP", file);
		bytebuff = getTiffFile(file);
		
		
		
		Tesjeract tess = new Tesjeract("eng");
		EANYCodeChar[] words = tess.recognizeAllWords(bytebuff);
		s = EANYCodeChartoString(words);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

return s;
	}

	//@Test
	public void getLanguages() {
		assertFalse(Tesjeract.getLanguages().isEmpty());
	}
}
