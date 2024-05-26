package ticket;

import client.Client;
import org.hibernate.Session;
import org.hibernate.Transaction;
import planet.Planet;
import storage.HibernateUtil;

import java.util.List;


public class TicketCrudService {


    public Ticket getTicketWithClient(Long ticketId) {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            return session.createQuery("SELECT t FROM Ticket t JOIN FETCH t.client WHERE t.id = :ticketId", Ticket.class)
                    .setParameter("ticketId", ticketId)
                    .uniqueResult();
        }
    }

    public Ticket getById(Long id) {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            return session.get(Ticket.class, id);
        }
    }

    public Ticket saveTicket(Client client, Planet fromPlanet, Planet toPlanet) {
        if (client == null || client.getId() == null
                || fromPlanet == null || fromPlanet.getId() == null
                || toPlanet == null || toPlanet.getId() == null) {
            throw new IllegalArgumentException("Client, fromPlanet, and toPlanet cannot be null or non-existent");
        }

        Transaction transaction = null;
        Ticket ticket = new Ticket();
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            if (client.getId() != null) {
                client = session.merge(client);
            } else {
                session.persist(client);
            }

            if (fromPlanet.getId() != null) {
                fromPlanet = session.merge(fromPlanet);
            } else {
                session.persist(fromPlanet);
            }

            if (toPlanet.getId() != null) {
                toPlanet = session.merge(toPlanet);
            } else {
                session.persist(toPlanet);
            }

            ticket.setClient(client);
            ticket.setFromPlanet(fromPlanet);
            ticket.setToPlanet(toPlanet);
            session.persist(ticket);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw e;
        }
        return ticket;
    }


    public Ticket updateTicket(Long ticketId, Planet newToPlanet, Planet newFromPlanet) {
        if (newToPlanet == null || newFromPlanet == null) {
            throw new IllegalArgumentException("Planets cannot be null");
        }

        Ticket ticket;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ticket = session.get(Ticket.class, ticketId);

            if (ticket != null) {
                Planet fromPlanet = session.get(Planet.class, newFromPlanet.getId());
                Planet toPlanet = session.get(Planet.class, newToPlanet.getId());

                if (fromPlanet == null || toPlanet == null) {
                    throw new IllegalArgumentException("One of the planets does not exist in the database");
                }

                ticket.setFromPlanet(fromPlanet);
                ticket.setToPlanet(toPlanet);
                session.merge(ticket);

                transaction.commit();
            } else {
                throw new IllegalArgumentException("Ticket does not exist in the database");
            }
        } catch (Exception e) {

            e.printStackTrace();
            throw e;
        }
        return ticket;
    }

    public Ticket deleteTicket(Long ticketId) {
        Ticket ticket;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ticket = session.get(Ticket.class, ticketId);

            if (ticket != null) {
                session.remove(ticket);
                transaction.commit();
            } else {
                throw new IllegalArgumentException("Ticket does not exist in the database");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw e;
        }
        return ticket;
    }

    public void clear() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete from Ticket").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Ticket> getAll() {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            return session.createQuery("from Ticket", Ticket.class).list();
        }
    }
}

