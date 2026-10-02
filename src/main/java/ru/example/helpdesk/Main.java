package ru.example.helpdesk;

import ru.example.helpdesk.model.Customer;
import ru.example.helpdesk.model.User;
import ru.example.helpdesk.model.UserRole;
import ru.example.helpdesk.repository.UserRepository;
import ru.example.helpdesk.repository.jdbc.JdbcUserRepository;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== СОЗДАНИЕ ПОЛЬЗОВАТЕЛЯ ===");

        UserRepository userRepository =
                new JdbcUserRepository();

        User user = new Customer(
                0,
                "Анна Петрова",
                "anna.test2@example.org",
                UserRole.CUSTOMER,
                null
        );

        User savedUser = userRepository.save(user);

        System.out.println("Пользователь создан:");
        System.out.println(savedUser.getName());
        System.out.println(savedUser.getEmail());
        System.out.println(savedUser.getRole());
    }
}
