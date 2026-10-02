package ru.example.helpdesk;

import ru.example.helpdesk.model.Ticket;
import ru.example.helpdesk.model.TicketPriority;
import ru.example.helpdesk.repository.TicketRepository;
import ru.example.helpdesk.repository.jdbc.JdbcTicketRepository;
import ru.example.helpdesk.service.TicketService;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== HELP DESK ===");

        TicketRepository ticketRepository =
                new JdbcTicketRepository();

        TicketService ticketService =
                new TicketService(ticketRepository);

        Ticket ticket = ticketService.createTicket(
                "Не работает Wi-Fi",
                "Ноутбук подключается к сети, но Интернет недоступен",
                TicketPriority.HIGH,
                1L,
                1L
        );

        System.out.println("Создана заявка:");
        System.out.println(ticket);

        System.out.println("Все заявки:");

        for (Ticket t : ticketRepository.findAll()) {
            System.out.println(t);
        }
    }
}
