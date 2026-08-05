package mg.gestionsalle.view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import mg.gestionsalle.dao.ProfDAO;
import mg.gestionsalle.entity.Prof;

public class ProfPanel extends JPanel {

    private final ProfDAO profDAO = new ProfDAO();
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField txtCode, txtNom, txtPrenom, txtGrade;
    
    // Couleurs du thème
    private final Color SECONDARY_COLOR = new Color(245, 247, 250);

    public ProfPanel() {
        setLayout(new BorderLayout(25, 0));
        setBackground(SECONDARY_COLOR);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- TITRE ---
        JLabel header = new JLabel("Gestion des Professeurs");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setForeground(new Color(11, 48, 92));
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        // --- FORMULAIRE (À GAUCHE) ---
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(350, 0));

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(25, 20, 25, 20)));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 5, 10, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;

        txtCode = createStyledField("Code Professeur");
        txtNom = createStyledField("Nom");
        txtPrenom = createStyledField("Prénom");
        txtGrade = createStyledField("Grade");

        // Colonne 1 : CODE et NOM
        JLabel lblCode = new JLabel("CODE");
        lblCode.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCode.setForeground(new Color(120, 130, 150));
        c.gridy = 0; c.gridx = 0; formCard.add(lblCode, c);
        c.gridy = 1; c.gridx = 0; formCard.add(txtCode, c);

        JLabel lblNom = new JLabel("NOM");
        lblNom.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNom.setForeground(new Color(120, 130, 150));
        c.gridy = 2; c.gridx = 0; formCard.add(lblNom, c);
        c.gridy = 3; c.gridx = 0; formCard.add(txtNom, c);

        // Colonne 2 : PRÉNOM et GRADE
        JLabel lblPrenom = new JLabel("PRÉNOM");
        lblPrenom.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPrenom.setForeground(new Color(120, 130, 150));
        c.gridy = 0; c.gridx = 1; formCard.add(lblPrenom, c);
        c.gridy = 1; c.gridx = 1; formCard.add(txtPrenom, c);

        JLabel lblGrade = new JLabel("GRADE");
        lblGrade.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblGrade.setForeground(new Color(120, 130, 150));
        c.gridy = 2; c.gridx = 1; formCard.add(lblGrade, c);
        c.gridy = 3; c.gridx = 1; formCard.add(txtGrade, c);

        // Boutons
        JPanel btnPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnAdd = createModernButton("Ajouter", new Color(46, 204, 113));
        JButton btnUpdate = createModernButton("Modifier", new Color(52, 152, 219));
        JButton btnDelete = createModernButton("Supprimer", new Color(231, 76, 60));
        JButton btnSearch = createModernButton("Rechercher", new Color(149, 165, 166));
        JButton btnRefresh = createModernButton("Actualiser", new Color(52, 73, 94));

        btnPanel.add(btnAdd); btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete); btnPanel.add(btnSearch);
        btnPanel.add(btnRefresh);

        c.gridy = 4; c.gridx = 0; c.gridwidth = 2; c.weightx = 1.0;
        formCard.add(btnPanel, c);
        leftPanel.add(formCard, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);

        // --- TABLEAU (AU CENTRE) ---
        tableModel = new DefaultTableModel(new Object[]{"CODE", "NOM", "PRÉNOM", "GRADE"}, 0);
        table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(new LineBorder(new Color(230, 230, 230)));
        add(scrollPane, BorderLayout.CENTER);

        // Actions
        btnAdd.addActionListener(e -> ajouterProfesseur());
        btnUpdate.addActionListener(e -> modifierProfesseur());
        btnDelete.addActionListener(e -> supprimerProfesseur());
        btnSearch.addActionListener(e -> rechercherProfesseur());
        btnRefresh.addActionListener(e -> chargerProfesseurs());

        table.getSelectionModel().addListSelectionListener(e -> fillFields());
        chargerProfesseurs();
    }

    private JTextField createStyledField(String hint) {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(0, 35));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(0, 10, 0, 10)));
        return f;
    }



    private JButton createModernButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(35);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.getTableHeader().setBackground(new Color(240, 240, 240));
        t.setSelectionBackground(new Color(232, 242, 255));
        t.setSelectionForeground(Color.BLACK);
        t.setGridColor(new Color(245, 245, 245));
    }

    private void fillFields() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            txtCode.setText(tableModel.getValueAt(r, 0).toString());
            txtNom.setText(tableModel.getValueAt(r, 1).toString());
            txtPrenom.setText(tableModel.getValueAt(r, 2).toString());
            txtGrade.setText(tableModel.getValueAt(r, 3).toString());
        }
    }

    // --- GARDER TA LOGIQUE DE DAO ICI ---
    private void ajouterProfesseur() {
        String code = txtCode.getText().trim();
        if (code.isBlank()) { showError("Code requis."); return; }
        // Vérifier si le code existe déjà
        for (Prof p : profDAO.afficherTous()) {
            if (p.getCodeprof().equalsIgnoreCase(code)) {
                JOptionPane.showMessageDialog(this, "Code professeur " + code + " existe déjà dans la base de données!", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        Prof prof = new Prof(code, txtNom.getText().trim(), txtPrenom.getText().trim(), txtGrade.getText().trim());
        profDAO.ajouter(prof);
        JOptionPane.showMessageDialog(this, "Professeur ajouté avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
        chargerProfesseurs();
        txtCode.setText(""); txtNom.setText(""); txtPrenom.setText(""); txtGrade.setText("");
    }

    private void modifierProfesseur() {
        String code = txtCode.getText().trim();
        if (code.isBlank()) { showError("Code requis."); return; }
        profDAO.modifier(new Prof(code, txtNom.getText().trim(), txtPrenom.getText().trim(), txtGrade.getText().trim()));
        JOptionPane.showMessageDialog(this, "Professeur modifié avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
        chargerProfesseurs();
        txtCode.setText(""); txtNom.setText(""); txtPrenom.setText(""); txtGrade.setText("");
    }

    private void supprimerProfesseur() {
        String code = txtCode.getText().trim();
        if (code.isBlank()) { showError("Code requis."); return; }
        int choice = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce professeur?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            profDAO.supprimer(code);
            JOptionPane.showMessageDialog(this, "Professeur supprimé avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerProfesseurs();
            txtCode.setText(""); txtNom.setText(""); txtPrenom.setText(""); txtGrade.setText("");
        }
    }
    private void rechercherProfesseur() {
        // Prendre le premier champ non vide (code, nom ou prénom) comme mot-clé de recherche
        String motCle = txtCode.getText().trim();
        if (motCle.isBlank()) motCle = txtNom.getText().trim();
        if (motCle.isBlank()) motCle = txtPrenom.getText().trim();
        if (motCle.isBlank()) {
            showError("Entrez un code, un nom ou un prénom à rechercher.");
            return;
        }
        List<Prof> list = profDAO.rechercherParMotCle(motCle);
        tableModel.setRowCount(0);
        for (Prof p : list) {
            tableModel.addRow(new Object[]{p.getCodeprof(), p.getNom(), p.getPrenom(), p.getGrade()});
        }
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun professeur trouvé pour \"" + motCle + "\".", "Résultat", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void chargerProfesseurs() {
        List<Prof> list = profDAO.afficherTous();
        tableModel.setRowCount(0);
        for(Prof p : list) tableModel.addRow(new Object[]{p.getCodeprof(), p.getNom(), p.getPrenom(), p.getGrade()});
    }
    private void showError(String m) { JOptionPane.showMessageDialog(this, m, "Erreur", JOptionPane.ERROR_MESSAGE); }
}