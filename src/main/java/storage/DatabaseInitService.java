package storage;

import org.flywaydb.core.Flyway;

public class DatabaseInitService {

    public void initDb(String url) {

        Flyway flyway = Flyway
                .configure()
                .dataSource(url, null, null)
                .load();

        flyway.migrate();
    }
}