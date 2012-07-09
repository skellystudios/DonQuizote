package donQuizote_v2;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;



public class GetAreaWorker extends SwingWorker<Rectangle[], String> {

	int numberToGet;
	public Rectangle[] areaOutput;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public GetAreaWorker(int i){
		numberToGet = i;
		areaOutput = new Rectangle[i];
	}
	
	@Override
	protected Rectangle[] doInBackground() throws Exception {
		// TODO Auto-generated method stub
		
	for (int i=0; i<numberToGet; i++){
		
		JFrame frame = new JFrame("");
		TwoClickListener mouseTrap = new TwoClickListener();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setUndecorated(true);
	    JTextArea outputBox = new JTextArea(5,30);
	    outputBox.setEditable(false);
	    //frame.add(outputBox,BorderLayout.CENTER);
	    Font f = new Font("Arial", 30, 100);
	    outputBox.setAlignmentY(0);
	    outputBox.setFont(f);
	    outputBox.setText("HEY");
	    makeTranslucent(frame, Float.valueOf(0.40f));
	    frame.setSize(toolkit.getScreenSize());
	    frame.setVisible(true);
	    
	   
	  frame.addMouseListener(mouseTrap);
	    while(!mouseTrap.done){
	     	//System.out.println("WAITING FOR INPUT");
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } 
	    Rectangle captureArea = new Rectangle(mouseTrap.point1.x,mouseTrap.point1.y,mouseTrap.point2.x-mouseTrap.point1.x, mouseTrap.point2.y-mouseTrap.point1.y);
		mouseTrap.done = true;
	
		

	    frame.dispose();
	   // System.out.println("Closed " + i);
	    Rectangle test = captureArea;
	   // System.out.println("Postclose");
	    areaOutput[i] = captureArea;
	     System.out.println("Gottit " + i + " #GetAreaWorker 1");
	}
	   done2();
	   return areaOutput;
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

	protected void done2() {
		// TODO Auto-generated method stub
		
	}
	 
   
}


class TwoClickListener implements MouseListener {

	 public boolean done = false;
	 public Point point1;
	 public Point point2;
	 int clicks;
	private Rectangle rect;
	private Frame frame;
	
	private TwoClickListener(Rectangle r, Frame frame){ rect = r; clicks = 0; this.frame = frame; }
	public  TwoClickListener() {};
	public void mouseClicked(MouseEvent e) {

		if (!done){	clicks++;
			if (clicks == 1) {
				point1 = e.getPoint();
				System.out.println("Point 1: " + e.getPoint().toString());
				} 
			
			else { point2 = e.getPoint();
				System.out.println("Point 2: " + e.getPoint().toString());
				done = true;
			}
		}

	    
		  
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}
	  
}


