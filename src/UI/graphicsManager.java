package UI;

import game.domino;
import game.gameManager;
import game.player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


public class graphicsManager extends JPanel implements ActionListener {
    private JLabel currentPlayerLabel = new JLabel("         ");
    public JLabel labelConsigne = new JLabel("   Choisissez un domino.   ");
    public static int sizePart = 30;

    graphicsManager(int width, int height) {
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        ActionListener actionListener = actionEvent -> System.exit(0);
        registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        setLayout(null);
        setBackground(Color.black);
        JPanel annonceUI = new JPanel();
        annonceUI.setBackground(Color.orange);
        JLabel label = new JLabel("     Au tour de :     ");
        label.setForeground(Color.white);
        currentPlayerLabel.setForeground(Color.white);
        annonceUI.setBounds(width / 2 - 58, 0, 116, 50);
        annonceUI.add(label);
        annonceUI.add(currentPlayerLabel);
        add(annonceUI);
        JPanel consigneUI = new JPanel();
        consigneUI.setBackground(Color.orange);
        labelConsigne.setForeground(Color.white);
        consigneUI.setBounds(width / 2 - 84, 70, 166, 28);
        consigneUI.add(labelConsigne);
        add(consigneUI);
        TrashUI trash = new TrashUI();
        trash.setBounds(width / 2 - 54, height - 50, 116, 50);
        add(trash);
    }

    public void setPlayersUI(ArrayList<player> list) {
        if (list.size() > 0) {
            PlayerUI playerUI = new PlayerUI(list.get(0), sizePart);
            playerUI.setBounds(0, 0, playerUI.getWidth(), playerUI.getHeight());
            add(playerUI);
        }
        if (list.size() > 1) {
            PlayerUI playerUI2 = new PlayerUI(list.get(1), sizePart);
            playerUI2.setBounds(getWidth() - playerUI2.getWidth(), 0, playerUI2.getWidth(), playerUI2.getHeight());
            add(playerUI2);
        }
        if (list.size() > 2) {
            PlayerUI playerUI3 = new PlayerUI(list.get(2), sizePart);
            playerUI3.setBounds(0, getHeight() - playerUI3.getHeight(), playerUI3.getWidth(), playerUI3.getHeight());
            add(playerUI3);
        }
        if (list.size() > 3) {
            PlayerUI playerUI4 = new PlayerUI(list.get(3), sizePart);
            playerUI4.setBounds(getWidth() - playerUI4.getWidth(), getHeight() - playerUI4.getHeight(), playerUI4.getWidth(), playerUI4.getHeight());
            add(playerUI4);
        }
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println(memory);
    }

    public void newLineDominos(gameManager game, List<domino> list, int n) {
        for (domino domino : list) {
            domino.turnPriority = list.indexOf(domino);
            int x = getWidth() / 2 - (2 * sizePart + 4) - 10 + n * (2 * sizePart + 4 + 10);
            int y = 120 + list.indexOf(domino) * (10 + 40 + (sizePart + 2));
            DominoUI dominoUI = new DominoUI(domino, x, y, this, game);
            dominoUI.originalX = x;
            dominoUI.originalY = y;
            add(dominoUI);
            setComponentZOrder(dominoUI, 0);
        }
    }

    public void setCurrentPlayer(player player) {
        currentPlayerLabel.setText(player.name);
        currentPlayerLabel.setForeground(player.color);
    }

    public void showScores(ArrayList<player> list) {
        JPanel scorePanel = new JPanel(new GridBagLayout());
        scorePanel.setBackground(Color.orange);
        scorePanel.setBounds(getWidth() / 2 - getWidth() / 8, getHeight() / 2 - getWidth() / 8, getWidth() / 4, getHeight() / 3);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        JLabel score = new JLabel("Scores");
        score.setFont(new Font("Time New Roman", Font.PLAIN, 30));
        scorePanel.add(score, c);
        int i = 0;
        for (player player : list) {
            JLabel label = new JLabel(player.name);
            label.setFont(new Font("Time New Roman", Font.PLAIN, 20));
            GridBagConstraints c1 = new GridBagConstraints();
            c1.gridx = 0;
            scorePanel.add(label, c1);
            JLabel s = new JLabel(String.valueOf(player.cumuledScore));
            s.setFont(new Font("Time New Roman", Font.PLAIN, 20));
            GridBagConstraints c2 = new GridBagConstraints();
            c2.gridx = 1;
            scorePanel.add(s, c2);
            i++;
        }
        String butLabel = "Rejouer";
        if (Application.getInstance().manches > 1) {
            butLabel = "Prochaine Manche";
        }
        JButton button = new JButton(butLabel);
        button.addActionListener(this);
        GridBagConstraints c1 = new GridBagConstraints();
        c1.gridx = 0;
        c1.gridwidth = 2;
        scorePanel.add(button, c1);
        JButton buttonMenu = new JButton("Menu Principal");
        buttonMenu.addActionListener(this);
        scorePanel.add(buttonMenu, c1);
        add(scorePanel);
        scorePanel.validate();
    }

    @Override
    //used when a button has been pressed on the scores pane
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Menu Principal")) {
            //go back to the menu
            MenuUI menu = new MenuUI(getWidth(), getHeight());
            Application.getInstance().setGM(menu);
        } else {
            Thread thread = new Thread() {
                public void run() {
                    //create another game with the sames players
                    graphicsManager graphics = new graphicsManager(getWidth(), getHeight());
                    Application.getInstance().setGM(graphics);
                    gameManager game = new gameManager(graphics);
                    if (Application.getInstance().manches > 1) {
                        Application.getInstance().manches--;
                        for (player player : gameManager.listPlayers) {
                            player.newBoard(player.size);
                        }
                    } else {
                        ArrayList<player> newListPlayers = new ArrayList<>();
                        for (player player : gameManager.listPlayers) {
                            newListPlayers.add(new player(player.name, player.color));
                        }
                        gameManager.listPlayers = newListPlayers;
                    }
                    gameManager.specialRules.remove(gameManager.Rule.DYNASTY);
                    game.newGame(gameManager.listPlayers, gameManager.specialRules);
                }
            };
            thread.start();
        }
    }
}
