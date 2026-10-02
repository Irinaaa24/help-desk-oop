package ru.example.helpdesk.repository;

import ru.example.helpdesk.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    List<Category> findAllCategories();
    Optional<Category> findCategoryById(long id);
}