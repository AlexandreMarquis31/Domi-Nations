package UI;

import game.GameManager;
import game.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

class ScoresUI extends JPanel implements ActionListener {
    ScoresUI(ArrayList<Player> list) {
        setLayout(null);
        setBackground(Color.orange);
        JLabel score = new JLabel("Scores");
        score.setHorizontalAlignment(SwingConstants.CENTER);
        add(score);
        for (Player player : list) {
            JLabel label = new JLabel(player.name);
            label.setSize(1000, getHeight() / 12);
            add(label);
            JLabel s = new JLabel(String.valueOf(player.totalScore));
            s.setHorizontalAlignment(SwingConstants.CENTER);
            s.setSize(10, getHeight() / 12);
            add(s);
        }
        String butLabel = "Rejouer";
        if (Application.getInstance().turns > 1) {
            butLabel = "Prochaine Manche";
        }
        JButton button = new JButton(butLabel);
        button.addActionListener(this);
        add(button);
        JButton buttonMenu = new JButton("Menu Principal");
        buttonMenu.addActionListener(this);
        add(buttonMenu);
        validate();
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                removeComponentListener(this);
                score.setBounds(0, 0, getWidth(), getHeight() / 5);
                score.setFont(new Font("New Time Roman", Font.PLAIN, getHeight() / 7));
                int i = 0;
                for (Component comp : getComponents()) {
                    if (comp instanceof JLabel && comp != score) {
                        if (comp.getWidth() > getWidth() / 2) {
                            comp.setBounds(10, getHeight() / 7 + 10 + getHeight() * i * 3 / 20, getWidth() * 3 / 4 - 10, getHeight() / 12);
                        } else {
                            comp.setBounds(getWidth() - getWidth() / 4, getHeight() / 7 + 10 + getHeight() * i * 3 / 20, getWidth() / 4, getHeight() / 12);
                            i++;
                        }
                        //comp.setFont(new Font("New Time Roman", Font.PLAIN, getHeight() / 12));
                        autosizeLabel(((JLabel)comp));
                    }
                }
                button.setBounds(getWidth()/12,getHeight()*15/20,getWidth()*5/6,getHeight()/10);
                buttonMenu.setBounds(getWidth()/12,getHeight()*17/20,getWidth()*5/6,getHeight()/10);
                addComponentListener(this);
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
        setSize(Application.getInstance().getWidth() / 4, Application.getInstance().getHeight() / 3);
    }

    @Override
    //used when a button has been pressed on the scores pane
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Menu Principal")) {
            //go back to the menu
            MenuUI menu = new MenuUI(getWidth(), getHeight());
            Application.getInstance().setGM(menu);
        } else {
            Thread thread = new Thread(() -> {
                //create another game with the sames players
                GraphicsManager graphics = new GraphicsManager(getWidth(), getHeight());
                Application.getInstance().setGM(graphics);
                GameManager game = new GameManager(graphics);
                if (Application.getInstance().turns > 1) {
                    Application.getInstance().turns--;
                } else {
                    for (Player player : GameManager.listPlayers) {
                        player.totalScore = 0;
                    }
                }
                for (Player player : GameManager.listPlayers) {
                    player.newBoard(player.board.size);
                }
                GameManager.specialRules.remove(GameManager.Rule.DYNASTY);
                game.newGame(GameManager.listPlayers, GameManager.specialRules);
            });
            thread.start();
        }
    }

    private void autosizeLabel(JLabel label) {
        Font labelFont = label.getFont();
        String labelText = label.getText();

        int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
        int componentWidth = label.getWidth();

// Find out how much the font can grow in width.
        double widthRatio = (double) componentWidth / (double) stringWidth;

        int newFontSize = (int) (labelFont.getSize() * widthRatio);
        int componentHeight = label.getHeight();

// Pick a new font size so it will not be larger than the height of label.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);

// Set the label's font size to the newly determined size.
        label.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
    }
}
