import client.Client;
import client.ClientCrudService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import storage.DatabaseInitService;
import storage.StorageConstant;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientCrudServiceTest {

    private ClientCrudService clientCrudService;


    @BeforeEach
    public void setUp() {

        new DatabaseInitService().initDb(StorageConstant.TEST_CONNECTION_URL);
        clientCrudService = new ClientCrudService();
        clientCrudService.clear();
    }


    @Test
    void testGetById_invalidId_returnsNull() {
        Client clientName = clientCrudService.getById(-1L);
        assertNull(clientName);
    }

    @Test
    void testCreateValidName() {
        String name = "John Doe";

        clientCrudService.create(name);

        List<Client> clients = clientCrudService.listAll();
        assertEquals(1, clients.size());

        Client createdClient = clients.get(0);
        assertNotNull(createdClient.getId());
        assertEquals(name, createdClient.getName());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "Jo"})
    void testCreateInvalidName(String invalidName) {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> clientCrudService.create(invalidName));
        assertEquals("Client name cannot be null, empty, or too long", exception.getMessage());
    }

    @Test
    void testCreateLongName() {
        String longName = "abcdefghij".repeat(21);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> clientCrudService.create(longName));
        assertEquals("Client name cannot be null, empty, or too long", exception.getMessage());
    }

    @Test
    void testDeleteById_existingId_deletesClientAndAssociatedTickets() {

        long clientId = 1L;
        clientCrudService.deleteById(clientId);
        assertNull(clientCrudService.getById(clientId));
    }


    @Test
    void testUpdateWithNullId() {

        assertThrows(IllegalArgumentException.class, () -> clientCrudService.update(null, "New Name"));
    }

    @Test
    void testUpdateWithNullName() {

        assertThrows(IllegalArgumentException.class, () -> clientCrudService.update(1L, null));
    }

    @Test
    void testUpdateWithEmptyName() {

        assertThrows(IllegalArgumentException.class, () -> clientCrudService.update(1L, ""));
    }

    @Test
    void testUpdateWithLongName() {

        String longName = "Very long name exceeding 500 characters limit".repeat(50);
        assertThrows(IllegalArgumentException.class, () -> clientCrudService.update(1L, longName));
    }
}