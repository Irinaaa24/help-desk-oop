package helpdesk;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class Ticket {
    private long id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private LocalDateTime createdAt;
    private Device device;
    private final List<StatusHistory> history = new ArrayList<>();
    public Ticket(
            long id,
            String title,
            String description,
            TicketPriority priority,
            Device device
    ) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Название заявки не может быть пустым"
            );
        }

        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.device = device;
        this.status = TicketStatus.NEW;
        this.createdAt = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Device getDevice() {
        return device;
    }

    public List<StatusHistory> getHistory() {
        return new ArrayList<>(history);
    }

    public void startProcessing() {
        if (status != TicketStatus.NEW) {
            throw new InvalidTicketStateException(
                    "В работу можно взять только новую заявку"
            );
        }

        TicketStatus oldStatus = status;
        status = TicketStatus.IN_PROGRESS;

        history.add(new StatusHistory(oldStatus, status));
    }

    public void resolve() {
        if (status != TicketStatus.IN_PROGRESS) {
            throw new InvalidTicketStateException(
                    "Решить можно только заявку в работе"
            );
        }

        TicketStatus oldStatus = status;
        status = TicketStatus.RESOLVED;

        history.add(new StatusHistory(oldStatus, status));
    }

    public void close() {
        if (status != TicketStatus.RESOLVED) {
            throw new InvalidTicketStateException(
                    "Закрыть можно только решённую заявку"
            );
        }

        TicketStatus oldStatus = status;
        status = TicketStatus.CLOSED;

        history.add(new StatusHistory(oldStatus, status));
    }

    public void cancel() {
        if (status == TicketStatus.CLOSED) {
            System.out.println(
                    "Ошибка: закрытую заявку нельзя отменить"
            );
            return;
        }

        TicketStatus oldStatus = status;
        status = TicketStatus.CANCELLED;

        history.add(new StatusHistory(oldStatus, status));
    }

    @Override
    public String toString() {
        return "#" + id
                + " " + title
                + " | " + status
                + " | Приоритет: " + priority
                + " | Устройство: " + device;
    }
}