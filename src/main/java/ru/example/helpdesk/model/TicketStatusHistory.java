package ru.example.helpdesk.model;

import java.time.LocalDateTime;
public class TicketStatusHistory {
    private Long id;
    private Long ticketId;
    private String oldStatus;
    private String newStatus;
    private Long changedById;
    private LocalDateTime changedAt;
    public TicketStatusHistory(
            Long ticketId,
            String oldStatus,
            String newStatus,
            Long changedById,
            LocalDateTime changedAt
    ) {
        this.ticketId = ticketId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedById = changedById;
        this.changedAt = changedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public Long getChangedById() {
        return changedById;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    @Override
    public String toString() {
        return ticketId
                + " | "
                + oldStatus
                + " -> "
                + newStatus
                + " | пользователь: "
                + changedById
                + " | "
                + changedAt;
    }
}