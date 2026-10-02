package ru.example.helpdesk.model;

public class SupportAgent extends User {
    public SupportAgent(
            long id,
            String name,
            String email,
            UserRole role,
            Long departmentId
    ) {
        super(id, name, email, role, departmentId);
    }
    @Override
    public void performAction() {
        System.out.println(getName() + " обрабатывает заявку");
    }
}

