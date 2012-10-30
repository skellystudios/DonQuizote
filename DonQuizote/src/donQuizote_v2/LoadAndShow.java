package donQuizote_v2;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
 
public class LoadAndShow extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4811955208890396888L;
	BufferedImage image;
    Dimension size = new Dimension();
 
    public LoadAndShow(BufferedImage image) {
        this.image = image;
        size.setSize(image.getWidth(), image.getHeight());
    }
 
    /**
     * Drawing an image can allow for more
     * flexibility in processing/editing.
     */
    protected void paintComponent(Graphics g) {
        // Center image in this component.
        int x = (getWidth() - size.width)/2;
        int y = (getHeight() - size.height)/2;
        g.drawImage(image, x, y, this);
    }
 
    public Dimension getPreferredSize() { return size; }
 
   
 
    /**
     * Easy way to show an image: load it into a JLabel
     * and add the label to a container in your gui.
     */
    private static void showIcon(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon, JLabel.CENTER);
        JOptionPane.showMessageDialog(null, label, "icon", -1);
    }
}