
import client.Client;
import org.junit.jupiter.api.*;
import planet.Planet;
import storage.DatabaseInitService;
import storage.HibernateUtil;
import storage.StorageConstant;
import ticket.Ticket;
import ticket.TicketCrudService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TicketCrudServiceTest {

    private TicketCrudService ticketCrudService;


    @BeforeAll
    public void setup() {
        new DatabaseInitService().initDb(StorageConstant.TEST_CONNECTION_URL);
        ticketCrudService = new TicketCrudService();
    }

    @BeforeEach
    public void beforeEachTest() {
        ticketCrudService.clear();
    }

    @AfterAll
    public void tearDown() {

        HibernateUtil.getInstance().getSessionFactory().close();
    }

    @Test
    void testCreateTicket() {
        Client client = new Client();
        client.setName("Test Client");
        client.setId(1L);

        Planet fromPlanet = new Planet();
        fromPlanet.setId("EA");
        fromPlanet.setName("Earth");

        Planet toPlanet = new Planet();
        toPlanet.setId("MSA");
        toPlanet.setName("Mars");

        Ticket createdTicket = ticketCrudService.saveTicket(client, fromPlanet, toPlanet);

        assertNotNull(createdTicket, "Ticket should not be null");
        assertNotNull(createdTicket.getId(), "Ticket ID should not be null");
        assertNotNull(createdTicket.getClient(), "Client should not be null");
        assertNotNull(createdTicket.getFromPlanet(), "From Planet should not be null");
        assertEquals(fromPlanet.getId(), createdTicket.getFromPlanet().getId(), "From Planet ID should match");
        assertNotNull(createdTicket.getToPlanet(), "To Planet should not be null");
        assertEquals(toPlanet.getId(), createdTicket.getToPlanet().getId(), "To Planet ID should match");
    }

    @Test
    void testDeleteTicket() {
        Client client = new Client();
        client.setName("Test Client");
        client.setId(1L);

        Planet fromPlanet = new Planet();
        fromPlanet.setId("EA");
        fromPlanet.setName("Earth");

        Planet toPlanet = new Planet();
        toPlanet.setId("MSA");
        toPlanet.setName("Mars");

        Ticket ticket = ticketCrudService.saveTicket(client, fromPlanet, toPlanet);

        assertNotNull(ticket, "Ticket should not be null");

        Ticket deletedTicket = ticketCrudService.deleteTicket(ticket.getId());

        assertNull(ticketCrudService.getById(ticket.getId()), "Ticket should be deleted from the database");
    }

    @Test
    void testUpdateTicket_success() {

        Planet fromPlanet = new Planet();
        fromPlanet.setId("EARTH");
        fromPlanet.setName("Earth");

        Planet toPlanet = new Planet();
        toPlanet.setId("JUP");
        toPlanet.setName("Jupiter");

        Client client = new Client();
        client.setName("Test Client");
        client.setId(1L);

        Ticket createdTicket = ticketCrudService.saveTicket(client, fromPlanet, toPlanet);

        assertNotNull(createdTicket);
        assertNotNull(createdTicket.getId());
        System.out.println("Created Ticket ID: " + createdTicket.getId());

        Planet newFromPlanet = new Planet();
        newFromPlanet.setId("JUP");
        newFromPlanet.setName("Jupiter");

        Planet newToPlanet = new Planet();
        newToPlanet.setId("EARTH");
        newToPlanet.setName("Earth");

        Ticket updateTicket = ticketCrudService.updateTicket(createdTicket.getId(), newToPlanet, newFromPlanet);

        assertNotNull(updateTicket);
        assertEquals("Jupiter", updateTicket.getFromPlanet().getName());
        assertEquals("Earth", updateTicket.getToPlanet().getName());
    }

    @Test
    void testUpdateTicket_ticketNotFound() {

        Planet newToPlanet = new Planet();
        newToPlanet.setId("JUP");
        newToPlanet.setName("Jupiter");

        Planet newFromPlanet = new Planet();
        newFromPlanet.setId("EARTH");
        newFromPlanet.setName("Earth");


        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ticketCrudService.updateTicket(99L, newToPlanet, newFromPlanet);
        });

        assertEquals("Ticket does not exist in the database", exception.getMessage());

    }

    @Test
    void testUpdateTicketPlanetNotFound() {
        Planet nonExistentPlanet = new Planet();
        nonExistentPlanet.setId("NON_EXISTENT");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ticketCrudService.updateTicket(1L, nonExistentPlanet, nonExistentPlanet);
        });

        String expectedMessage = "does not exist in the database";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetAll() {
        Client client1 = new Client();
        client1.setName("Test Client 1");
        client1.setId(1L);

        Planet fromPlanet1 = new Planet();
        fromPlanet1.setId("EA");
        fromPlanet1.setName("Earth");

        Planet toPlanet1 = new Planet();
        toPlanet1.setId("MSA");
        toPlanet1.setName("Mars");

        Ticket ticket1 = ticketCrudService.saveTicket(client1, fromPlanet1, toPlanet1);

        Client client2 = new Client();
        client2.setName("Test Client 2");
        client2.setId(2L);

        Planet fromPlanet2 = new Planet();
        fromPlanet2.setId("VEN");
        fromPlanet2.setName("Venus");

        Planet toPlanet2 = new Planet();
        toPlanet2.setId("SAT");
        toPlanet2.setName("Saturn");

        Ticket ticket2 = ticketCrudService.saveTicket(client2, fromPlanet2, toPlanet2);

        List<Ticket> tickets = ticketCrudService.getAll();

        assertNotNull(tickets);
        assertEquals(2, tickets.size());
        assertTrue(tickets.stream().anyMatch(ticket -> ticket.getId().equals(ticket1.getId())));
        assertTrue(tickets.stream().anyMatch(ticket -> ticket.getId().equals(ticket2.getId())));

    }

}

