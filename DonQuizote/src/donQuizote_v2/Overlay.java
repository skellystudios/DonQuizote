package donQuizote_v2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sun.awt.AWTUtilities;

@SuppressWarnings("restriction")
public class Overlay extends JFrame
{
	
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
    Container container = getContentPane();
    DrawRectangle rectangle = new DrawRectangle();
    
    public Overlay() {
	    setSize(toolkit.getScreenSize());
        setTitle("Overlay");
    	setUndecorated(true);
    	setAlwaysOnTop(true);
    	
    	AWTUtilities.setWindowOpaque(this, false);
    	AWTUtilities.setWindowOpacity(this, 0.8f);
    
        //container.add( new JLabel("Several rectangles are being displayed"), BorderLayout.NORTH );
        //container.add(rectangle);
    }
    public void addRectangle(int startX, int startY, int width, int height, String s) {
        this.rectangle.addRectangle( startX, startY, width, height, s);
        
        container.add(rectangle);
    }
    
    public void addRectangles(HashMap<String, Rectangle> map){
    	for (String s : map.keySet()){
    		Rectangle r = map.get(s);
    		this.addRectangle(r.x, r.y, r.width, r.height, s);
    	}
    }
    
	
    public static void main(String[] args) {
    	Overlay o = new Overlay();
    	o.addRectangle(200, 200, 50, 100, "A window on your soul");

    	o.setVisible(true);
    	
    }
}

    class DrawRectangle extends JPanel {
        private HashMap<String, Rectangle2D> squares;
        //private int a, startX, startY;
        public DrawRectangle(){
            squares = new HashMap<String, Rectangle2D>();
        }

        public void addRectangle(int startX, int startY, int w, int h, String s)  { // square
            squares.put(s,new Rectangle2D.Double(startX, startY, w, h));
            //this.a = a;
            //this.startX = startX;
            //this.startY = startY;
        }
        public void paintComponent(Graphics g) {
            Graphics2D g1 = (Graphics2D) g;
            g1.setPaint(Color.red);
            for( String s : squares.keySet()) {
                g1.setFont(new Font( "SansSerif", Font.BOLD, 12 ));
                Rectangle2D rect = squares.get(s);
                g1.drawString(s, (int) rect.getX(),(int) rect.getY());
                g1.draw(rect);
            }
        }
    }
