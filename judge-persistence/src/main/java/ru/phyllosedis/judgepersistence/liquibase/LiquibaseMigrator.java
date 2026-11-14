package ru.phyllosedis.judgepersistence.liquibase;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import javax.sql.DataSource;
import java.sql.Connection;

@ApplicationScoped
public class LiquibaseMigrator {

    @Resource(lookup = "jdbc/judgeDS")
    private DataSource dataSource;

    public void migrate(@Observes @Initialized(ApplicationScoped.class) Object unit) {
        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            try (Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.yaml",
                    new ClassLoaderResourceAccessor(),
                    database)) {
                liquibase.update("");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute Liquibase migrations", e);
        }
    }
}
