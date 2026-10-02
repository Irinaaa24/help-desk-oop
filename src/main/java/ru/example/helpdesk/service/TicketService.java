package ru.example.helpdesk.service;

import ru.example.helpdesk.model.Ticket;
import ru.example.helpdesk.model.TicketPriority;
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
            Long customerId,
            Long categoryId
    ) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Название заявки не может быть пустым"
            );
        }

        Ticket ticket = new Ticket(
                title,
                description,
                priority,
                customerId,
                categoryId
        );

        return ticketRepository.save(ticket);
    }
}




