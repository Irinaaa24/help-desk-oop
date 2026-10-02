package ru.example.helpdesk.repository;

import ru.example.helpdesk.model.Department;
import java.util.List;
import java.util.Optional;
public interface DepartmentRepository {
    List<Department> findAllDepartments();
    Optional<Department> findDepartmentById(long id);
}