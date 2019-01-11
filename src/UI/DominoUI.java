package UI;

import game.Domino;
import game.GameManager;
import game.Player;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;


public class DominoUI extends JPanel implements KeyListener {
    public Domino domino;
    private DominoPartUI do1;
    private DominoPartUI do2;
    int originalX = 0;
    int originalY = 0;
    private GameManager game;
    private JPanel selectedMark = new JPanel();

    DominoUI(Domino d, int x, int y, GraphicsManager gManager, GameManager g) {
        addKeyListener(this);
        domino = d;
        game = g;
        setSize(2 * gManager.sizePart + 4, gManager.sizePart + 2);
        setLayout(null);
        setLocation(x, y);
        setBackground(Color.orange);
        do1 = new DominoPartUI(domino.part1);
        do1.setBounds(1, 1, gManager.sizePart, gManager.sizePart);
        add(do1);
        do2 = new DominoPartUI(domino.part2);
        do2.setBounds(gManager.sizePart + 3, 1, gManager.sizePart, gManager.sizePart);
        add(do2);
        selectedMark.setBounds(getWidth() / 2 - gManager.sizePart / 3, getHeight() / 2 - gManager.sizePart / 3, gManager.sizePart * 2 / 3, gManager.sizePart * 2 / 3);
        selectedMark.setBorder(BorderFactory.createLineBorder(Color.white));
        selectedMark.setVisible(false);
        add(selectedMark);
        this.setComponentZOrder(selectedMark, 0);
        this.setComponentZOrder(do2, 1);
        this.setComponentZOrder(do1, 1);

        MouseInputAdapter mi = new MouseInputAdapter() {
            int clickX = 0;
            int clickY = 0;

            @Override
            //used to select allow Player to select Domino
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                e.getComponent().setFocusable(true);
                e.getComponent().requestFocusInWindow();
                this.clickX = e.getX();
                this.clickY = e.getY();
                if (((DominoUI) e.getComponent()).domino.player == null && game.currentPlayer.currentState == Player.state.IDLE) {
                    ((DominoUI) e.getComponent()).domino.player = game.currentPlayer;
                    game.currentDomino = ((DominoUI) e.getComponent()).domino;
                    synchronized (GameManager.lock) {
                        game.currentPlayer.currentState = Player.state.DOMINOSELECTED;
                        //notify the game manager that the Domino was successfully selected
                        GameManager.lock.notify();
                    }
                }
            }

            @Override
            //used when the Player want to place a Domino
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                e.getComponent().setFocusable(false);
                int x = e.getX() + e.getComponent().getX();
                int y = e.getY() + e.getComponent().getY();
                for (Component comp : gManager.getComponents()) {
                    // used when the Player want to put the Domino in the trash can
                    if (comp instanceof TrashUI) {
                        if (x < comp.getX() + comp.getWidth() && y < comp.getY() + comp.getHeight() && x > comp.getX() && y > comp.getY()) {
                            synchronized (GameManager.lock) {
                                game.currentPlayer.currentState = Player.state.IDLE;
                                game.currentPlayer.litter = true;
                                GameManager.lock.notify();
                            }
                            gManager.remove(e.getComponent());
                        }
                    }
                    if (comp instanceof PlayerUI) {
                        //verify that the Player is placing the Domino on the correct board
                        if (x < comp.getX() + comp.getWidth() && y < comp.getY() + comp.getHeight() && x > comp.getX() && y > comp.getY() && ((PlayerUI) comp).player == game.currentPlayer) {
                            Rectangle rec1 = new Rectangle(((DominoUI) e.getComponent()).do1.getX() + e.getComponent().getX(), ((DominoUI) e.getComponent()).do1.getY() + e.getComponent().getY(), gManager.sizePart, gManager.sizePart);
                            Rectangle rec2 = new Rectangle(((DominoUI) e.getComponent()).do2.getX() + e.getComponent().getX(), ((DominoUI) e.getComponent()).do2.getY() + e.getComponent().getY(), gManager.sizePart, gManager.sizePart);
                            int place = 0;
                            int x1 = 1000;
                            int x2 = 1000;
                            int y1 = 1000;
                            int y2 = 1000;
                            float air = ((DominoUI) e.getComponent()).do1.getWidth() * ((DominoUI) e.getComponent()).do1.getHeight();
                            // get the x and y values of the dominos part representation on the board
                            for (int i = 0; i < ((PlayerUI) comp).player.board.length; i++) {
                                for (int k = 0; k < ((PlayerUI) comp).player.board[i].length; k++) {
                                    Rectangle recTest = new Rectangle(((PlayerUI) comp).boardUI[k][i].getX() + comp.getX(), ((PlayerUI) comp).boardUI[k][i].getY() + comp.getY(), gManager.sizePart, gManager.sizePart);
                                    if (recTest.intersection(rec1).getHeight() * recTest.intersection(rec1).getWidth() > 0.5 * air && recTest.intersection(rec1).getWidth() > 0 && recTest.intersection(rec1).getHeight() > 0) {
                                        y1 = k;
                                        x1 = i;
                                    }
                                    if (recTest.intersection(rec2).getHeight() * recTest.intersection(rec2).getWidth() > 0.5 * air && recTest.intersection(rec2).getWidth() > 0 && recTest.intersection(rec2).getHeight() > 0) {
                                        y2 = k;
                                        x2 = i;
                                    }
                                }
                            }
                            //place the Domino if it can be placed
                            if ((((DominoUI) e.getComponent()).domino.canBePlaced(x1, y1, x2, y2, game.currentPlayer.board))) {
                                game.currentPlayer.board[y1][x1] = ((DominoUI) e.getComponent()).do1.dominoPart;
                                game.currentPlayer.board[y2][x2] = ((DominoUI) e.getComponent()).do2.dominoPart;
                                synchronized (GameManager.lock) {
                                    game.currentPlayer.currentState = Player.state.IDLE;
                                    //notify the game manager that the Domino has been placed
                                    GameManager.lock.notify();
                                }
                                gManager.remove(e.getComponent());
                            }
                        }
                    }
                }
                // move the Domino back to its original place if it cant be placed
                DominoUI d = (DominoUI) e.getComponent();
                d.setLocation(d.originalX, d.originalY);
            }

            @Override
            //move the Domino
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (game.currentPlayer.currentState == Player.state.PLACINGDOMINO && game.currentPlayer == domino.player && game.currentDomino == domino) {
                    int x = e.getX() + e.getComponent().getX() - this.clickX;
                    int y = e.getY() + e.getComponent().getY() - this.clickY;
                    e.getComponent().setLocation(x, y);
                    e.getComponent().repaint();
                }
            }
        };
        this.addMouseMotionListener(mi);
        this.addMouseListener(mi);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (domino.player != null) {
            selectedMark.setVisible(true);
            selectedMark.setBackground(domino.player.color);
        }
    }

    private void rotationRight() {
        if (getHeight() > getWidth()) {
            if (do2.getY() == 1) {
                do2.setLocation(do1.getWidth() + 3, 1);
                do1.setLocation(1, 1);
            } else if (do2.getY() == do1.getWidth() + 3) {
                do1.setLocation(do2.getWidth() + 3, 1);
                do2.setLocation(1, 1);
            }
        } else {
            if (do2.getX() == 1) {
                do1.setLocation(1, do2.getWidth() + 3);
            } else if (do2.getX() == do1.getWidth() + 3) {
                do2.setLocation(1, do1.getWidth() + 3);
            }
        }
        selectedMark.setLocation(selectedMark.getY(), selectedMark.getX());
        setSize(new Dimension(getHeight(), getWidth()));
    }

    private void rotationLeft() {
        if (getHeight() > getWidth()) {
            if (do2.getY() == 1) {
                do1.setLocation(do2.getWidth() + 3, 1);
            } else {
                do2.setLocation(do1.getWidth() + 3, 1);
                do1.setLocation(1, 1);
            }
        } else {
            if (do2.getX() == 1) {
                do2.setLocation(1, do1.getWidth() + 3);
                do1.setLocation(1, 1);
            } else {
                do2.setLocation(1, 1);
                do1.setLocation(1, do2.getWidth() + 3);
            }
        }
        selectedMark.setLocation(selectedMark.getY(), selectedMark.getX());
        setSize(new Dimension(getHeight(), getWidth()));
    }

    //used to rotate the Domino
    public void keyPressed(KeyEvent e) {
        if (domino.player == game.currentPlayer && domino.player.currentState == Player.state.PLACINGDOMINO && domino == game.currentDomino) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
                this.rotationRight();
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_Q) {
                this.rotationLeft();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}

