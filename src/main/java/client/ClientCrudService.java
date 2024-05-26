package client;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import storage.HibernateUtil;
import ticket.Ticket;

import java.util.List;

public class ClientCrudService {

    public Client getByIdWithTickets(Long id) {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT c FROM Client c LEFT JOIN FETCH c.tickets WHERE c.id = :id", Client.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }


    public Client getById(Long id) {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            Client client = session.get(Client.class, id);
            if (client != null && !Hibernate.isInitialized(client.getTickets())) {
                Hibernate.initialize(client.getTickets());
            }
            return client;
        }
    }
    public void update(Long id, String newName) {
        if (id == null || newName == null || newName.isEmpty() || newName.length() > 500) {
            throw new IllegalArgumentException("Invalid id or name");
        }

        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Query<?> query = session.createQuery("update Client set name = :newName where id = :id");
            query.setParameter("newName", newName);
            query.setParameter("id", id);

            transaction.commit();
        }
    }


    public void create(String name) {
        if (name == null || name.length() <= 3 || name.length() > 200) {
            throw new IllegalArgumentException("Client name cannot be null, empty, or too long");
        }

        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Client client = getClientByName(name, session);

            if (client == null) {
                client = new Client();
                client.setName(name);

                session.merge(client);
            }

            transaction.commit();
        }
    }



    public List<Client> listAll() {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            Query<Client> query = session.createQuery("FROM Client", Client.class);
            return query.list();
        }
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid id");
        }

        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Client clientToDelete = session.get(Client.class, id);

            if (clientToDelete != null) {
                Query<Ticket> ticketQuery = session.createQuery(
                        "FROM Ticket WHERE client = :client", Ticket.class
                );
                ticketQuery.setParameter("client", clientToDelete);
                List<Ticket> ticketsToDelete = ticketQuery.getResultList();
                for (Ticket ticket : ticketsToDelete) {
                    session.remove(ticket);
                }

                session.remove(clientToDelete);
            }

            transaction.commit();
        }
    }

    public void clear() {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            String hql = "delete from Client";
            Query query = session.createQuery(hql);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteDB() {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            String hql = "DROP DATABASE spacetrain";
            Query query = session.createQuery(hql);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Client getClientByName(String name, Session session) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        Query<Client> query = session.createQuery("FROM Client WHERE name = :name", Client.class);
        query.setParameter("name", name);
        return query.uniqueResult();
    }
}
