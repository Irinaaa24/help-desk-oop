package ru.example.helpdesk.repository.jdbc;

import ru.example.helpdesk.config.DatabaseConfig;
import ru.example.helpdesk.model.TicketStatusHistory;
import ru.example.helpdesk.repository.StatusHistoryRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class JdbcStatusHistoryRepository implements StatusHistoryRepository {

    @Override
    public List<TicketStatusHistory> findByTicketId(long ticketId) {
        String sql = """
                SELECT id,
                       ticket_id,
                       old_status,
                       new_status,
                       changed_by_id,
                       changed_at
                FROM ticket_status_history
                WHERE ticket_id = ?
                ORDER BY changed_at
                """;

        List<TicketStatusHistory> history = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, ticketId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    TicketStatusHistory item =
                            new TicketStatusHistory(
                                    resultSet.getLong("ticket_id"),
                                    resultSet.getString("old_status"),
                                    resultSet.getString("new_status"),
                                    resultSet.getLong("changed_by_id"),
                                    resultSet.getTimestamp("changed_at")
                                            .toLocalDateTime()
                            );

                    history.add(item);
                }
            }

            return history;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка получения истории статусов",
                    e
            );
        }
    }
}