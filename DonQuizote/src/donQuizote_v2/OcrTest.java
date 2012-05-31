//package donQuizote_v2;
//
//import java.awt.AWTEvent;
//import net.gencsoy.tesjeract.Tesjeract;
//import java.awt.*;
//import java.awt.event.*;
//import java.awt.image.BufferedImage;
//import java.awt.image.BufferedImageOp;
//import java.awt.image.LookupOp;
//import java.awt.image.ShortLookupTable;
//import java.io.BufferedOutputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.nio.ByteBuffer;
//import java.nio.MappedByteBuffer;
//import java.nio.channels.FileChannel.MapMode;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import javax.swing.ImageIcon;
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JTextArea;
//import javax.imageio.ImageIO;
//import com.asprise.util.ocr.OCR;
//
//public class OcrTest {
//
//	private final short[] invertTable;
//	 { invertTable = new short[256];
//		for (int i = 0; i < 256; i++) {
//		invertTable[i] = (short) (255 - i); } }
//	private Toolkit toolkit = Toolkit.getDefaultToolkit();
//	public Rectangle questionArea = new Rectangle(0,0,0,0); 
//	public Rectangle answerAArea = new Rectangle(0,0,0,0); 
//	public Rectangle answerBArea = new Rectangle(0,0,0,0); 
//	public Rectangle answerCArea = new Rectangle(0,0,0,0); 
//	public Rectangle answerDArea = new Rectangle(0,0,0,0); 
//	public JTextArea outputBox = new JTextArea(200,200);
//	
//	//GUIController guiController = new GUIController();  
//	Robot robot = new Robot(); 
//	//ArrayList<TrainingImageSpec> imgs;
//	
//
//	
//	public OcrTest() throws AWTException {
//		
//	
//		// Make the OCR machine
//		TesjeractOCR t = new TesjeractOCR();
//		
//		// Make the control windows
//		makeWindow();
//		
//		 //defineAreas();
//		// defineAreas();
//		/*outputBox.setText(outputBox.getText() + "\nSelect the question box");
//		questionArea = getArea("Select the question box"); 
//		outputBox.setText(outputBox.getText() + "\nSelect answer A");
//		answerAArea = getArea("Select answer A"); 
//		outputBox.setText(outputBox.getText() + "\nSelect answer B");
//		answerBArea = getArea("Select answer B"); 
//		outputBox.setText(outputBox.getText() + "\nSelect answer C");
//		answerCArea = getArea("Select answer C"); 
//		outputBox.setText(outputBox.getText() + "\nSelect answer D");
//		answerDArea = getArea("Select answer D"); */
//		
//		
//		
//		
//	}
//	
//	private void makeWindow(){
//
//		// Make the mouse listener
//		toolkit.addAWTEventListener(new Listener(), AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
//		
//	    
//		JFrame controls = new JFrame("Don Quizote");
//	    controls.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		JButton mappingButton = new JButton("Set Areas");	
//		mappingButton.addActionListener(new ActionListener() { 
//												public void actionPerformed(ActionEvent actionEvent) {
//		      									makeWindow2();
//		      									//defineAreas();
//		      								} });
//	
//		JButton ocrButton = new JButton("Process Areas");	
//		ocrButton.addActionListener(new ActionListener() {
//		      								public void actionPerformed(ActionEvent actionEvent) {
//		      									ocrAreas();
//		      								} });
//		 //mappingButton.addMouseListener(mouseListener);
//		 
//		 Container contentPane = controls.getContentPane();
//		 contentPane.setLayout(new FlowLayout());
//		 outputBox.setEditable(false);
//		 outputBox.setText(outputBox.getText() + "Welcome to Don Quizote v0.1");
//		 contentPane.add(mappingButton);
//		 contentPane.add(ocrButton);
//		 contentPane.add(outputBox);
//		 controls.setSize(500, 400);
//		 controls.setVisible(true);
//		
//	}
//	
//	
//	public void testOcr(Rectangle captureArea){
//		
//		try{	
//		
//		BufferedImage image = robot.createScreenCapture(captureArea);
//		BufferedImage flippedImage = invertImage(image);
//		
//		String ocrd = "a";//guiController.performMSEOCR(imgs, targImageLoc);
//		System.out.print("WOOOOOOOOOO: " + ocrd);
//		}
//		catch(Exception e){
//			System.out.print("FUCK! " + e.toString());
//		}
//		
//	}
//	
//	private void defineAreas(){
//		System.out.println("A");
//		outputBox.setText(outputBox.getText() + "\nSelect the question box");
//		System.out.println("B");
//		questionArea = getArea("Select the question box"); 
//		System.out.println("C");
//		outputBox.setText(outputBox.getText() + "\nSelect answer A");
//		answerAArea = getArea("Select answer A"); 
//		outputBox.setText(outputBox.getText() + "\nSelect answer B");
//		answerBArea = getArea("Select answer B"); 
//		outputBox.setText(outputBox.getText() + "\nSelect answer C");
//		answerCArea = getArea("Select answer C"); 
//		outputBox.setText(outputBox.getText() + "\nSelect answer D");
//		answerDArea = getArea("Select answer D"); 
//	
//	}
//	
//	
//	private void ocrAreas(){
//		testOcr(questionArea);
//		String questionText = ocrArea(questionArea);
//		String answerA = ocrArea(answerAArea);
//		String answerB = ocrArea(answerBArea);
//		String answerC = ocrArea(answerCArea);
//		String answerD = ocrArea(answerDArea);	
//		String totalQuestion = "RESULTS: \n" + "Question: " + questionText + "A: " + answerA + answerB + answerC + answerD;
//		System.out.println(totalQuestion);
//		outputBox.setText(outputBox.getText() + totalQuestion); 
//		new BingLookup(questionText, answerA, answerB, answerC, answerD);
//		
//		
//	}
//	
//	private String ocrArea(Rectangle captureArea){
//		
//		try { 
//		//System.out.println("D");
//		BufferedImage image = robot.createScreenCapture(captureArea);
//		BufferedImage flippedImage = invertImage(image);
//		//System.out.println("E");
//		String s = new TesjeractOCR().recognizeBuffImage(flippedImage);
//		
//		return s;
//		} catch(Exception e) {return "";}
//	}
//	
//	/*private void makeWindow2(){
//		TwoClickListener mouseTrap = new TwoClickListener();
//		JFrame frame = new JFrame("test");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setUndecorated(true);
//        makeTranslucent(frame, Float.valueOf(0.40f));
//        frame.setSize(toolkit.getScreenSize());
//	    frame.setVisible(true);
//	    frame.addMouseListener(mouseTrap); 
//	}*/
//	
//	private getArea(String title){
//
//		
//		JFrame frame = new JFrame(title);
//		TwoClickListener mouseTrap = new TwoClickListener(frame);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setUndecorated(true);
//        makeTranslucent(frame, Float.valueOf(0.40f));
//        frame.setSize(toolkit.getScreenSize());
//        frame.setVisible(true);
//        
//       
//      frame.addMouseListener(mouseTrap);
//        while(!mouseTrap.done){
//         	System.out.println("WAITING FOR INPUT");
//        	try {
//				Thread.sleep(4);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        } 
//        Rectangle captureArea = new Rectangle(mouseTrap.point1.x,mouseTrap.point1.y,mouseTrap.point2.x-mouseTrap.point1.x, mouseTrap.point2.y-mouseTrap.point1.y);
//		mouseTrap.done = true;
//      //  System.out.println("Gottit");
//        frame.dispose();
//        return new Rectangle(10,10);
////        return captureArea;
//	}
//	
//	private BufferedImage invertImage(final BufferedImage src) {
//		final int w = src.getWidth();
//		final int h = src.getHeight();
//		final BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
//		final BufferedImageOp invertOp = new LookupOp(new ShortLookupTable(0, invertTable), null);
//		return invertOp.filter(src, dst);
//	}
//	
//	private void makeTranslucent(Window w, Float f){
//		try {
//			   Class<?> awtUtilitiesClass = Class.forName("com.sun.awt.AWTUtilities");
//			   Method mSetWindowOpacity = awtUtilitiesClass.getMethod("setWindowOpacity", Window.class, float.class);
//			   mSetWindowOpacity.invoke(null, w, f);
//			 
//			} catch (NoSuchMethodException ex) {
//			   ex.printStackTrace();
//			} catch (SecurityException ex) {
//			   ex.printStackTrace();
//			} catch (ClassNotFoundException ex) {
//			   ex.printStackTrace();
//			} catch (IllegalAccessException ex) {
//			   ex.printStackTrace();
//			} catch (IllegalArgumentException ex) {
//			   ex.printStackTrace();
//			} catch (InvocationTargetException ex) {
//			   ex.printStackTrace();
//			}
//	}
//	 
//	
//	 private class Listener implements AWTEventListener {
//	        public void eventDispatched(AWTEvent event) {
//	            //System.out.print(MouseInfo.getPointerInfo().getLocation() + " | ");
//	            //System.out.println(event);
//	        }
//	        public void mouseClicked(MouseEvent e) {
//	            //System.out.printf("Mouse clicked (# of clicks: "
//	            //             + e.getClickCount() + ")", e);
//	         }
//	 }
//	 public class TwoClickListener implements MouseListener {
//
//		 public boolean done = false;
//		 public Point point1;
//		 public Point point2;
//		 int clicks;
//		private Rectangle rect;
//		private Frame frame;
//		
//		private TwoClickListener(Rectangle r, Frame frame){ rect = r; clicks = 0; this.frame = frame; }
//		
//		public void mouseClicked(MouseEvent e) {
//
//			if (!done){	clicks++;
//				if (clicks == 1) {
//					point1 = e.getPoint();
//					System.out.printf("Point 1: " + e.getPoint().toString());
//					} 
//				
//				else { point2 = e.getPoint();
//					System.out.printf("Point 2: " + e.getPoint().toString());
//					done = true;
//				}
//			}
//			
//		    rect.setFrame(point1.x,point1.y,point2.x-point1.x, point2.y-point1.y);
//		    frame.dispose();
//		}
//
//		@Override
//		public void mouseEntered(MouseEvent e) {}
//
//		public void mouseExited(MouseEvent e) {}
//
//		public void mousePressed(MouseEvent e) {}
//
//		public void mouseReleased(MouseEvent e) {}
//		  
//	 }
//}
//
//	