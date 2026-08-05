package mg.gestionsalle.view;

import java.awt.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class MainFrame extends javax.swing.JFrame {
    
    public static final String CARD_ACCUEIL = "ACCUEIL";
    public static final String CARD_PROF = "PROF";
    public static final String CARD_SALLE = "SALLE";
    public static final String CARD_OCCUPATION = "OCCUPATION";
    public static final String CARD_APROPOS = "APROPOS";

    private static final Color COLOR_SIDEBAR = new Color(21, 37, 58);
    private static final Color COLOR_SELECTED = new Color(41, 121, 255);

    public MainFrame() {
        initComponents();
        setupStyles();
        initDateTimeUpdater();
        showCard(CARD_ACCUEIL);
        updateSelectedMenuPanel(pnlAccueil);
    }

    private void setupStyles() {
        setTitle("Gestion des Salles de Classe");
        logomenu.setLayout(new CardLayout());
        
        // Sécurité : Vérifier si les classes existent avant de les ajouter
        logomenu.add(new AccueilPanel(this), CARD_ACCUEIL);
        try {
            logomenu.add(new ProfPanel(), CARD_PROF);
            logomenu.add(new SallePanel(), CARD_SALLE);
            logomenu.add(new OccupationPanel(), CARD_OCCUPATION);
            logomenu.add(new AboutPanel(), CARD_APROPOS);
        } catch (Exception e) {
            System.err.println("Note: Certains panels ne sont pas encore créés.");
        }

        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        
        menu.setBackground(COLOR_SIDEBAR);
        menuPanel.setBackground(COLOR_SIDEBAR);
        logoPanel.setBackground(COLOR_SIDEBAR);
        bottomPanel.setBackground(COLOR_SIDEBAR);

        footer.setBackground(new Color(230, 235, 245));
        footer.setLayout(new BorderLayout());
        JLabel status = new JLabel("  Utilisateur : Administrateur | Version 1.0");
        status.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.add(status, BorderLayout.WEST);
    }

    private void initDateTimeUpdater() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss");
        Timer timer = new Timer(1000, e -> {
            jLabel3.setText(LocalDateTime.now().format(df));
        });
        timer.start();
    }

    public void showCard(String cardName) {
        CardLayout cl = (CardLayout) logomenu.getLayout();
        cl.show(logomenu, cardName);
    }

    private void updateSelectedMenuPanel(JPanel panel) {
        pnlAccueil.setBackground(COLOR_SIDEBAR);
        pnlProfesseurs.setBackground(COLOR_SIDEBAR);
        pnlSalles.setBackground(COLOR_SIDEBAR);
        pnlOccupations.setBackground(COLOR_SIDEBAR);
        pnlApropos.setBackground(COLOR_SIDEBAR);
        if (panel != null) panel.setBackground(COLOR_SELECTED);
    }

    private ImageIcon safeLoadIcon(String path) {
        URL url = getClass().getResource(path);
        return (url != null) ? new ImageIcon(url) : null;
    }

    // --- CETTE MÉTHODE CORRIGE LE PROBLÈME DE CLIC ET DE STYLE ---
    private void initComponents() {
        // Code généré simplifié pour assurer la stabilité
        header = new JPanel(new BorderLayout());
        headercenter = new JPanel();
        jLabel1 = new JLabel("MENU PRINCIPAL");
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headercenter.add(jLabel1);
        header.add(headercenter, BorderLayout.CENTER);
        
        jLabel3 = new JLabel(); // Date/Heure
        header.add(jLabel3, BorderLayout.EAST);

        menu = new JPanel(new BorderLayout());
        menu.setPreferredSize(new Dimension(250, 0));
        
        logoPanel = new JPanel();
        logoPanel.setPreferredSize(new Dimension(250, 150));
        jLabel6 = new JLabel("<html><center>GESTION DES SALLES</center></html>");
        jLabel6.setForeground(Color.WHITE);
        jLabel6.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoPanel.add(jLabel6);
        menu.add(logoPanel, BorderLayout.NORTH);

        menuPanel = new JPanel(new GridLayout(6, 1, 0, 5));
        pnlAccueil = createMenuBtn("Accueil", "/icons/home.png");
        pnlProfesseurs = createMenuBtn("Professeurs", "/icons/profil.png");
        pnlSalles = createMenuBtn("Salles", "/icons/classroom.png");
        pnlOccupations = createMenuBtn("Occupations", "/icons/calendar.png");
        pnlApropos = createMenuBtn("À propos", "/icons/info.png");

        pnlAccueil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { showCard(CARD_ACCUEIL); updateSelectedMenuPanel(pnlAccueil); }
        });
        pnlProfesseurs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { showCard(CARD_PROF); updateSelectedMenuPanel(pnlProfesseurs); }
        });
        pnlSalles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { showCard(CARD_SALLE); updateSelectedMenuPanel(pnlSalles); }
        });
        pnlOccupations.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { showCard(CARD_OCCUPATION); updateSelectedMenuPanel(pnlOccupations); }
        });
        pnlApropos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { showCard(CARD_APROPOS); updateSelectedMenuPanel(pnlApropos); }
        });

        menuPanel.add(pnlAccueil);
        menuPanel.add(pnlProfesseurs);
        menuPanel.add(pnlSalles);
        menuPanel.add(pnlOccupations);
        menuPanel.add(pnlApropos);
        menu.add(menuPanel, BorderLayout.CENTER);

        bottomPanel = new JPanel();
        btnQuitter = new JButton("Quitter");
        btnQuitter.addActionListener(e -> System.exit(0));
        bottomPanel.add(btnQuitter);
        menu.add(bottomPanel, BorderLayout.SOUTH);

        logomenu = new JPanel();
        footer = new JPanel();

        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(menu, BorderLayout.WEST);
        getContentPane().add(logomenu, BorderLayout.CENTER);
        getContentPane().add(footer, BorderLayout.SOUTH);

        setSize(1280, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel createMenuBtn(String text, String iconPath) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        p.setBackground(COLOR_SIDEBAR);
        JLabel lbl = new JLabel(text, safeLoadIcon(iconPath), SwingConstants.LEFT);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        p.add(lbl);
        p.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return p;
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }

    private JPanel bottomPanel, footer, header, headercenter, logoPanel, logomenu, menu, menuPanel, pnlAccueil, pnlApropos, pnlOccupations, pnlProfesseurs, pnlSalles;
    private JButton btnQuitter;
    private JLabel jLabel1, jLabel3, jLabel6;
}