	package donQuizote_v2;

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


public class AreaFinder {
	
	private Rectangle area;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	public AreaFinder(){
	}
	
		public Rectangle getArea(){
		JFrame frame = new JFrame("");
		TwoClickListener mouseTrap = new TwoClickListener();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setUndecorated(true);
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
	    Rectangle captureArea = new Rectangle(mouseTrap.point1.x,mouseTrap.point1.y,mouseTrap.point2.x-mouseTrap.point1.x, mouseTrap.point2.y-mouseTrap.point1.y);
		mouseTrap.done = true;
	  //  System.out.println("Gottit");
	    frame.dispose();
	   
	   return captureArea;
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
	 
   
}
