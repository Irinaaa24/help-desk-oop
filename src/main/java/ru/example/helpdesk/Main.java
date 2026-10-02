package ru.example.helpdesk;

import ru.example.helpdesk.model.TicketComment;
import ru.example.helpdesk.repository.CommentRepository;
import ru.example.helpdesk.repository.jdbc.JdbcCommentRepository;

public class Main {

    public static void main(String[] args) {

        CommentRepository commentRepository =
                new JdbcCommentRepository();

        TicketComment comment = new TicketComment(
                7L,
                2L,
                "Проверка комментария",
                false
        );

        commentRepository.add(comment);

        System.out.println("Добавлен комментарий:");
        System.out.println(comment);

        System.out.println("Комментарии заявки:");

        for (TicketComment item :
                commentRepository.findByTicketId(7L)) {
            System.out.println(item);
        }
    }
}