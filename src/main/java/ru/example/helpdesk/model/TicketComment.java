package ru.example.helpdesk.model;

import java.time.LocalDateTime;

public class TicketComment {

    private Long id;
    private Long ticketId;
    private Long authorId;
    private String text;
    private boolean internal;
    private LocalDateTime createdAt;
    private String authorName;
    private String authorRole;

    public TicketComment(
            Long ticketId,
            Long authorId,
            String text,
            boolean internal
    ) {
        this.ticketId = ticketId;
        this.authorId = authorId;
        this.text = text;
        this.internal = internal;
    }

    public TicketComment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorRole() {
        return authorRole;
    }

    public void setAuthorRole(String authorRole) {
        this.authorRole = authorRole;
    }

    @Override
    public String toString() {
        return authorName + " (" + authorRole + "): " + text;
    }
}