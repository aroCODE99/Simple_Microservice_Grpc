package com.TodoService.Service;

import com.TodoService.Dto.TodoRequest;
import com.TodoService.Entity.Todo;
import com.TodoService.Mapper.TodoMapper;
import com.TodoService.Repositories.TodoRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    private static final Logger log = LoggerFactory.getLogger(TodoService.class);
    private final TodoRepo todoRepo;

    public TodoService(TodoRepo todoRepo) {
        this.todoRepo = todoRepo;
    }

    public Todo addTodo(TodoRequest todoRequest) {
        Todo todo = TodoMapper.toModel(todoRequest);
        return todoRepo.save(todo);
    }

}
