package UI;

import game.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

class PlayerUI extends JPanel {
    int sizePart;
    int widthBorder;
    final Player player;
    final DominoPartUI[][] boardUI;
    int place;

    PlayerUI(Player p, int size,int border) {
        player = p;
        sizePart = size;
        widthBorder = border;
        boardUI = new DominoPartUI[p.board.size][p.board.size];
        setLayout(null);
        setBackground(Color.orange);
        for (int i = 0; i < p.board.size; i++) {
            for (int k = 0; k < p.board.size; k++) {
                boardUI[k][i] = new DominoPartUI(p.board.get(i, k));
                add(boardUI[k][i]);
            }
        }
        JLabel label = new JLabel(p.name);
        add(label);
        JPanel color = new JPanel();
        color.setBorder(BorderFactory.createLineBorder(Color.white));
        color.setBackground(p.color);
        add(color);
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                removeComponentListener(this);
                for (int i = 0; i < p.board.size; i++) {
                    for (int k = 0; k < p.board.size; k++) {
                        boardUI[k][i].setBounds((i * (sizePart + widthBorder)) + widthBorder, (k * (sizePart + widthBorder)) + widthBorder, sizePart, sizePart);
                    }
                }
                label.setBounds(widthBorder, getHeight() - Application.getInstance().getHeight()/26, getWidth(), Application.getInstance().getHeight()/35);
                color.setBounds(getWidth() - Application.getInstance().getHeight()/26, getHeight() - Application.getInstance().getHeight()/26, Application.getInstance().getHeight()/35, Application.getInstance().getHeight()/35);

                setSize(p.board.size * (sizePart + widthBorder) + widthBorder, p.board.size * (sizePart + widthBorder) + widthBorder + (int)(Application.getInstance().getHeight()/23.4));
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
        setSize(p.board.size * (size + border) + border, p.board.size * (size + border) + border + (int)(Application.getInstance().getHeight()/23.4));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < player.board.size; i++) {
            for (int k = 0; k < player.board.size; k++) {
                boardUI[k][i].dominoPart = player.board.get(i, k);
            }
        }
    }
}