package mg.gestionsalle.dao;

import java.util.List;
import mg.gestionsalle.entity.Prof;
import mg.gestionsalle.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ProfDAO {

    public void ajouter(Prof prof) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(prof);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            e.printStackTrace();
        }
    }

    public void modifier(Prof prof) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(prof);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            e.printStackTrace();
        }
    }

    public void supprimer(String codeProf) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            Prof prof = session.get(Prof.class, codeProf);

            if (prof != null) {

                session.remove(prof);

            }

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            e.printStackTrace();
        }
    }

    public Prof rechercher(String codeProf) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(Prof.class, codeProf);

        }

    }

    public List<Prof> afficherTous() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("from Prof", Prof.class).list();

        }

    }

    public List<Prof> rechercherParMotCle(String motCle) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String pattern = "%" + motCle.toLowerCase() + "%";

            return session.createQuery(
                    "from Prof p where lower(p.codeprof) like :pat "
                    + "or lower(p.nom) like :pat "
                    + "or lower(p.prenom) like :pat",
                    Prof.class)
                    .setParameter("pat", pattern)
                    .list();
        }
    }

}
