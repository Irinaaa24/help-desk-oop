package ru.example.helpdesk.model;

public class Administrator extends User {
    public Administrator(
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
        System.out.println(getName() + " управляет системой");
    }
}


