package UI;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// a JFrame that will contains the game and the menu
public class Application extends JFrame implements ActionListener {
    public int turns = 1;
    private JPanel gM;
    private static Application instance = null;
    public MenuUI menuUI;
    private Timer resisingTimer;

    public Application(String str) {
        super(str);
        instance = this;
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAlwaysOnTop(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = (int) screenSize.getHeight();
        int height = screenHeight - 100;
        int screenWidth = (int) screenSize.getWidth();
        int width = height * 8 / 7;
        Timer t = new Timer(30, this);
        t.start();
        setBounds(screenWidth / 2 - width / 2, screenHeight / 2 - height / 2, width, height + getInsets().top);
        setVisible(true);
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
