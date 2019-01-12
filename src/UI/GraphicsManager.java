package UI;

import game.Domino;
import game.GameManager;
import game.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class GraphicsManager extends JPanel implements ActionListener {
    private final JLabel currentPlayerLabel = new JLabel("         ");
    public final JLabel labelIndications = new JLabel("   Choisissez un Domino.   ");
    public static int sizePart = 30;
    static int widthBorder = 2;

    GraphicsManager(int width, int height) {
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        ActionListener actionListener = actionEvent -> System.exit(0);
        registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        setLayout(null);
        setBackground(new Color(100, 150, 255));
        JPanel infosUI = new JPanel();
        infosUI.setBackground(Color.orange);
        JLabel label = new JLabel("     Au tour de :     ");
        label.setForeground(Color.black);
        infosUI.add(label);
        infosUI.add(currentPlayerLabel);
        add(infosUI);
        JPanel nameUI = new JPanel();
        nameUI.setBackground(Color.orange);
        labelIndications.setForeground(Color.black);
        nameUI.add(labelIndications);
        add(nameUI);
        TrashUI trash = new TrashUI();
        add(trash);
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                removeComponentListener(this);
                int k = 0;
                for (Component comp : getComponents()) {
                    if (comp instanceof PlayerUI) {
                        if (((PlayerUI) comp).player.board.size == 13) {
                            sizePart = Application.getInstance().getWidth() / 40;
                        } else {
                            sizePart = (int) (Application.getInstance().getWidth() / 26.5);
                        }
                        ((PlayerUI) comp).sizePart = sizePart;
                        ((PlayerUI) comp).widthBorder = widthBorder;
                        switch (k) {
                            case (0):
                                comp.setBounds(0, 0, comp.getWidth() - 1, comp.getHeight());
                                break;
                            case (1):
                                comp.setBounds(getWidth() - comp.getWidth(), 0, comp.getWidth() - 1, comp.getHeight());
                                break;
                            case (2):
                                comp.setBounds(0, getHeight() - comp.getHeight(), comp.getWidth() - 1, comp.getHeight());
                                break;
                            case (3):
                                comp.setBounds(getWidth() - comp.getWidth(), getHeight() - comp.getHeight() - 1, comp.getWidth(), comp.getHeight());
                                break;
                        }
                        k++;
                    }
                }
                label.setFont(new Font("New Time Roman", Font.PLAIN, getHeight() / 50));
                infosUI.setBounds(getWidth() / 2 - getWidth() / 14, 0, getWidth() / 7, getHeight() / 14);
                labelIndications.setFont(new Font("New Time Roman", Font.PLAIN, getHeight() / 60));
                currentPlayerLabel.setFont(new Font("New Time Roman", Font.PLAIN, getHeight() / 50));
                nameUI.setBounds(getWidth() / 2 - getWidth() / 10, getWidth() / 10, getWidth() / 5, getWidth() / 25);
                trash.setBounds(getWidth() / 2 - getWidth() / 14, getHeight() - getHeight() / 16, getWidth() / 7, getHeight() / 16);
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
        setSize(width, height);
    }

    public void setPlayersUI(ArrayList<Player> list) {
        PlayerUI playerUI =null ;
        for (Player player : list) {
            switch (list.indexOf(player)) {
                case 0:
                    playerUI = new PlayerUI(list.get(0), sizePart, widthBorder);
                    break;
                case 1:
                    playerUI = new PlayerUI(list.get(1), sizePart, widthBorder);
                    break;
                case 2:
                    playerUI = new PlayerUI(list.get(2), sizePart, widthBorder);
                    break;
                case 3:
                    playerUI = new PlayerUI(list.get(3), sizePart, widthBorder);
                    break;
            }
            add(playerUI);
        }
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                removeComponentListener(this);
                int k = 0;
                for (Component comp : getComponents()){
                    if(comp instanceof PlayerUI){
                        if (((PlayerUI)comp).player.board.size == 13) {
                            sizePart = Application.getInstance().getWidth() / 40;
                        } else {
                            sizePart = (int) (Application.getInstance().getWidth() / 26.5);
                        }
                        ((PlayerUI)comp).sizePart = sizePart;
                        ((PlayerUI)comp).widthBorder = widthBorder;
                        switch (k) {
                            case (0):
                                comp.setBounds(0, 0, comp.getWidth() - 1, comp.getHeight());
                                break;
                            case (1):
                                comp.setBounds(getWidth() - comp.getWidth(), 0, comp.getWidth() - 1, comp.getHeight());
                                break;
                            case (2):
                                comp.setBounds(0, getHeight() - comp.getHeight(), comp.getWidth() - 1, comp.getHeight());
                                break;
                            case (3):
                                comp.setBounds(getWidth() - comp.getWidth(), getHeight() - comp.getHeight() - 1, comp.getWidth(), comp.getHeight());
                                break;
                        }
                        k++;
                    }
                }
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
        setSize(getWidth()+8,getHeight()+7);
        setSize(getWidth()-8,getHeight()-7);
    }

        public void newLineDominos (GameManager game, List < Domino > list,int n){
            for (Domino domino : list) {
                int x = getWidth() / 2 - (2 * sizePart + 2 * widthBorder) - getWidth() / 80 + n * (2 * sizePart + 2 * widthBorder + getWidth() / 80);
                int y = getHeight() / 5 + list.indexOf(domino) * (getHeight() / 20 + (2 * (sizePart + widthBorder)));
                DominoUI dominoUI = new DominoUI(domino, x, y, this, game);
                dominoUI.place = list.indexOf(domino);
                dominoUI.originalX = x;
                dominoUI.originalY = y;
                add(dominoUI);
                setComponentZOrder(dominoUI, 0);
                addComponentListener(new ComponentListener() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        removeComponentListener(this);
                        int x = getWidth() / 2 - (2 * sizePart + 2 * widthBorder) - getWidth() / 80 + n * (2 * sizePart + 2 * widthBorder + getWidth() / 80);
                        int y = getHeight() / 5 + dominoUI.place * (getHeight() / 20 + (2 * (sizePart + widthBorder)));
                        dominoUI.setLocation(x, y);
                        //
                        if (dominoUI.getHeight() > dominoUI.getWidth()){
                            dominoUI.setSize( sizePart + widthBorder,2 * sizePart + widthBorder * 2);
                        } else {
                            dominoUI.setSize(2 * sizePart + widthBorder * 2, sizePart + widthBorder);
                        }
                        dominoUI.originalX = x;
                        dominoUI.originalY = y;
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
            }
        }

        public void setCurrentPlayer (Player player){
            currentPlayerLabel.setText(player.name);
            currentPlayerLabel.setForeground(player.color);
        }

        public void showScores (ArrayList < Player > list) {
            JPanel scorePanel = new JPanel(new GridBagLayout());
            scorePanel.setBackground(Color.orange);
            scorePanel.setBounds(getWidth() / 2 - getWidth() / 8, getHeight() / 2 - getWidth() / 8, getWidth() / 4, getHeight() / 3);
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            JLabel score = new JLabel("Scores");
            score.setFont(new Font("Time New Roman", Font.PLAIN, getHeight() / 23));
            scorePanel.add(score, c);
            int i = 0;
            for (Player player : list) {
                JLabel label = new JLabel(player.name);
                label.setFont(new Font("Time New Roman", Font.PLAIN, getHeight() / 35));
                GridBagConstraints c1 = new GridBagConstraints();
                c1.gridx = 0;
                scorePanel.add(label, c1);
                JLabel s = new JLabel(String.valueOf(player.totalScore));
                s.setFont(new Font("Time New Roman", Font.PLAIN, getHeight() / 35));
                GridBagConstraints c2 = new GridBagConstraints();
                c2.gridx = 1;
                scorePanel.add(s, c2);
                i++;
            }
            String butLabel = "Rejouer";
            if (Application.getInstance().turns > 1) {
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
        public void actionPerformed (ActionEvent e){
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
                        for (Player player : GameManager.listPlayers) {
                            player.newBoard(player.board.size);
                        }
                    } else {
                        ArrayList<Player> newListPlayers = new ArrayList<>();
                        for (Player player : GameManager.listPlayers) {
                            newListPlayers.add(new Player(player.name, player.color));
                        }
                        GameManager.listPlayers = newListPlayers;
                    }
                    GameManager.specialRules.remove(GameManager.Rule.DYNASTY);
                    game.newGame(GameManager.listPlayers, GameManager.specialRules);
                });
                thread.start();
            }
        }
    }
