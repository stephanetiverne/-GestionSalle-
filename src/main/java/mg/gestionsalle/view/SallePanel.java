package mg.gestionsalle.view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import mg.gestionsalle.dao.SalleDAO;
import mg.gestionsalle.entity.Salle;

public class SallePanel extends JPanel {

    private final SalleDAO salleDAO = new SalleDAO();
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField txtCode, txtDesignation;

    public SallePanel() {
        setLayout(new BorderLayout(25, 0));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel header = new JLabel("Gestion des Salles");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setForeground(new Color(11, 48, 92));
        add(header, BorderLayout.NORTH);

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
        c.weightx = 1.0;

        txtCode = createStyledField();
        txtDesignation = createStyledField();

        addFormField(formCard, "CODE SALLE", txtCode, c, 0);
        addFormField(formCard, "DÉSIGNATION", txtDesignation, c, 2);

        JPanel btnPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        btnPanel.add(createModernBtn("Ajouter", new Color(46, 204, 113), e -> ajouterSalle()));
        btnPanel.add(createModernBtn("Modifier", new Color(52, 152, 219), e -> modifierSalle()));
        btnPanel.add(createModernBtn("Supprimer", new Color(231, 76, 60), e -> supprimerSalle()));
        btnPanel.add(createModernBtn("Actualiser", new Color(52, 73, 94), e -> chargerSalles()));

        c.gridy = 4; c.gridwidth = 2;
        formCard.add(btnPanel, c);
        leftPanel.add(formCard, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);

        tableModel = new DefaultTableModel(new Object[]{"CODE", "DÉSIGNATION"}, 0);
        table = new JTable(tableModel);
        styleTable(table);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            int r = table.getSelectedRow();
            if(r >= 0) { txtCode.setText(tableModel.getValueAt(r,0).toString()); txtDesignation.setText(tableModel.getValueAt(r,1).toString()); }
        });
        chargerSalles();
    }

    private JTextField createStyledField() {
        JTextField f = new JTextField(); f.setPreferredSize(new Dimension(0, 35));
        f.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200, 200, 200)), new EmptyBorder(0,10,0,10)));
        return f;
    }

    private void addFormField(JPanel p, String text, JTextField f, GridBagConstraints c, int y) {
        JLabel l = new JLabel(text); l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(120, 130, 150));
        c.gridy = y; p.add(l, c); c.gridy = y+1; p.add(f, c);
    }

    private JButton createModernBtn(String t, Color bg, java.awt.event.ActionListener al) {
        JButton b = new JButton(t); b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13)); b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR)); b.addActionListener(al);
        return b;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(35); t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setSelectionBackground(new Color(232, 242, 255));
    }

    private void chargerSalles() {
        List<Salle> list = salleDAO.afficherTous();
        tableModel.setRowCount(0);
        for(Salle s : list) tableModel.addRow(new Object[]{s.getCodesal(), s.getDesignation()});
    }
    // ... copier coller tes méthodes ajouterSalle, modifierSalle, supprimerSalle ici ...
    private void ajouterSalle() {
        String code = txtCode.getText().trim();
        if (code.isBlank()) { JOptionPane.showMessageDialog(this, "Code salle requis.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        // Vérifier si le code existe déjà
        for (Salle s : salleDAO.afficherTous()) {
            if (s.getCodesal().equalsIgnoreCase(code)) {
                JOptionPane.showMessageDialog(this, "Code salle " + code + " existe déjà dans la base de données!", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        salleDAO.ajouter(new Salle(code, txtDesignation.getText().trim()));
        JOptionPane.showMessageDialog(this, "Salle ajoutée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
        chargerSalles();
        txtCode.setText(""); txtDesignation.setText("");
    }

    private void modifierSalle() {
        String code = txtCode.getText().trim();
        if (code.isBlank()) { JOptionPane.showMessageDialog(this, "Code salle requis.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        salleDAO.modifier(new Salle(code, txtDesignation.getText().trim()));
        JOptionPane.showMessageDialog(this, "Salle modifiée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
        chargerSalles();
        txtCode.setText(""); txtDesignation.setText("");
    }

    private void supprimerSalle() {
        String code = txtCode.getText().trim();
        if (code.isBlank()) { JOptionPane.showMessageDialog(this, "Code salle requis.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        int choice = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette salle?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            salleDAO.supprimer(code);
            JOptionPane.showMessageDialog(this, "Salle supprimée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerSalles();
            txtCode.setText(""); txtDesignation.setText("");
        }
    }
}