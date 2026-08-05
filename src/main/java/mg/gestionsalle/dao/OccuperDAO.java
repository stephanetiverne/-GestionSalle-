package mg.gestionsalle.dao;

import java.util.List;
import mg.gestionsalle.entity.Occuper;
import mg.gestionsalle.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class OccuperDAO {

    public void ajouter(Occuper occuper) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(occuper);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            e.printStackTrace();
        }

    }

    public void modifier(Occuper occuper) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(occuper);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            e.printStackTrace();
        }

    }

    public void supprimer(Integer id) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            Occuper occuper = session.get(Occuper.class, id);

            if (occuper != null) {

                session.remove(occuper);

            }

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            e.printStackTrace();
        }

    }

    public Occuper rechercher(Integer id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(Occuper.class, id);

        }

    }

    public List<Occuper> afficherTous() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("from Occuper", Occuper.class).list();

        }

    }

    public Occuper rechercherParSalleEtDate(String codeSalle, java.time.LocalDate dateOccupation) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Occuper o where o.salle.codesal = :codeSalle and o.dateOccupation = :dateOccupation", Occuper.class)
                    .setParameter("codeSalle", codeSalle)
                    .setParameter("dateOccupation", dateOccupation)
                    .uniqueResult();
        }
    }

}