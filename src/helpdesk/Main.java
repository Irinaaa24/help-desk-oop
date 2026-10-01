package helpdesk;
public class Main {
    public static void main(String[] args) {
        System.out.println("=== HELP DESK ===");
        AuditService auditService = new AuditService();
        Customer customer = new Customer(
                1,
                "Анна Петрова",
                "anna@mail.ru"
        );

        SupportAgent supportAgent = new SupportAgent(
                2,
                "Сергей Иванов",
                "sergey@helpdesk.ru"
        );

        System.out.println("Клиент: " + customer.getName());
        auditService.log(
                "Создан клиент: " + customer.getName()
        );

        Device device = new Device(
                "Ноутбук",
                "Lenovo IdeaPad 5",
                "SN123456"
        );

        Ticket ticket = new Ticket(
                1,
                "Не работает Wi-Fi",
                "После подключения к сети интернет не работает",
                TicketPriority.HIGH,
                device
        );

        auditService.log(
                "Создана заявка #" + ticket.getId()
        );

        System.out.println(
                "Заявка #" + ticket.getId()
                        + ": " + ticket.getTitle()
                        + " | " + ticket.getStatus()
                        + " | Приоритет: " + ticket.getPriority()
        );

        System.out.println(
                "Устройство: " + ticket.getDevice()
        );

        System.out.println(
                "Создана: " + ticket.getCreatedAt()
        );

        NotificationService notificationService =
                new ConsoleNotificationService();

        TicketService ticketService =
                new TicketService(notificationService);

        ticketService.startTicket(ticket);
        System.out.println(
                "Статус: " + ticket.getStatus()
        );

        ticketService.resolveTicket(ticket);
        System.out.println(
                "Статус: " + ticket.getStatus()
        );

        ticketService.closeTicket(ticket);
        System.out.println(
                "Статус: " + ticket.getStatus()
        );

        auditService.log(
                "Заявка #" + ticket.getId() + " закрыта"
        );

        ticket.cancel();

        System.out.println(
                "Статус после попытки отмены: "
                        + ticket.getStatus()
        );

        TicketRepository repository =
                new TicketRepository();

        repository.add(ticket);

        System.out.println("Полный список заявок:");

        for (Ticket t : repository.findAll()) {
            System.out.println(t);
        }

        repository.findById(1).ifPresent(t ->
                System.out.println(
                        "Найдена заявка: "
                                + t.getTitle()
                )
        );

        System.out.println("Закрытые заявки:");

        for (Ticket t :
                repository.findByStatus(TicketStatus.CLOSED)) {

            System.out.println(t);
        }

        Ticket emailTicket = new Ticket(
                2,
                "Ошибка приложения",
                "Приложение не запускается",
                TicketPriority.MEDIUM,
                new Device(
                        "Компьютер",
                        "HP ProDesk",
                        "SN222222"
                )
        );

        NotificationService emailNotification =
                new EmailNotificationService();

        TicketService emailTicketService =
                new TicketService(emailNotification);

        emailTicketService.startTicket(emailTicket);

        Ticket telegramTicket = new Ticket(
                3,
                "Не печатает принтер",
                "Принтер не печатает документы",
                TicketPriority.LOW,
                new Device(
                        "Принтер",
                        "HP LaserJet",
                        "SN333333"
                )
        );

        NotificationService telegramNotification =
                new TelegramNotificationService();

        TicketService telegramTicketService =
                new TicketService(telegramNotification);

        telegramTicketService.startTicket(
                telegramTicket
        );

        System.out.println("Статистика:");

        repository.getStatistics();

        auditService.printAudit();

        System.out.println(
                "История изменений статуса:"
        );

        for (StatusHistory history :
                ticket.getHistory()) {

            System.out.println(history);
        }

        try {
            repository.getById(999);
        } catch (TicketNotFoundException e) {
            System.out.println(
                    "Проверка №16: " + e.getMessage()
            );
        }

        try {
            ticket.resolve();
        } catch (InvalidTicketStateException e) {
            System.out.println(
                    "Проверка №16: " + e.getMessage()
            );
        }
    }
}
