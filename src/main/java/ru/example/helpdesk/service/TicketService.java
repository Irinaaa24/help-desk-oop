package ru.example.helpdesk.service;

import ru.example.helpdesk.config.DatabaseConfig;
import ru.example.helpdesk.model.Ticket;
import ru.example.helpdesk.model.TicketPriority;
import ru.example.helpdesk.model.TicketStatus;
import ru.example.helpdesk.repository.TicketRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(
            String title,
            String description,
            TicketPriority priority,
            long customerId,
            Long categoryId
    ) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Пустая тема заявки");
        }

        Ticket ticket = new Ticket();

        ticket.setTitle(title.trim());
        ticket.setDescription(description);
        ticket.setStatus(TicketStatus.NEW);
        ticket.setPriority(priority);
        ticket.setCustomerId(customerId);
        ticket.setCategoryId(categoryId);

        return ticketRepository.save(ticket);
    }

    public void changeStatus(
            long ticketId,
            TicketStatus newStatus,
            long changedByUserId
    ) {
        String selectSql =
                "SELECT status FROM tickets WHERE id = ? FOR UPDATE";

        String updateSql = """
                UPDATE tickets
                SET status = ?::ticket_status,
                    updated_at = CURRENT_TIMESTAMP,
                    closed_at = CASE
                        WHEN ?::ticket_status = 'CLOSED'
                        THEN CURRENT_TIMESTAMP
                        ELSE closed_at
                    END
                WHERE id = ?
                """;

        String historySql = """
                INSERT INTO ticket_status_history(
                    ticket_id,
                    old_status,
                    new_status,
                    changed_by_id
                )
                VALUES (?, ?::ticket_status, ?::ticket_status, ?)
                """;

        try (Connection connection = DatabaseConfig.getConnection()) {

            connection.setAutoCommit(false);

            try {
                TicketStatus oldStatus;

                try (PreparedStatement ps =
                             connection.prepareStatement(selectSql)) {

                    ps.setLong(1, ticketId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new IllegalArgumentException(
                                    "Заявка не найдена"
                            );
                        }

                        oldStatus = TicketStatus.valueOf(
                                rs.getString("status")
                        );
                    }
                }

                validateTransition(oldStatus, newStatus);

                try (PreparedStatement ps =
                             connection.prepareStatement(updateSql)) {

                    ps.setString(1, newStatus.name());
                    ps.setString(2, newStatus.name());
                    ps.setLong(3, ticketId);

                    ps.executeUpdate();
                }

                try (PreparedStatement ps =
                             connection.prepareStatement(historySql)) {

                    ps.setLong(1, ticketId);
                    ps.setString(2, oldStatus.name());
                    ps.setString(3, newStatus.name());
                    ps.setLong(4, changedByUserId);

                    ps.executeUpdate();
                }

                connection.commit();

            } catch (Exception e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка изменения статуса",
                    e
            );
        }
    }

    private void validateTransition(
            TicketStatus oldStatus,
            TicketStatus newStatus
    ) {
        boolean allowed = switch (oldStatus) {
            case NEW ->
                    newStatus == TicketStatus.IN_PROGRESS
                            || newStatus == TicketStatus.CANCELLED;

            case IN_PROGRESS ->
                    newStatus == TicketStatus.RESOLVED
                            || newStatus == TicketStatus.CANCELLED;

            case RESOLVED ->
                    newStatus == TicketStatus.CLOSED
                            || newStatus == TicketStatus.IN_PROGRESS;

            case CLOSED, CANCELLED -> false;
        };

        if (!allowed) {
            throw new IllegalStateException(
                    "Недопустимый переход: "
                            + oldStatus + " -> " + newStatus
            );
        }
    }

    public void assignTicket(
            long ticketId,
            long supportAgentId
    ) {
        String userSql = """
                SELECT role
                FROM users
                WHERE id = ?
                """;

        String updateSql = """
                UPDATE tickets
                SET assignee_id = ?,
                    status = 'IN_PROGRESS',
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConfig.getConnection()) {

            try (PreparedStatement ps =
                         connection.prepareStatement(userSql)) {

                ps.setLong(1, supportAgentId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalArgumentException(
                                "Пользователь не найден"
                        );
                    }

                    String role = rs.getString("role");

                    if (!"SUPPORT_AGENT".equals(role)) {
                        throw new IllegalArgumentException(
                                "Пользователь не является сотрудником поддержки"
                        );
                    }
                }
            }

            try (PreparedStatement ps =
                         connection.prepareStatement(updateSql)) {

                ps.setLong(1, supportAgentId);
                ps.setLong(2, ticketId);

                int updated = ps.executeUpdate();

                if (updated == 0) {
                    throw new IllegalArgumentException(
                            "Заявка не найдена"
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка назначения заявки", e
            );
        }
    }
}

