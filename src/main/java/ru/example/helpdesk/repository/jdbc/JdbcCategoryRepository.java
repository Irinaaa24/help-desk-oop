package ru.example.helpdesk.repository.jdbc;

import ru.example.helpdesk.config.DatabaseConfig;
import ru.example.helpdesk.model.Category;
import ru.example.helpdesk.repository.CategoryRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCategoryRepository implements CategoryRepository {
    @Override
    public List<Category> findAllCategories() {
        String sql = "SELECT id, name FROM categories ORDER BY id";

        List<Category> categories = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                categories.add(
                        new Category(
                                resultSet.getLong("id"),
                                resultSet.getString("name")
                        )
                );
            }

            return categories;

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения категорий", e);
        }
    }

    @Override
    public Optional<Category> findCategoryById(long id) {
        String sql = "SELECT id, name FROM categories WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(
                            new Category(
                                    resultSet.getLong("id"),
                                    resultSet.getString("name")
                            )
                    );
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска категории", e);
        }
    }
}