package ru.example.helpdesk.model;

import java.time.LocalDateTime;
public class TicketDetails {
    private Long id;
    private String title;
    private String status;
    private String priority;
    private String categoryName;
    private String customerName;
    private String assigneeName;
    private LocalDateTime createdAt;
    public TicketDetails(
            Long id,
            String title,
            String status,
            String priority,
            String categoryName,
            String customerName,
            String assigneeName,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.priority = priority;
        this.categoryName = categoryName;
        this.customerName = customerName;
        this.assigneeName = assigneeName;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "#" + id
                + " | " + title
                + " | " + status
                + " | " + priority
                + " | Категория: " + categoryName
                + " | Клиент: " + customerName
                + " | Сотрудник: " + assigneeName
                + " | Создана: " + createdAt;
    }
}