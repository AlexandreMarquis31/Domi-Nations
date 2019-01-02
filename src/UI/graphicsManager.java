package UI;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.domino;
import player.player;


public class graphicsManager extends JPanel implements ActionListener {
    public int width = 800;
    public int height = 700;
    public ArrayList<player> listPlayers = new ArrayList<player>();
    public List<domino> selectableDominos = Arrays.asList(new domino[3]);
    public JFrame f = new JFrame("DomiNation");
    public JLabel currentPlayer = new JLabel("         ");

    public graphicsManager() {
        this.setLayout(null);
        this.setBackground(Color.black);
        JPanel annonceUI = new JPanel();
        annonceUI.setBackground(Color.orange);
        JLabel label = new JLabel("     Au tour de :     ", SwingConstants.CENTER);
        label.setForeground(Color.white);
        label.setVerticalAlignment(JLabel.TOP);
        currentPlayer.setForeground(Color.white);
        annonceUI.setBounds(width / 2 - 58, 0, 116, 50);
        annonceUI.add(label);
        annonceUI.add(currentPlayer);
        this.add(annonceUI);



    }
    public void startGraphics(){
        f.setContentPane(this); //adds the main content to the frame
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setAlwaysOnTop(false);
        f.setSize(new Dimension(width, height));
        f.setLayout(null);
        //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
        //f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setUndecorated(true);
        f.setVisible(true);
        Timer t = new Timer(30,this);
        t.start();
    }

    public void setListPlayers(ArrayList<player> listPlayers) {
        System.out.println(listPlayers);
        this.listPlayers = listPlayers;
        if (listPlayers.size() > 0) {
            PlayerUI pleyerUI = new PlayerUI(listPlayers.get(0));
            pleyerUI.setBounds(0, 0, pleyerUI.width, pleyerUI.height);
            this.add(pleyerUI);
        }
        if (listPlayers.size() > 1) {
            PlayerUI pleyerUI2 = new PlayerUI(listPlayers.get(1));
            pleyerUI2.setBounds(width - pleyerUI2.width, 0, pleyerUI2.width, pleyerUI2.height);
            this.add(pleyerUI2);
        }
        if (listPlayers.size() > 2) {
            PlayerUI pleyerUI3 = new PlayerUI(listPlayers.get(2));
            pleyerUI3.setBounds(0, height - pleyerUI3.height, pleyerUI3.width, pleyerUI3.height);
            this.add(pleyerUI3);
        }
        if (listPlayers.size() > 3) {
            PlayerUI pleyerUI4 = new PlayerUI(listPlayers.get(3));
            pleyerUI4.setBounds(width - pleyerUI4.width, height - pleyerUI4.height, pleyerUI4.width, pleyerUI4.height);
            this.add(pleyerUI4);
        }
    }

    public void setSelectableDominos(List<domino> list) {
        for (domino domino : list) {
            DominoUI dominoUI = new DominoUI(domino);
            dominoUI.setBounds(width / 2 - dominoUI.width - 2, (height - (list.size() * (10 + dominoUI.height))) / 2 + list.indexOf(domino) * (10 + dominoUI.height), dominoUI.width, dominoUI.height);
            this.add(dominoUI);
        }
    }
    public void render(Graphics g){
        /*BufferStrategy bs = f.getBufferStrategy();
        if(bs == null){
            f.createBufferStrategy(3);
        }
        g = bs.getDrawGraphics();

           //code here for draw

        g.dispose();
        bs.show();*/
    }
    public void update(Graphics g){

    }
    public void paintComponent(Graphics g) {
        update(g);
        render(g);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        repaint();
    }


}
