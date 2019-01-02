import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.*;
import java.net.URL;
public class TopClass {
    //global constant variables
    private static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    //global variables
    //global swing objects
    private JFrame f = new JFrame("DomiNation");

    //other global objects
    private static TopClass tc = new TopClass();

    /**
     * Default constructor
     */
    public TopClass() {
    }

    /**
     * Main executable method invoked when running .jar file
     * @param args
     */

    public static void main(String[] args) {
        //build the GUI on a new thread
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                tc.buildFrame();

                //create a new thread to keep the GUI responsive while the game runs
                Thread t = new Thread() {
                    public void run() {
                        //in here we will call a function to start the game
                    }
                };
                t.start();
            }
        });
    }

    /**
     * Method to construct the JFrame and add the program content
     */
    void buildFrame() {
        Image icon = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("blue_bird.png"));
        //f.setContentPane(createContentPane()); //adds the main content to the frame
        f.setResizable(true); //true, but game will not function properly unless maximized!
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setAlwaysOnTop(false);
        f.setVisible(true);
        //f.setMinimumSize(new Dimension(SCREEN_WIDTH/4, SCREEN_HEIGHT/4)); //set to prevent collapse to tiny window upon resizing
        f.setExtendedState(JFrame.MAXIMIZED_BOTH); //maximize the JFrame
        //f.setIconImage(icon); //set the icon

    }

    private JPanel createContentPane() {
        return new JPanel(); //return a blank panel
    }
}