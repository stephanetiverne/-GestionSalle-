package mg.gestionsalle;

import java.util.List;
import mg.gestionsalle.dao.ProfDAO;
import mg.gestionsalle.entity.Prof;
import mg.gestionsalle.util.HibernateUtil;
import org.hibernate.Session;

public class TestConnexion {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("Test de la connexion Hibernate");
        System.out.println("================================");

        try {

            Session session = HibernateUtil.getSessionFactory().openSession();

            System.out.println("Connexion à PostgreSQL réussie !");

            session.close();

            ProfDAO dao = new ProfDAO();

            // Insertion d'un professeur de test
            Prof prof = new Prof("P001", "RAKOTO", "Jean", "Maitre de conférences");

            dao.ajouter(prof);

            System.out.println("Professeur ajouté avec succès.");

            // Affichage de tous les professeurs
            List<Prof> liste = dao.afficherTous();

            System.out.println("\nListe des professeurs :");

            for (Prof p : liste) {

                System.out.println(
                        p.getCodeprof() + " | "
                        + p.getNom() + " | "
                        + p.getPrenom() + " | "
                        + p.getGrade());

            }

            HibernateUtil.shutdown();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}