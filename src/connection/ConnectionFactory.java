package connection;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Centraliza a criação e a configuração das conexões SQLite.
 */
public final class ConnectionFactory {

    private static final String DB_PROPERTY = "obras.db.path";
    private static final String DEFAULT_DB_NAME = "sistema.db";

    private ConnectionFactory() {
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(getJdbcUrl());
        configurar(connection);
        return connection;
    }

    public static Path getDatabasePath() {
        String configuredPath = System.getProperty(DB_PROPERTY);
        if (configuredPath == null || configuredPath.isBlank()) {
            return Paths.get(DEFAULT_DB_NAME).toAbsolutePath().normalize();
        }
        return Paths.get(configuredPath.trim()).toAbsolutePath().normalize();
    }

    private static String getJdbcUrl() {
        return "jdbc:sqlite:" + getDatabasePath();
    }

    private static void configurar(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
            statement.execute("PRAGMA busy_timeout = 5000");
        }
    }
}
