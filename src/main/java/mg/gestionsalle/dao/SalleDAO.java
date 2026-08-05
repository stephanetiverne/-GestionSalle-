package mg.gestionsalle.dao;

import java.util.List;
import mg.gestionsalle.entity.Salle;
import mg.gestionsalle.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SalleDAO {

    public void ajouter(Salle salle) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(salle);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            e.printStackTrace();
        }

    }

    public void modifier(Salle salle) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(salle);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            e.printStackTrace();
        }

    }

    public void supprimer(String codeSalle) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            Salle salle = session.get(Salle.class, codeSalle);

            if (salle != null) {

                session.remove(salle);

            }

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            e.printStackTrace();
        }

    }

    public Salle rechercher(String codeSalle) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(Salle.class, codeSalle);

        }

    }

    public List<Salle> afficherTous() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("from Salle", Salle.class).list();

        }

    }

}