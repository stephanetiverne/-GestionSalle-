package mg.gestionsalle.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AboutPanel extends JPanel {

    public AboutPanel() {
        setLayout(new BorderLayout(24, 24));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("À propos", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(11, 95, 134));

        JLabel content = new JLabel("<html><center>Gestion des Salles de Classe<br><br>Application desktop Java Swing avec gestion des professeurs, salles et occupations.<br><br>Version 1.0</center></html>", SwingConstants.CENTER);
        content.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        content.setForeground(new Color(76, 86, 106));

        add(title, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }
}
