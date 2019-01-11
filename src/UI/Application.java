package UI;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// a JFrame that will countain the game and the menu
public class Application extends JFrame implements ActionListener {
    private int width = 800;
    private int height = 700;
    public int manches = 1;
    private JPanel gM;
    private static Application instance = null;
    public MenuUI menuUI;

    public Application(String str) {
        super(str);
        instance = this;
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAlwaysOnTop(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        setVisible(true);
        setSize(new Dimension((int)width, (int)height + getInsets().top));
        Timer t = new Timer(30, this);
        t.start();
    }

    //set the core panel
    public void setGM(JPanel g) {
        this.gM = g;
        this.setContentPane(g);
        repaint();
        revalidate();
    }

    //return the supposed unique application instance
    public static Application getInstance() {
        return instance;
    }

    @Override
    //repaint to update graphics
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
