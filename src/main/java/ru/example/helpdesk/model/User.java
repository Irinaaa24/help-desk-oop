package ru.example.helpdesk.model;

public abstract class User {
    private long id;
    private String name;
    private String email;
    private UserRole role;
    private Long departmentId;

    public User(
            long id,
            String name,
            String email,
            UserRole role,
            Long departmentId
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.departmentId = departmentId;
    }
    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public UserRole getRole() {
        return role;
    }
    public Long getDepartmentId() {
        return departmentId;
    }
    public abstract void performAction();
}

