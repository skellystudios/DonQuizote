package donQuizote_v2;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.Button;

public class DQWindow implements Writable{

	private JFrame frame;
	private JTextField qIDBox;
	private DonQuizote dq;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DQWindow window = new DQWindow();
					window.frame.setVisible(true);
					window.frame.setAlwaysOnTop(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DQWindow(DonQuizote d){
		dq = d;
		initialize();
		frame.setVisible(true);
	}
	
	public DQWindow(){
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
	//	System.out.println("Init #DQWindow");
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JLabel lblNewJgoodiesLabel = DefaultComponentFactory.getInstance().createLabel("QID");
		panel.add(lblNewJgoodiesLabel);
		
		qIDBox = new JTextField();
		panel.add(qIDBox);
		qIDBox.setColumns(3);
		
		JButton btnA = new JButton("A");
		panel.add(btnA);
		
		JButton btnB = new JButton("B");
		btnB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel.add(btnB);
		
		JButton btnC = new JButton("C");
		panel.add(btnC);
		
		JButton btnD = new JButton("D");
		panel.add(btnD);
		
		Button button = new Button("Mark True");
		panel.add(button);
		
		Button button_1 = new Button("Mark False");
		panel.add(button_1);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.NORTH);
		
		JButton btnSetAreas = new JButton("Set Areas");
		btnSetAreas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
						dq.setAreas();
			}
		});
		panel_1.add(btnSetAreas);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		panel_1.add(horizontalGlue_1);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		panel_1.add(horizontalGlue);
		
		JButton btnProcessAreas = new JButton("Process Areas");
		btnProcessAreas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					//dq.answerQuestion();
					dq.startProcessing();
			}
		});
		panel_1.add(btnProcessAreas);
		
		textArea = new JTextArea();
		frame.getContentPane().add(textArea, BorderLayout.CENTER);
	}
	
	public void updateText(String s){
		
		textArea.setText(textArea.getText() + "\n" + s);
	
	}

	// Add the QID label
	public void setQID(final String string) {
		
		   Thread t = new Thread(  
				      new Runnable() {  
				         public void run()  
				         {  

				               SwingUtilities.invokeLater(new Runnable(){    
				                 public void run(){  
				                	 qIDBox.setText(string);	  
				                 }  
				               });  
				              
				         }  
				      });  
				  t.start();  
				  
		
		System.out.println("HI");
		qIDBox.repaint();
		//this.notify();
		frame.repaint();
	}

}
