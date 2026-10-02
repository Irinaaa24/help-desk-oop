package ru.example.helpdesk.repository.jdbc;

import ru.example.helpdesk.config.DatabaseConfig;
import ru.example.helpdesk.model.Ticket;
import ru.example.helpdesk.model.TicketPriority;
import ru.example.helpdesk.model.TicketStatus;
import ru.example.helpdesk.repository.TicketRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTicketRepository implements TicketRepository {

    @Override
    public Ticket save(Ticket ticket) {

        String sql = """
                INSERT INTO tickets
                (title, description, status, priority, customer_id, category_id)
                VALUES (?, ?, ?::ticket_status, ?::ticket_priority, ?, ?)
                RETURNING id, created_at, updated_at
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, ticket.getTitle());
            statement.setString(2, ticket.getDescription());
            statement.setString(3, ticket.getStatus().name());
            statement.setString(4, ticket.getPriority().name());
            statement.setLong(5, ticket.getCustomerId());
            statement.setLong(6, ticket.getCategoryId());

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    ticket.setId(resultSet.getLong("id"));
                    ticket.setCreatedAt(
                            resultSet.getTimestamp("created_at")
                                    .toLocalDateTime()
                    );
                    ticket.setUpdatedAt(
                            resultSet.getTimestamp("updated_at")
                                    .toLocalDateTime()
                    );

                    return ticket;
                }

                throw new SQLException("Не удалось создать заявку");
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка сохранения заявки", e
            );
        }
    }

    @Override
    public Optional<Ticket> findById(long id) {

        String sql = "SELECT * FROM tickets WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(mapTicket(resultSet));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка поиска заявки", e
            );
        }
    }

    @Override
    public List<Ticket> findAll() {

        String sql = "SELECT * FROM tickets ORDER BY id";

        List<Ticket> tickets = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                tickets.add(mapTicket(resultSet));
            }

            return tickets;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка получения заявок", e
            );
        }
    }

    @Override
    public List<Ticket> findByStatus(TicketStatus status) {

        String sql =
                "SELECT * FROM tickets WHERE status = ?::ticket_status";

        List<Ticket> tickets = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status.name());

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    tickets.add(mapTicket(resultSet));
                }

                return tickets;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка поиска заявок по статусу", e
            );
        }
    }

    @Override
    public void update(Ticket ticket) {

        String sql = """
                UPDATE tickets
                SET title = ?,
                    description = ?,
                    status = ?::ticket_status,
                    priority = ?::ticket_priority,
                    assignee_id = ?,
                    category_id = ?,
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, ticket.getTitle());
            statement.setString(2, ticket.getDescription());
            statement.setString(3, ticket.getStatus().name());
            statement.setString(4, ticket.getPriority().name());

            if (ticket.getAssigneeId() == null) {
                statement.setNull(5, Types.BIGINT);
            } else {
                statement.setLong(5, ticket.getAssigneeId());
            }

            statement.setLong(6, ticket.getCategoryId());
            statement.setLong(7, ticket.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка обновления заявки", e
            );
        }
    }

    @Override
    public boolean deleteById(long id) {

        String sql = "DELETE FROM tickets WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка удаления заявки", e
            );
        }
    }

    private Ticket mapTicket(ResultSet resultSet)
            throws SQLException {

        Ticket ticket = new Ticket();

        ticket.setId(resultSet.getLong("id"));
        ticket.setTitle(resultSet.getString("title"));
        ticket.setDescription(resultSet.getString("description"));

        ticket.setStatus(
                TicketStatus.valueOf(
                        resultSet.getString("status")
                )
        );

        ticket.setPriority(
                TicketPriority.valueOf(
                        resultSet.getString("priority")
                )
        );

        ticket.setCustomerId(
                resultSet.getLong("customer_id")
        );

        long assigneeId = resultSet.getLong("assignee_id");
        ticket.setAssigneeId(
                resultSet.wasNull() ? null : assigneeId
        );

        long categoryId = resultSet.getLong("category_id");
        ticket.setCategoryId(
                resultSet.wasNull() ? null : categoryId
        );

        Timestamp createdAt =
                resultSet.getTimestamp("created_at");

        if (createdAt != null) {
            ticket.setCreatedAt(
                    createdAt.toLocalDateTime()
            );
        }

        Timestamp updatedAt =
                resultSet.getTimestamp("updated_at");

        if (updatedAt != null) {
            ticket.setUpdatedAt(
                    updatedAt.toLocalDateTime()
            );
        }

        Timestamp closedAt =
                resultSet.getTimestamp("closed_at");

        if (closedAt != null) {
            ticket.setClosedAt(
                    closedAt.toLocalDateTime()
            );
        }

        return ticket;
    }
}