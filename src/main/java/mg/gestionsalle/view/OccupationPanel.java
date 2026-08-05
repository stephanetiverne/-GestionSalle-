package mg.gestionsalle.view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import com.toedter.calendar.JDateChooser;
import mg.gestionsalle.dao.*;
import mg.gestionsalle.entity.*;

public class OccupationPanel extends JPanel {

    private final OccuperDAO occuperDAO = new OccuperDAO();
    private final SalleDAO salleDAO = new SalleDAO();
    private final ProfDAO profDAO = new ProfDAO();
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField txtProf;
    private final JComboBox<String> comboSalle;
    private final JDateChooser dateChooser;

    public OccupationPanel() {
        setLayout(new BorderLayout(25, 0));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel header = new JLabel("Affectation des Salles");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setForeground(new Color(11, 48, 92));
        add(header, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(360, 0));

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(25, 20, 25, 20)));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 5, 8, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        txtProf = new JTextField();
        txtProf.setPreferredSize(new Dimension(0, 35));
        
        comboSalle = new JComboBox<>();
        comboSalle.setPreferredSize(new Dimension(0, 35));
        comboSalle.setBackground(Color.WHITE);

        dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(0, 35));

        addLabel(formCard, "CODE PROFESSEUR", c, 0);
        c.gridy = 1; formCard.add(txtProf, c);
        
        addLabel(formCard, "SÉLECTIONNER SALLE", c, 2);
        c.gridy = 3; formCard.add(comboSalle, c);
        
        addLabel(formCard, "DATE D'OCCUPATION", c, 4);
        c.gridy = 5; formCard.add(dateChooser, c);

        JPanel btnPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        btnPanel.add(createBtn("Ajouter", new Color(46, 204, 113), e -> ajouterOccupation()));
        btnPanel.add(createBtn("Modifier", new Color(52, 152, 219), e -> modifierOccupation()));
        btnPanel.add(createBtn("Supprimer", new Color(231, 76, 60), e -> supprimerOccupation()));
        btnPanel.add(createBtn("Rafraîchir", new Color(52, 73, 94), e -> { chargerSallesDansCombo(); chargerOccupations(); }));

        c.gridy = 6; c.gridwidth = 2;
        formCard.add(btnPanel, c);
        leftPanel.add(formCard, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);

        tableModel = new DefaultTableModel(new Object[]{"ID", "CODE PROF", "PROFESSEUR", "SALLE", "DATE"}, 0);
        table = new JTable(tableModel);
        styleTable(table);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Listener pour pré-remplir les champs au clic sur le tableau
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtProf.setText(tableModel.getValueAt(row, 1).toString());
                String salle = tableModel.getValueAt(row, 3).toString();
                for (int i = 0; i < comboSalle.getItemCount(); i++) {
                    if (comboSalle.getItemAt(i).contains(salle)) {
                        comboSalle.setSelectedIndex(i);
                        break;
                    }
                }
                try {
                    dateChooser.setDate(new java.text.SimpleDateFormat("yyyy-MM-dd").parse(tableModel.getValueAt(row, 4).toString()));
                } catch (Exception ex) {
                    dateChooser.setDate(null);
                }
            }
        });

        chargerSallesDansCombo();
        chargerOccupations();

        // Recharger les salles et occupations à chaque fois que le panneau devient visible
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                chargerSallesDansCombo();
                chargerOccupations();
            }
        });
    }

    private void addLabel(JPanel p, String t, GridBagConstraints c, int y) {
        JLabel l = new JLabel(t); l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(new Color(130, 140, 160));
        c.gridy = y; p.add(l, c);
    }

    private JButton createBtn(String t, Color bg, java.awt.event.ActionListener al) {
        JButton b = new JButton(t); b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12)); b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR)); b.addActionListener(al);
        return b;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(35); t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.setSelectionBackground(new Color(232, 242, 255));
    }

    private void chargerOccupations() {
        List<Occuper> list = occuperDAO.afficherTous();
        tableModel.setRowCount(0);
        for(Occuper o : list) tableModel.addRow(new Object[]{o.getId(), o.getProf().getCodeprof(), o.getProf().getNom(), o.getSalle().getDesignation(), o.getDateOccupation()});
    }

    private void chargerSallesDansCombo() {
        comboSalle.removeAllItems();
        for(Salle s : salleDAO.afficherTous()) comboSalle.addItem(s.getCodesal() + " - " + s.getDesignation());
    }
    
    // ... Garder tes méthodes ajouterOccupation, etc. ici ...
    private void ajouterOccupation() {
        String codeProfStr = txtProf.getText().trim();
        if (codeProfStr.isBlank()) { JOptionPane.showMessageDialog(this, "Code professeur requis.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        if (comboSalle.getSelectedItem() == null) { JOptionPane.showMessageDialog(this, "Salle requise.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        if (dateChooser.getDate() == null) { JOptionPane.showMessageDialog(this, "Date requise.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        
        Prof profObj = profDAO.rechercher(codeProfStr);
        if (profObj == null) { JOptionPane.showMessageDialog(this, "Professeur non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        
        String salleStr = comboSalle.getSelectedItem().toString();
        String codeSalle = salleStr.split(" - ")[0];
        Salle salleObj = salleDAO.rechercher(codeSalle);
        if (salleObj == null) { JOptionPane.showMessageDialog(this, "Salle non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        
        java.util.Date utilDate = dateChooser.getDate();
        java.time.LocalDate localDate = utilDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        Occuper occuper = new Occuper(profObj, salleObj, localDate);
        occuperDAO.ajouter(occuper);
        
        JOptionPane.showMessageDialog(this, "Occupation ajoutée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
        chargerOccupations();
        txtProf.setText("");
        dateChooser.setDate(null);
        table.clearSelection();
    }

    private void modifierOccupation() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Sélectionnez une occupation à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        String codeProfStr = txtProf.getText().trim();
        if (codeProfStr.isBlank()) { JOptionPane.showMessageDialog(this, "Code professeur requis.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        if (comboSalle.getSelectedItem() == null) { JOptionPane.showMessageDialog(this, "Salle requise.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        if (dateChooser.getDate() == null) { JOptionPane.showMessageDialog(this, "Date requise.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        
        Prof profObj = profDAO.rechercher(codeProfStr);
        if (profObj == null) { JOptionPane.showMessageDialog(this, "Professeur non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        
        String salleStr = comboSalle.getSelectedItem().toString();
        String codeSalle = salleStr.split(" - ")[0];
        Salle salleObj = salleDAO.rechercher(codeSalle);
        if (salleObj == null) { JOptionPane.showMessageDialog(this, "Salle non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        
        int id = (int) tableModel.getValueAt(row, 0);
        java.util.Date utilDate = dateChooser.getDate();
        java.time.LocalDate localDate = utilDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        Occuper occuper = new Occuper(profObj, salleObj, localDate);
        occuper.setId(id);
        occuperDAO.modifier(occuper);
        
        JOptionPane.showMessageDialog(this, "Occupation modifiée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
        chargerOccupations();
        txtProf.setText("");
        dateChooser.setDate(null);
        table.clearSelection();
    }

    private void supprimerOccupation() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Sélectionnez une occupation à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        
        int choice = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette occupation?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(row, 0);
            occuperDAO.supprimer(id);
            
            JOptionPane.showMessageDialog(this, "Occupation supprimée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerOccupations();
            txtProf.setText("");
            dateChooser.setDate(null);
            table.clearSelection();
        }
    }
}