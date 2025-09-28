package com.TodoService.Repositories;

import com.TodoService.Entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoRepo extends JpaRepository<Todo, UUID> {
    Optional<List<Todo>> findByUserId(String userId);
}
