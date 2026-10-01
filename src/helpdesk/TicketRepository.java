package helpdesk;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class TicketRepository {
    private final List<Ticket> tickets = new ArrayList<>();
    public void add(Ticket ticket) {
        tickets.add(ticket);
    }

    public List<Ticket> findAll() {
        return new ArrayList<>(tickets);
    }

    public Optional<Ticket> findById(long id) {
        for (Ticket ticket : tickets) {
            if (ticket.getId() == id) {
                return Optional.of(ticket);
            }
        }

        return Optional.empty();
    }

    public Ticket getById(long id) {
        return findById(id).orElseThrow(
                () -> new TicketNotFoundException(
                        "Заявка с ID " + id + " не найдена"
                )
        );
    }

    public List<Ticket> findByStatus(TicketStatus status) {
        List<Ticket> result = new ArrayList<>();

        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == status) {
                result.add(ticket);
            }
        }

        return result;
    }

    public void getStatistics() {
        for (TicketStatus status : TicketStatus.values()) {
            int count = 0;

            for (Ticket ticket : tickets) {
                if (ticket.getStatus() == status) {
                    count++;
                }
            }

            System.out.println(status + ": " + count);
        }
    }
}