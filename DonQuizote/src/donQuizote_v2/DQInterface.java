package donQuizote_v2;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DQInterface implements Writable{
	
	// Necessary Items
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private JTextArea outputBox = new JTextArea(5,30);
	private DonQuizote dq;
	
	public DQInterface(DonQuizote d){
		dq = d;
	}

	public void makeWindow(){

		// Make the mouse listener for any buttons
		toolkit.addAWTEventListener(new Listener(), AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
		
		//Make the window
		JFrame controls = new JFrame("Don Quizote");
	    controls.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Add some buttons
		JButton mappingButton = new JButton("Set Areas");	
		mappingButton.addActionListener(new ActionListener() { 
												public void actionPerformed(ActionEvent actionEvent) {
													// Defer this to the main app
													dq.setAreas();
		      								} });
	
		JButton ocrButton = new JButton("Process Areas");	
		ocrButton.addActionListener(new ActionListener() {
		      								public void actionPerformed(ActionEvent actionEvent) {
		      									//ocrAreas();
		      									updateText("Hello");
		      								} });
		 //mappingButton.addMouseListener(mouseListener);
		 
		 Container contentPane = controls.getContentPane();

		// contentPane.setLayout(new FlowLayout());
		 outputBox.setEditable(false);
		 outputBox.setText("Welcome to Don Quizote v0.1");
		 contentPane.add(mappingButton,BorderLayout.NORTH);
		 contentPane.add(ocrButton,BorderLayout.SOUTH);
		 contentPane.add(new JScrollPane(outputBox),BorderLayout.CENTER);
		 contentPane.doLayout();
		 controls.setSize(500, 400);
		 controls.setVisible(true);
		
	}
	
	public void updateText(String s){
		
		outputBox.setText(outputBox.getText() + "\n" + s);
	
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

}
