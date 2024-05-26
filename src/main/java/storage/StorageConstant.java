package storage;

public class StorageConstant {
    public static final String CONNECTION_URL = "jdbc:h2:./spacetrain";
    public static final String TEST_CONNECTION_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private StorageConstant() {
    }
}
