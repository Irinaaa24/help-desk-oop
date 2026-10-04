package ru.example.helpdesk.repository;

import ru.example.helpdesk.model.TicketStatusHistory;
import java.util.List;
public interface StatusHistoryRepository {
    List<TicketStatusHistory> findByTicketId(long ticketId);
}
