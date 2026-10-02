package ru.example.helpdesk.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String URL =
            System.getenv().getOrDefault(
                    "DB_URL",
                    "jdbc:postgresql://localhost:5432/helpdesk_db"
            );

    private static final String USER =
            System.getenv().getOrDefault(
                    "DB_USER",
                    "helpdesk_app"
            );

    private static final String PASSWORD =
            System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new IllegalStateException(
                    "Переменная окружения DB_PASSWORD не задана"
            );
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}