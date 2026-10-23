package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Opens connections to the Horizon MySQL database.
 *
 * Settings are read, in this order, from:
 *   1. environment variables DB_URL, DB_USER, DB_PASSWORD
 *   2. a db.properties file in the project's working directory
 *      (copy db.properties.example and fill it in - it is git-ignored)
 *   3. defaults (local MySQL, user "root", empty password)
 */
public class DBConnection {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties props = new Properties();
        try (InputStream in = new FileInputStream("db.properties")) {
            props.load(in);
        } catch (IOException e) {
            // No db.properties file - fall back to environment variables / defaults.
        }

        URL = setting("DB_URL", "db.url", props, "jdbc:mysql://localhost:3306/HorizonDB");
        USER = setting("DB_USER", "db.user", props, "root");
        PASSWORD = setting("DB_PASSWORD", "db.password", props, "");
    }

    private static String setting(String envName, String propName, Properties props, String fallback) {
        String fromEnv = System.getenv(envName);
        if (fromEnv != null && !fromEnv.isEmpty()) {
            return fromEnv;
        }
        return props.getProperty(propName, fallback);
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}
