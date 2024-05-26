package storage;

import client.Client;
import client.ClientCrudService;
import planet.Planet;
import planet.PlanetCrudService;
import ticket.Ticket;
import ticket.TicketCrudService;


import java.util.List;

public class Main {
    public static void main(String[] args) {

        new DatabaseInitService().initDb(StorageConstant.CONNECTION_URL);

        ClientCrudService clientCrudService = new ClientCrudService();
        PlanetCrudService planetCrudService = new PlanetCrudService();
        TicketCrudService ticketCrudService = new TicketCrudService();

        clientCrudService.create("Trina New ");
        clientCrudService.deleteById(1L);
        clientCrudService.update(3L, "Update Name");


        List<Client> clients = clientCrudService.listAll();
        Planet mars = planetCrudService.getById("MARS");
        planetCrudService.deleteById("VEN");
        planetCrudService.update("SAT", "newSaturn");
        List<Planet> all = planetCrudService.getAll();
        System.out.println(" ");

        System.out.println("clients = " + clients);
        System.out.println("mars = " + mars);
        System.out.println("all = " + all.size());
        System.out.println(" ");

        clientCrudService.create("Test Client");

        planetCrudService.create("TEST1", "Test Planet 1");
        planetCrudService.create("TEST2", "Test Planet 2");

        Client client = clientCrudService.getByIdWithTickets(1L);
        Planet fromPlanet = planetCrudService.getByIdWithTickets("TEST1");
        Planet toPlanet = planetCrudService.getByIdWithTickets("TEST2");

        if (client != null && fromPlanet != null && toPlanet != null) {
            ticketCrudService.saveTicket(client, fromPlanet, toPlanet);
        } else {
            System.out.println("Client or Planets not found");
        }

        Ticket ticket = ticketCrudService.getTicketWithClient(3L);
        Ticket getByIdTicket = ticketCrudService.getById(6L);

        Planet newFromPlanet = planetCrudService.getById("VEN");
        Planet newToPlanet = planetCrudService.getById("JUP");

        if (newFromPlanet != null && newToPlanet != null) {
            ticketCrudService.updateTicket(4L, newToPlanet, newFromPlanet);
        } else {
            System.out.println("Planets for update not found");
        }
        Ticket deleteTicket = ticketCrudService.deleteTicket(6L);
        System.out.println(" ");
        System.out.println("deleteTicket = " + deleteTicket);
        System.out.println("getByIdTicket = " + getByIdTicket);
        System.out.println("ticketCrudService.getById(4L) = " + ticketCrudService.getById(4L));
        System.out.println(ticket);
        System.out.println(" ");

        List<Ticket> tickets = ticketCrudService.getAll();
        System.out.println(tickets);
    }
}
