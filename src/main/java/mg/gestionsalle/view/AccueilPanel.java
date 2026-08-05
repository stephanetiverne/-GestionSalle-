package mg.gestionsalle.view;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AccueilPanel extends JPanel {

    private final Color COLOR_BG = new Color(240, 244, 249);
    private final Color COLOR_BANNER = new Color(227, 237, 253);
    private final Color COLOR_TITLE_BLUE = new Color(11, 87, 156);
    private final Color COLOR_PROF = new Color(46, 172, 108);
    private final Color COLOR_SALLE = new Color(41, 121, 255);
    private final Color COLOR_OCCUP = new Color(230, 145, 56);

    public AccueilPanel(MainFrame mainFrame) {
        setBackground(COLOR_BG);
        setLayout(new BorderLayout(0, 30));
        setBorder(new EmptyBorder(30, 50, 30, 50));

        // --- BANNIÈRE DE BIENVENUE ---
        JPanel welcomeBanner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_BANNER);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        welcomeBanner.setOpaque(false);
        welcomeBanner.setLayout(new BoxLayout(welcomeBanner, BoxLayout.Y_AXIS));
        welcomeBanner.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel lblWelcome = new JLabel("Bienvenue dans l'application");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblWelcome.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblAppName = new JLabel("Gestion des Salles de Classe");
        lblAppName.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblAppName.setForeground(COLOR_TITLE_BLUE);
        lblAppName.setAlignmentX(CENTER_ALIGNMENT);

        // Séparateur avec icône de maison/bâtiment
        JPanel separatorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        separatorPanel.setOpaque(false);
        JSeparator s1 = new JSeparator(); s1.setPreferredSize(new Dimension(150, 2));
        JSeparator s2 = new JSeparator(); s2.setPreferredSize(new Dimension(150, 2));
        
        // Icône centrale redimensionnée à 40x40
        JLabel midIcon = new JLabel(safeLoadIcon("/icons/home.png", 40, 40)); 
        separatorPanel.add(s1); separatorPanel.add(midIcon); separatorPanel.add(s2);

        JLabel lblDesc = new JLabel("<html><center>Cette application permet de gérer les professeurs, les salles<br>et les occupations des salles de classe.</center></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblDesc.setForeground(new Color(80, 90, 100));
        lblDesc.setHorizontalAlignment(SwingConstants.CENTER);
        lblDesc.setAlignmentX(CENTER_ALIGNMENT);

        welcomeBanner.add(lblWelcome);
        welcomeBanner.add(lblAppName);
        welcomeBanner.add(separatorPanel);
        welcomeBanner.add(Box.createVerticalStrut(10));
        welcomeBanner.add(lblDesc);
        add(welcomeBanner, BorderLayout.NORTH);

        // --- SECTION DES 3 CARTES ---
        JPanel cardsContainer = new JPanel(new GridLayout(1, 3, 30, 0));
        cardsContainer.setOpaque(false);

        cardsContainer.add(createModernCard(
            "PROFESSEURS", 
            "Ajouter, modifier, supprimer et rechercher un professeur.", 
            "/icons/profil.png", COLOR_PROF, mainFrame, MainFrame.CARD_PROF));

        cardsContainer.add(createModernCard(
            "SALLES", 
            "Ajouter, modifier, supprimer et rechercher une salle.", 
            "/icons/classroom.png", COLOR_SALLE, mainFrame, MainFrame.CARD_SALLE));

        cardsContainer.add(createModernCard(
            "OCCUPATIONS", 
            "Affecter une salle à un professeur et gérer les occupations.", 
            "/icons/calendar.png", COLOR_OCCUP, mainFrame, MainFrame.CARD_OCCUPATION));

        add(cardsContainer, BorderLayout.CENTER);
    }

    private JPanel createModernCard(String title, String description, String iconPath, Color accentColor, MainFrame mainFrame, String cardName) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(30, 25, 25, 25));

        // Panel Central pour l'icône, le titre et la description
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // -- CRÉATION DU CERCLE COLORÉ POUR L'ICÔNE --
        JPanel iconCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accentColor);
                g2.fill(new Ellipse2D.Float(0, 0, getWidth(), getHeight()));
                g2.dispose();
            }
        };
        iconCircle.setOpaque(false);
        iconCircle.setPreferredSize(new Dimension(100, 100));
        iconCircle.setMaximumSize(new Dimension(100, 100));
        iconCircle.setLayout(new GridBagLayout());
        
        // Icône à l'intérieur du cercle (en blanc si possible, sinon telle quelle)
        JLabel lblIcon = new JLabel(safeLoadIcon(iconPath, 50, 50));
        iconCircle.add(lblIcon);
        iconCircle.setAlignmentX(CENTER_ALIGNMENT);

        // Titre
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(accentColor);
        lblTitle.setAlignmentX(CENTER_ALIGNMENT);
        lblTitle.setBorder(new EmptyBorder(20, 0, 10, 0));

        // Description
        JLabel lblDesc = new JLabel("<html><center>" + description + "</center></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDesc.setForeground(new Color(110, 120, 130));
        lblDesc.setAlignmentX(CENTER_ALIGNMENT);

        content.add(iconCircle);
        content.add(lblTitle);
        content.add(lblDesc);
        card.add(content, BorderLayout.CENTER);

        // Bouton d'action en bas
        JButton btn = new JButton("GÉRER LES " + title + "  >");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(accentColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 45));
        btn.addActionListener(e -> mainFrame.showCard(cardName));
        
        card.add(btn, BorderLayout.SOUTH);

        return card;
    }

    /**
     * Charge une icône et la redimensionne proprement
     */
    private ImageIcon safeLoadIcon(String path, int width, int height) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(newImg);
        }
        return null;
    }
}