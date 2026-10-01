package helpdesk;
import java.util.ArrayList;
import java.util.List;
public class AuditService {
    private final List<String> actions = new ArrayList<>();
    public void log(String action) {
        actions.add(action);
    }
    public void printAudit() {
        System.out.println("История действий:");

        for (String action : actions) {
            System.out.println(action);
        }
    }
}