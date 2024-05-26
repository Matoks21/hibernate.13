import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.*;
import planet.Planet;
import planet.PlanetCrudService;
import storage.DatabaseInitService;
import storage.StorageConstant;

import static org.junit.jupiter.api.Assertions.*;

class PlanetCrudServiceTest {

    private PlanetCrudService planetCrudService;


    @BeforeEach
    void setUp() {
        new DatabaseInitService().initDb(StorageConstant.TEST_CONNECTION_URL);
        planetCrudService = new PlanetCrudService();
        planetCrudService.clear();
    }

    @Test
    void create_ValidPlanet_ShouldCreatePlanetSuccessfully() {

        String id = "1";
        String name = "Earth";

        assertDoesNotThrow(() -> planetCrudService.create(id, name));

        Planet createdPlanet = planetCrudService.getById(id);
        assertNotNull(createdPlanet);
        assertEquals(id, createdPlanet.getId());
        assertEquals(name, createdPlanet.getName());
    }

    @Test
    void create_NullId_ShouldThrowIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () -> planetCrudService.create(null, "Earth"));
    }

    @Test
    void create_EmptyId_ShouldThrowIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () -> planetCrudService.create("", "Earth"));
    }

    @Test
    void create_NullName_ShouldThrowIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () -> planetCrudService.create("1", null));
    }

    @Test
    void create_EmptyName_ShouldThrowIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () -> planetCrudService.create("1", ""));
    }

    @Test
    void create_LongName_ShouldThrowIllegalArgumentException() {

        String longName = "Very long name exceeding 500 characters limit".repeat(20);
        assertThrows(IllegalArgumentException.class, () -> planetCrudService.create("1", longName));
    }

    @Test
    void create_DuplicatePlanet_ShouldThrowPersistenceException() {
        String name = "Earth";
        planetCrudService.create("1", name);
        assertThrows(PersistenceException.class, () -> planetCrudService.create("2", name));
    }


    @Test
    void testGetById() {

        String id = "1";
        String name = "Mars";
        planetCrudService.create(id, name);

        Planet planet = planetCrudService.getById(id);

        assertNotNull(planet);
        assertEquals(id, planet.getId());
        assertEquals(name, planet.getName());
    }

    @Test
    void testUpdate_invalidId_throwsIllegalArgumentException() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> planetCrudService.update(null, "New Name"));
        assertEquals("Invalid id or name", exception.getMessage());
    }

    @Test
    void testUpdate_emptyId_throwsIllegalArgumentException() {

        String emptyId = "";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> planetCrudService.update(emptyId, "New Name"));
        assertEquals("Invalid id or name", exception.getMessage());
    }

    @Test
    void testUpdate_invalidName_throwsIllegalArgumentException() {

        String validId = "1";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> planetCrudService.update(validId, null));
        assertEquals("Invalid id or name", exception.getMessage());
    }

    @Test
    void testUpdate_emptyName_throwsIllegalArgumentException() {

        String validId = "1";
        String emptyName = "";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> planetCrudService.update(validId, emptyName));
        assertEquals("Invalid id or name", exception.getMessage());
    }


    @Test
    void testDeleteById() {

        String id = "1";
        String name = "Venus";
        planetCrudService.create(id, name);

        planetCrudService.deleteById(id);
        Planet deletedPlanet = planetCrudService.getById(id);

        assertNull(deletedPlanet);
    }
}
