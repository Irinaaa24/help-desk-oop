package ru.example.helpdesk;

import ru.example.helpdesk.config.DatabaseConfig;
import ru.example.helpdesk.model.Category;
import ru.example.helpdesk.model.Ticket;
import ru.example.helpdesk.model.TicketComment;
import ru.example.helpdesk.model.TicketDetails;
import ru.example.helpdesk.model.TicketStatus;
import ru.example.helpdesk.model.User;
import ru.example.helpdesk.repository.CategoryRepository;
import ru.example.helpdesk.repository.StatusHistoryRepository;
import ru.example.helpdesk.repository.TicketRepository;
import ru.example.helpdesk.repository.UserRepository;
import ru.example.helpdesk.repository.jdbc.JdbcCategoryRepository;
import ru.example.helpdesk.repository.jdbc.JdbcStatusHistoryRepository;
import ru.example.helpdesk.repository.jdbc.JdbcTicketRepository;
import ru.example.helpdesk.repository.jdbc.JdbcUserRepository;
import ru.example.helpdesk.repository.jdbc.JdbcCommentRepository;
import ru.example.helpdesk.service.TicketService;
import java.sql.Connection;
import java.util.List;
public class Main {

    public static void main(String[] args) {

        UserRepository userRepository = new JdbcUserRepository();
        CategoryRepository categoryRepository =
                new JdbcCategoryRepository();
        TicketRepository ticketRepository =
                new JdbcTicketRepository();
        JdbcCommentRepository commentRepository =
                new JdbcCommentRepository();
        StatusHistoryRepository historyRepository =
                new JdbcStatusHistoryRepository();

        TicketService ticketService =
                new TicketService(ticketRepository);
        if (args.length > 0) {
            long ticketId = Long.parseLong(args[0]);

            Ticket ticket = ticketRepository
                    .findById(ticketId)
                    .orElseThrow();

            System.out.println("Проверка после перезапуска:");
            System.out.println("Заявка найдена в PostgreSQL:");
            System.out.println(ticket);
            System.out.println("ID заявки: " + ticket.getId());

            return;
        }

        try (Connection connection = DatabaseConfig.getConnection()) {
            System.out.println("1. Подключение к PostgreSQL: успешно");
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        User customer = userRepository
                .findByEmail("anna@example.org")
                .orElseThrow();

        System.out.println("2. Клиент: " + customer.getName());

        List<Category> categories =
                categoryRepository.findAllCategories();

        System.out.println("3. Категории:");
        categories.forEach(System.out::println);

        Category category = categoryRepository
                .findCategoryById(1)
                .orElseThrow();

        Ticket ticket = ticketService.createTicket(
                "Проверка Help Desk",
                "Проверка полного сценария практической работы №2",
                ru.example.helpdesk.model.TicketPriority.HIGH,
                customer.getId(),
                category.getId()
        );

        System.out.println("4. Заявка создана");
        System.out.println("5. ID заявки: " + ticket.getId());

        Ticket loadedTicket = ticketRepository
                .findById(ticket.getId())
                .orElseThrow();

        System.out.println("6. Заявка через findById:");
        System.out.println(loadedTicket);

        long supportAgentId = 2L;

        ticketService.assignTicket(
                ticket.getId(),
                supportAgentId
        );

        System.out.println("7. Сотрудник назначен");

        TicketComment customerComment = new TicketComment(
                ticket.getId(),
                customer.getId(),
                "Клиентский комментарий для проверки",
                false
        );

        commentRepository.add(customerComment);

        System.out.println("8. Клиентский комментарий добавлен");

        ticketService.changeStatus(
                ticket.getId(),
                TicketStatus.RESOLVED,
                supportAgentId
        );

        System.out.println("9. Статус изменён: IN_PROGRESS -> RESOLVED");

        TicketComment internalComment = new TicketComment(
                ticket.getId(),
                supportAgentId,
                "Внутренний комментарий сотрудника",
                true
        );

        commentRepository.add(internalComment);

        System.out.println("10. Внутренний комментарий добавлен");

        ticketService.changeStatus(
                ticket.getId(),
                TicketStatus.CLOSED,
                supportAgentId
        );

        System.out.println("11. Статус изменён: RESOLVED -> CLOSED");

        System.out.println("12. История статусов:");

        historyRepository
                .findByTicketId(ticket.getId())
                .forEach(System.out::println);

        System.out.println("13. JOIN:");

        List<TicketDetails> details =
                ticketRepository.findAllWithDetails();

        details.stream()
                .filter(item -> item.toString()
                        .startsWith("#" + ticket.getId() + " "))
                .forEach(System.out::println);

        System.out.println();
        System.out.println("14. Заявка сохранена в PostgreSQL.");
        System.out.println("ID для проверки после перезапуска: "
                + ticket.getId());
    }
}