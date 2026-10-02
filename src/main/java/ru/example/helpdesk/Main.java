package ru.example.helpdesk;

import ru.example.helpdesk.model.Category;
import ru.example.helpdesk.model.Department;
import ru.example.helpdesk.repository.CategoryRepository;
import ru.example.helpdesk.repository.DepartmentRepository;
import ru.example.helpdesk.repository.jdbc.JdbcCategoryRepository;
import ru.example.helpdesk.repository.jdbc.JdbcDepartmentRepository;

public class Main {
    public static void main(String[] args) {

        CategoryRepository categoryRepository =
                new JdbcCategoryRepository();

        DepartmentRepository departmentRepository =
                new JdbcDepartmentRepository();

        System.out.println("=== КАТЕГОРИИ ===");

        for (Category category : categoryRepository.findAllCategories()) {
            System.out.println(category);
        }

        System.out.println("\n=== ОТДЕЛЫ ===");

        for (Department department : departmentRepository.findAllDepartments()) {
            System.out.println(department);
        }
    }
}