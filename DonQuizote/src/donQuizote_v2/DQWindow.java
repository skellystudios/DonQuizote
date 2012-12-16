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
import javax.swing.UIManager;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.Button;
import javax.swing.JScrollBar;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTabbedPane;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;

public class DQWindow implements Writable{

	private JFrame frmDonQuizote;
	private DonQuizote dq;
	private JTextArea textArea;
	private JTextField stateField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DQWindow window = new DQWindow();
					window.frmDonQuizote.setVisible(true);
					
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
		frmDonQuizote.setAlwaysOnTop(true);
		frmDonQuizote.setVisible(true);
	}
	
	public DQWindow(){
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		try
		{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}

		catch (Exception e)
		{
		System.out.println("Unable to load Windows look and feel");
		}
		
		
	//	System.out.println("Init #DQWindow");
		frmDonQuizote = new JFrame();
		frmDonQuizote.setTitle("Don Quizote");
		frmDonQuizote.setAlwaysOnTop(true);
		frmDonQuizote.setBounds(100, 100, 268, 300);
		frmDonQuizote.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frmDonQuizote.setDefaultLookAndFeelDecorated = false;
		JPanel panel = new JPanel();
		frmDonQuizote.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnAnswerQuestion = new JButton("Answer Question");
		panel.add(btnAnswerQuestion);
		btnAnswerQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					dq.answerQuestion();
			}
		});
		
		JButton btnPickColour = new JButton("Pick Colour");
		panel.add(btnPickColour);
		btnPickColour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					//dq.answerQuestion();
					dq.getColour();
			}
		});
		
		JPanel panel_1 = new JPanel();
		frmDonQuizote.getContentPane().add(panel_1, BorderLayout.NORTH);
		
		JButton btnSetAreas = new JButton("Set Areas");
		btnSetAreas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
						dq.setAreas();
			}
		});
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel_1.add(btnSetAreas);
		
		JButton btnProcessAreas = new JButton("Go Automatic");
		btnProcessAreas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					//dq.answerQuestion();
					dq.startProcessing();
			}
		});
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		panel_1.add(horizontalGlue_1);
		panel_1.add(btnProcessAreas);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		panel_1.add(horizontalGlue);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmDonQuizote.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Status", null, panel_2, null);
		panel_2.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		JLabel lblNewLabel = new JLabel("State:");
		panel_2.add(lblNewLabel, "flowx,cell 0 0");
		
		stateField = new JTextField();
		stateField.setEnabled(false);
		stateField.setEditable(false);
		panel_2.add(stateField, "cell 0 0,growx");
		stateField.setColumns(10);
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setEnabled(false);
		textArea_1.setEditable(false);
		panel_2.add(textArea_1, "cell 0 1,grow");
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Logs", null, panel_3, null);
		panel_3.setLayout(new MigLayout("","[182px,grow]","[182px,grow]"));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_3.add(scrollPane, "cell 0 0,grow");
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
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
				                	 //qIDBox.setText(string);	  
				                 }  
				               });     
				         }  
				      });  
				  t.start();  
				  
		
		//System.out.println("HI");
		//qIDBox.repaint();
		//this.notify();
		frmDonQuizote.repaint();
	}	

	public void setState(final String string) {
	   Thread t = new Thread(  
		new Runnable() {  
				         public void run()  
				         {  
				               SwingUtilities.invokeLater(new Runnable(){    
				                 public void run(){  
				                	 stateField.setText(string);	  
				                 }  
				               });     
				         }  
				      });  
				  t.start();  
		stateField.repaint();
		//this.notify();
		frmDonQuizote.repaint();
	}
	

}
