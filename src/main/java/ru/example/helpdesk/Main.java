package ru.example.helpdesk;

import ru.example.helpdesk.model.User;
import ru.example.helpdesk.repository.UserRepository;
import ru.example.helpdesk.repository.jdbc.JdbcUserRepository;

public class Main {
    public static void main(String[] args) {

        UserRepository userRepository =
                new JdbcUserRepository();

        System.out.println("=== ВСЕ ПОЛЬЗОВАТЕЛИ ===");

        for (User user : userRepository.findAll()) {
            System.out.println(
                    user.getId() + " | "
                            + user.getName() + " | "
                            + user.getEmail() + " | "
                            + user.getRole()
            );
        }
    }
}