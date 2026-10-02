package ru.example.helpdesk.repository.jdbc;

import ru.example.helpdesk.config.DatabaseConfig;
import ru.example.helpdesk.model.Department;
import ru.example.helpdesk.repository.DepartmentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcDepartmentRepository implements DepartmentRepository {

    @Override
    public List<Department> findAllDepartments() {
        String sql = "SELECT id, name FROM departments ORDER BY id";

        List<Department> departments = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                departments.add(
                        new Department(
                                resultSet.getLong("id"),
                                resultSet.getString("name")
                        )
                );
            }

            return departments;

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения отделов", e);
        }
    }

    @Override
    public Optional<Department> findDepartmentById(long id) {
        String sql = "SELECT id, name FROM departments WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(
                            new Department(
                                    resultSet.getLong("id"),
                                    resultSet.getString("name")
                            )
                    );
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска отдела", e);
        }
    }
}