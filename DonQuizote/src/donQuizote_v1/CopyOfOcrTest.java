package donQuizote_v1;

import java.awt.AWTEvent;
//import net.sourceforge.tess4j.*;
import net.gencsoy.tesjeract.EANYCodeChar;
import net.gencsoy.tesjeract.Tesjeract;

import net.sourceforge.javaocr.gui.GUIController;
import net.sourceforge.javaocr.gui.MainFrame;
import net.sourceforge.javaocr.gui.handwritingRecognizer.ConfigPanel;
import net.sourceforge.javaocr.gui.handwritingRecognizer.ProcessPanel;
import net.sourceforge.javaocr.gui.handwritingRecognizer.TrainingPanel;
import net.sourceforge.javaocr.gui.meanSquareOCR.MeanSquareAnalyzer;
import net.sourceforge.javaocr.gui.meanSquareOCR.TrainingImageSpec;
import net.sourceforge.javaocr.ocrPlugins.mseOCR.CharacterRange;
import net.sourceforge.javaocr.ocrPlugins.mseOCR.OCRScanner;
import net.sourceforge.javaocr.ocrPlugins.mseOCR.TrainingImage;
import net.sourceforge.javaocr.ocrPlugins.mseOCR.TrainingImageLoader;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.imageio.ImageIO;
import javax.media.jai.*;

import com.asprise.util.ocr.OCR;

public class CopyOfOcrTest {

	private final short[] invertTable;
	 { invertTable = new short[256];
		for (int i = 0; i < 256; i++) {
		invertTable[i] = (short) (255 - i); } }
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	public Rectangle questionArea = new Rectangle(0,0,0,0); 
	public Rectangle answerAArea = new Rectangle(0,0,0,0); 
	public Rectangle answerBArea = new Rectangle(0,0,0,0); 
	public Rectangle answerCArea = new Rectangle(0,0,0,0); 
	public Rectangle answerDArea = new Rectangle(0,0,0,0); 
	public JTextArea outputBox = new JTextArea(200,200);
	GUIController guiController = new GUIController();  
	Robot robot = new Robot(); 
	ArrayList<TrainingImageSpec> imgs;
	

	
	public CopyOfOcrTest() throws AWTException {
		
		//Set up JOCR 
		
		//Tesjeract n = new Tesjeract("eng");
		
		System.out.println("Hello" + java.lang.System.getProperty("java.library.path"));
  
		TrainingPanel handWriteTrainingPanel = new TrainingPanel(guiController);
		ConfigPanel    handWriteConfigPanel = new ConfigPanel(guiController);
		ProcessPanel handWriteProcess = new ProcessPanel(guiController);
		MainFrame  mainFrame = new MainFrame(guiController);	        
		MeanSquareAnalyzer msa = new MeanSquareAnalyzer(guiController);
		TrainingImageSpec spec1 = new TrainingImageSpec();
		spec1.setFileLocation("/Users/skellystudios/Dev/Itbox/Images/trainingset.gif");
		CharacterRange charRange = new CharacterRange('0','z');
		imgs = new ArrayList<TrainingImageSpec>();
		spec1.setCharRange(charRange);
		imgs.add(spec1);
	
		
		toolkit.addAWTEventListener(new Listener(), AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
		
	      
		JFrame controls = new JFrame("Hello World");
	    controls.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton mappingButton = new JButton("Set Areas");	
		mappingButton.addActionListener(new ActionListener() {
		      								public void actionPerformed(ActionEvent actionEvent) {
		      									getAreas();
		      								} });
		JButton ocrButton = new JButton("Process Areas");	
		ocrButton.addActionListener(new ActionListener() {
		      								public void actionPerformed(ActionEvent actionEvent) {
		      									ocrAreas();
		      								} });
		 
		 //mappingButton.addMouseListener(mouseListener);
		 
		 Container contentPane = controls.getContentPane();
		 contentPane.setLayout(new FlowLayout());
		 outputBox.setEditable(false);
		 outputBox.setText(outputBox.getText() + "Welcome to Don Quizote v0.1");
		 contentPane.add(mappingButton);
		 contentPane.add(ocrButton);
		 contentPane.add(outputBox);
		 controls.setSize(500, 400);
		 controls.setVisible(true);
		
		 
		//After getting the screen dimension create an instance of the Robot class and copy the rectangle from the screen. The createScreenCapture() method in the Robot class can be used to get the BufferedImage of the rectangular dimension passed as argument to it. The following code snippet will help you to do the above said things.
		//to get the BufferedImage with createScreenCapture method

		// prints the results.
			outputBox.setText(outputBox.getText() + "\nSelect the question box");
			questionArea = getArea(); 
			outputBox.setText(outputBox.getText() + "\nSelect answer A");
			answerAArea = getArea(); 
			outputBox.setText(outputBox.getText() + "\nSelect answer B");
			answerBArea = getArea(); 
			outputBox.setText(outputBox.getText() + "\nSelect answer C");
			answerCArea = getArea(); 
			outputBox.setText(outputBox.getText() + "\nSelect answer D");
			answerDArea = getArea(); 
	}
	    
	public void testOcr(Rectangle captureArea){
		
		try{	
		
		BufferedImage image = robot.createScreenCapture(captureArea);
		BufferedImage flippedImage = invertImage(image);
		ImageIO.write(flippedImage, "jpg",new File("out.jpg"));	
			
		String targImageLoc = "out.jpg";
		String ocrd = guiController.performMSEOCR(imgs, targImageLoc);
		System.out.print("WOOOOOOOOOO: " + ocrd);
		}
		catch(Exception e){
			System.out.print("FUCK! " + e.toString());
		}
		
	}
	
	private void getAreas(){
		outputBox.setText(outputBox.getText() + "\nSelect the question box");
		questionArea = getArea(); 
		outputBox.setText(outputBox.getText() + "\nSelect answer A");
		answerAArea = getArea(); 
		outputBox.setText(outputBox.getText() + "\nSelect answer B");
		answerBArea = getArea(); 
		outputBox.setText(outputBox.getText() + "\nSelect answer C");
		answerCArea = getArea(); 
		outputBox.setText(outputBox.getText() + "\nSelect answer D");
		answerDArea = getArea(); 
	
	}
	
	
	private void ocrAreas(){
		testOcr(questionArea);
		String questionText = ocrArea(questionArea);
		String answerA = ocrArea(answerAArea);
		String answerB = ocrArea(answerBArea);
		String answerC = ocrArea(answerCArea);
		String answerD = ocrArea(answerDArea);	
		String totalQuestion = "RESULTS: \n" + "Question: " + questionText + "A: " + answerA + answerB + answerC + answerD;
		System.out.println(totalQuestion);
		outputBox.setText(outputBox.getText() + totalQuestion); 
		
		
	}
	
	private String ocrArea(Rectangle captureArea){
		
		try { 
		//System.out.println("D");
		BufferedImage image = robot.createScreenCapture(captureArea);
		BufferedImage flippedImage = invertImage(image);
		//System.out.println("E");
		String s = new OCR().recognizeEverything(flippedImage);
		
/*		Tesjeract instance = new Tesjeract("eng");
		Graphics2D graphics2D = flippedImage.createGraphics();
		RenderedOp op = JAI.create("filestore", flippedImage, "/Users/skellystudios/egg.tiff", "TIFF");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( flippedImage, "jpg", baos );
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		ByteBuffer converter = ByteBuffer.wrap(imageInByte);
		
		EANYCodeChar[] chararray = instance.recognizeAllWords(converter);
		System.out.print("HERE IT GOES\n");
		for (EANYCodeChar c:chararray) {
			while (c.blanks-- > 0)
				System.out.print(" ");

			System.out.print((char) c.char_code);
		}	
				*/
	

		
		/*JFrame showFrame = new JFrame("Test, yah");
		showFrame.setSize(flippedImage.getWidth(), flippedImage.getHeight());
		JLabel label = new JLabel(new ImageIcon(image));
		showFrame.add(label);
		showFrame.setVisible(true);*/
		
		//System.out.println(s);
		String nocrap = s.substring(199);
		String shortened = nocrap.substring(0, nocrap.length()-2);
		System.out.println(shortened);
		return shortened;
		} catch(Exception e) {return "";}
	}
	
	private Rectangle getArea(){

		Listener2 mouseTrap = new Listener2();
		JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        makeTranslucent(frame, Float.valueOf(0.40f));
        frame.setSize(toolkit.getScreenSize());
        frame.setVisible(true);
        frame.addMouseListener(mouseTrap);
        while(!mouseTrap.done){
         	//System.out.println("WAITING FOR INPUT");
        	try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } 
        Rectangle captureArea = new Rectangle(mouseTrap.point1.x,mouseTrap.point1.y+20,mouseTrap.point2.x-mouseTrap.point1.x, mouseTrap.point2.y-mouseTrap.point1.y);
		mouseTrap.done = true;
      //  System.out.println("Gottit");
        frame.dispose();
        return captureArea;
	}
	
	private BufferedImage invertImage(final BufferedImage src) {
		final int w = src.getWidth();
		final int h = src.getHeight();
		final BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		final BufferedImageOp invertOp = new LookupOp(new ShortLookupTable(0, invertTable), null);
		return invertOp.filter(src, dst);
	}
	
	private void makeTranslucent(Window w, Float f){
		try {
			   Class<?> awtUtilitiesClass = Class.forName("com.sun.awt.AWTUtilities");
			   Method mSetWindowOpacity = awtUtilitiesClass.getMethod("setWindowOpacity", Window.class, float.class);
			   mSetWindowOpacity.invoke(null, w, f);
			} catch (NoSuchMethodException ex) {
			   ex.printStackTrace();
			} catch (SecurityException ex) {
			   ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
			   ex.printStackTrace();
			} catch (IllegalAccessException ex) {
			   ex.printStackTrace();
			} catch (IllegalArgumentException ex) {
			   ex.printStackTrace();
			} catch (InvocationTargetException ex) {
			   ex.printStackTrace();
			}
		
	}
		
	
	 private class Listener implements AWTEventListener {
	        public void eventDispatched(AWTEvent event) {
	            //System.out.print(MouseInfo.getPointerInfo().getLocation() + " | ");
	            //System.out.println(event);
	        }
	        public void mouseClicked(MouseEvent e) {
	            //System.out.printf("Mouse clicked (# of clicks: "
	            //             + e.getClickCount() + ")", e);
	         }
	 }
	 
	 private class Listener2 implements MouseListener {

		 public boolean done = false;
		 public Point point1;
		 public Point point2;
		 int clicks;
		 
		private Listener2(){
			
			clicks = 0;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
			if (!done){
			clicks++;
			if (clicks == 1) {
				point1 = e.getPoint();
				//System.out.printf("Point 1: " + e.getPoint().toString());
			} 
			
			else { point2 = e.getPoint();
			//System.out.printf("Point 2: " + e.getPoint().toString());
			done = true;
			
			}
		
		
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		 
		 
	 }
	    
	


}

