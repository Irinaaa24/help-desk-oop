package helpdesk;
import java.time.LocalDateTime;
public class StatusHistory {
    private final TicketStatus oldStatus;
    private final TicketStatus newStatus;
    private final LocalDateTime changedAt;
    public StatusHistory(TicketStatus oldStatus, TicketStatus newStatus) {
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedAt = LocalDateTime.now();
    }
    @Override
    public String toString() {
        return oldStatus + " -> " + newStatus + " | " + changedAt;
    }
}
