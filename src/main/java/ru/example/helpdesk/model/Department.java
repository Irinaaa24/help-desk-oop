package ru.example.helpdesk.model;

public class Department {

    private long id;
    private String name;
    public Department(long id, String name) {
        this.id = id;
        this.name = name;
    }
    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + " | " + name;
    }
}
