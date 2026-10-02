package ru.example.helpdesk;

import ru.example.helpdesk.config.DatabaseConfig;
import java.sql.Connection;

public class DatabaseTest {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConfig.getConnection()) {
            System.out.println("Подключение к PostgreSQL успешно!");
            System.out.println("Автокоммит: " + connection.getAutoCommit());
        } catch (Exception e) {
            System.out.println("Ошибка подключения к PostgreSQL:");
            e.printStackTrace();
        }
    }
}
