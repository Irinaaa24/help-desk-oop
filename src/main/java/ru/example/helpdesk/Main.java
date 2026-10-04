package ru.example.helpdesk;

import ru.example.helpdesk.model.TicketDetails;
import ru.example.helpdesk.repository.TicketRepository;
import ru.example.helpdesk.repository.jdbc.JdbcTicketRepository;

public class Main {
    public static void main(String[] args) {
        TicketRepository ticketRepository =
                new JdbcTicketRepository();
        System.out.println("Заявки с данными JOIN:");
        for (TicketDetails ticket :
                ticketRepository.findAllWithDetails()) {
            System.out.println(ticket);
        }
    }
}