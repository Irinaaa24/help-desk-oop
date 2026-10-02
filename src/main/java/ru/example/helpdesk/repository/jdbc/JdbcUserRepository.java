package ru.example.helpdesk.repository.jdbc;

import ru.example.helpdesk.config.DatabaseConfig;
import ru.example.helpdesk.model.*;
import ru.example.helpdesk.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    @Override
    public User save(User user) {

        String sql = """
                INSERT INTO users(name, email, role, department_id)
                VALUES (?, ?, ?::user_role, ?)
                RETURNING id, created_at
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getRole().name());

            if (user.getDepartmentId() == null) {
                statement.setNull(4, Types.BIGINT);
            } else {
                statement.setLong(4, user.getDepartmentId());
            }

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return user;
                }

                throw new SQLException("Не удалось создать пользователя");
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка сохранения пользователя", e
            );
        }
    }

    @Override
    public Optional<User> findById(long id) {

        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(mapUser(resultSet));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка поиска пользователя", e
            );
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {

        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(mapUser(resultSet));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка поиска пользователя по email", e
            );
        }
    }

    @Override
    public List<User> findAll() {

        String sql = "SELECT * FROM users ORDER BY id";

        List<User> users = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }

            return users;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка получения пользователей", e
            );
        }
    }

    private User mapUser(ResultSet resultSet) throws SQLException {

        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");

        UserRole role = UserRole.valueOf(
                resultSet.getString("role")
        );

        long departmentId = resultSet.getLong("department_id");
        Long department = resultSet.wasNull()
                ? null
                : departmentId;

        return switch (role) {
            case CUSTOMER ->
                    new Customer(id, name, email, role, department);

            case SUPPORT_AGENT ->
                    new SupportAgent(id, name, email, role, department);

            case ADMIN ->
                    new Administrator(id, name, email, role, department);
        };
    }
}
