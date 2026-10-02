package ru.example.helpdesk.service;

import ru.example.helpdesk.model.Ticket;
import ru.example.helpdesk.model.TicketPriority;
import ru.example.helpdesk.model.TicketStatus;
import ru.example.helpdesk.repository.TicketRepository;

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
}




