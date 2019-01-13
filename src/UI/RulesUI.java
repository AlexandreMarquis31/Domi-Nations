package UI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

class RulesUI extends JPanel implements ActionListener {
    private final JTextPane ruleDetails;
    final JRadioButton duelSwitch;
    final JRadioButton harmonySwitch;
    final JRadioButton middleEarthSwitch;
    final JRadioButton dynastySwitch;

    RulesUI(int width, int height) {
        setLayout(null);
        JLabel rulesLabel = new JLabel("Règles spéciales");
        rulesLabel.setFont(new Font("New Time Roman", Font.BOLD, height / 10));
        rulesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rulesLabel.setBounds(0, 0, width, height / 8);
        add(rulesLabel);
        dynastySwitch = new JRadioButton("Dynastie");
        dynastySwitch.setBounds(width / 32, height / 3, width / 4, height / 8);
        dynastySwitch.addActionListener(this);
        add(dynastySwitch);
        harmonySwitch = new JRadioButton("Harmonie");
        harmonySwitch.setBounds(width / 4 + width / 32, height / 3, width / 4, height / 8);
        harmonySwitch.addActionListener(this);
        add(harmonySwitch);
        middleEarthSwitch = new JRadioButton("Empire du Milieu");
        middleEarthSwitch.setBounds(width / 32, height / 2, width / 4, height / 8);
        middleEarthSwitch.addActionListener(this);
        add(middleEarthSwitch);
        duelSwitch = new JRadioButton("Duel");
        duelSwitch.setBounds(width / 4 + width / 32, height / 2, width / 4, height / 8);
        duelSwitch.addActionListener(this);
        add(duelSwitch);
        ruleDetails = new JTextPane();
        Border margin = new EmptyBorder(height / 32, width / 20, height / 16, width / 20);
        ruleDetails.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.gray), margin));
        ruleDetails.setBounds(width / 2 + width / 16, (int) (height / 5.4), 3 * width / 8, (int) (height / 1.6));
        add(ruleDetails);
        StyledDocument doc = ruleDetails.getStyledDocument();
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                removeComponentListener(this);
                rulesLabel.setFont(new Font("New Time Roman", Font.BOLD, getHeight() / 10));
                SimpleAttributeSet center = new SimpleAttributeSet();
                StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                StyleConstants.setFontSize(center, getHeight() / 14);
                SimpleAttributeSet redacted = new SimpleAttributeSet();
                doc.setParagraphAttributes(0, doc.getLength(), center, true);
                rulesLabel.setHorizontalAlignment(SwingConstants.CENTER);
                rulesLabel.setBounds(0, 0, getWidth(), getHeight() / 8);
                dynastySwitch.setBounds(getWidth() / 32, getHeight() / 3, getWidth() / 4, getHeight() / 8);
                harmonySwitch.setBounds(getWidth() / 4 + getWidth() / 32, getHeight() / 3, getWidth() / 4, getHeight() / 8);
                middleEarthSwitch.setBounds(getWidth() / 32, getHeight() / 2, getWidth() / 4, getHeight() / 8);
                duelSwitch.setBounds(getWidth() / 4 + getWidth() / 32, getHeight() / 2, getWidth() / 4, getHeight() / 8);
                Border margin = new EmptyBorder(getHeight() / 32, getWidth() / 32, getHeight() / 16, getWidth() / 32);
                ruleDetails.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.gray), margin));
                ruleDetails.setBounds(getWidth() / 2 + getWidth() / 16, (int) (getHeight() / 5.4), 3 * getWidth() / 8, (int) (getHeight() / 1.6));
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

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = ((JRadioButton) e.getSource()).getActionCommand();
        switch (((JRadioButton) e.getSource()).getActionCommand()) {
            case "Duel":
                str += "\n\nLe royaume peut avoir une taille maximale de 7 × 7 cases. Il faudra alors utiliser tous les dominos pour construire de tels royaumes.";
                break;
            case "Harmonie":
                str += "\n\nAjouter 5 points de bonus si le royaume est complet.\n";
                break;
            case "Empire du Milieu":
                str += "\n\nAjouter 10 points de bonus si le chateau se retrouve au centre du royaume.";
                break;
            case "Dynastie":
                str += "\n\nJouer 3 tours de suite. A la fin des trois tours, le joueur qui a le plus de points remporte la partie.";
                break;
        }
        ruleDetails.setText(str);


    }
}
