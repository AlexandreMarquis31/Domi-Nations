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
import java.nio.charset.Charset;

class RulesUI extends JPanel implements ActionListener {
    private final JTextPane ruleDetails;
    final JRadioButton duelSwitch;
    final JRadioButton harmonySwitch;
    final JRadioButton middleEarthSwitch;
    final JRadioButton dynastySwitch;

    RulesUI(int width, int height) {
        setSize(width, height);
        setLayout(null);
        JLabel rulesLabel = new JLabel("Règles spéciales");
        rulesLabel.setFont(new Font("New Time Roman", Font.BOLD, 16));
        rulesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rulesLabel.setBounds(0, 0, width, 20);
        add(rulesLabel);
        dynastySwitch = new JRadioButton("Dynastie");
        dynastySwitch.setBounds(width / 32, 50, width / 4, 20);
        dynastySwitch.addActionListener(this);
        add(dynastySwitch);
        harmonySwitch = new JRadioButton("Harmonie");
        harmonySwitch.setBounds(width / 4 + width / 32, 50, width / 4, 20);
        harmonySwitch.addActionListener(this);
        add(harmonySwitch);
        middleEarthSwitch = new JRadioButton("Empire du Milieu");
        middleEarthSwitch.setBounds(width / 32, 90, width / 4, 20);
        middleEarthSwitch.addActionListener(this);
        add(middleEarthSwitch);
        duelSwitch = new JRadioButton("Duel");
        duelSwitch.setBounds(width / 4 + width / 32, 90, width / 4, 20);
        duelSwitch.addActionListener(this);
        add(duelSwitch);
        ruleDetails = new JTextPane();
        Border margin = new EmptyBorder(5, 10, 10, 10);
        ruleDetails.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.gray), margin));
        ruleDetails.setBounds(width / 2 + width / 16, 30, 3 * width / 8, 100);
        add(ruleDetails);
        StyledDocument doc = ruleDetails.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        SimpleAttributeSet redacted = new SimpleAttributeSet();
        doc.setParagraphAttributes(0, 1, center, true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = ((JRadioButton) e.getSource()).getActionCommand() ;
        switch (((JRadioButton) e.getSource()).getActionCommand()) {
            case "Duel":
                str += "\n\nLe royaume peut désormais faire une taille maximale de 7 × 7 cases. Il faudra alors utiliser la totalités des dominos pour construire de tels royaumes.";
                break;
            case "Harmonie":
                str += "\n\nAjouter 5 points de bonus si le royaume est complet (c’est-à- dire, fait exactement 5 × 5 cases).\n";
                break;
            case "Empire du Milieu":
                str += "\n\nAjouter 10 points de bonus si le château se retrouve au centre du royaume.";
                break;
            case "Dynastie":
                str += "\n\nJouer 3 turns de suite. À la fin des trois turns, le joueur qui a cumulé le plus de points lors des 3 turns remporte la partie.";
                break;
        }
        String strEncoded = new String(str.getBytes(), Charset.forName("UTF-8"));
        ruleDetails.setText(strEncoded);


    }
}
