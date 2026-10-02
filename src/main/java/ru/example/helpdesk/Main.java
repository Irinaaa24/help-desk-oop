package ru.example.helpdesk;

import ru.example.helpdesk.model.Ticket;
import ru.example.helpdesk.model.TicketPriority;
import ru.example.helpdesk.model.TicketStatus;
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
                "Проверка транзакции",
                "Проверка изменения статуса",
                TicketPriority.HIGH,
                1L,
                1L
        );

        System.out.println("Создана заявка:");
        System.out.println(ticket);

        ticketService.changeStatus(
                ticket.getId(),
                TicketStatus.IN_PROGRESS,
                2L
        );

        System.out.println("Статус изменён:");
        System.out.println(
                ticketRepository.findById(ticket.getId()).orElseThrow()
        );
    }
}