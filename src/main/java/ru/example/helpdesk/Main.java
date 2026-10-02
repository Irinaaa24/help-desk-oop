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
                "Проверка назначения",
                "Проверка назначения сотрудника",
                TicketPriority.HIGH,
                1L,
                1L
        );

        System.out.println("Создана заявка:");
        System.out.println(ticket);

        ticketService.assignTicket(
                ticket.getId(),
                2L
        );

        System.out.println("После назначения:");
        System.out.println(
                ticketRepository.findById(ticket.getId()).orElseThrow()
        );
    }
}