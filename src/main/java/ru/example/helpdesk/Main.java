package ru.example.helpdesk;

import ru.example.helpdesk.model.Ticket;
import ru.example.helpdesk.model.TicketPriority;
import ru.example.helpdesk.repository.TicketRepository;
import ru.example.helpdesk.repository.jdbc.JdbcTicketRepository;
import ru.example.helpdesk.service.TicketService;

public class Main {
    public static void main(String[] args) {

        TicketRepository ticketRepository =
                new JdbcTicketRepository();

        TicketService ticketService =
                new TicketService(ticketRepository);

        Ticket ticket = ticketService.createTicket(
                "Проверка TicketService",
                "Проверка бизнес-логики",
                TicketPriority.HIGH,
                1L,
                1L
        );

        System.out.println("Создана заявка:");
        System.out.println(ticket);
    }
}