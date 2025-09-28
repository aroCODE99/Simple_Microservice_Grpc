package com.TodoService.Mapper;

import com.TodoService.Dto.TodoRequest;
import com.TodoService.Entity.Todo;
import todo.Todo.TodoResponse;

public class TodoMapper {

    public static Todo toModel(TodoRequest todoRequest) {
        Todo todo = new Todo();
        todo.setDone(todoRequest.isDone());
        todo.setDescription(todoRequest.getDesc());
        todo.setName(todoRequest.getName());
        todo.setUserId(todo.getUserId());
        return todo;
    }

    public static Todo fromGrpcRequestToModel(todo.Todo.TodoRequest request) {
        Todo todo = new Todo();
        todo.setDescription(request.getTodoDesc());
        todo.setName(request.getTodoName());
        todo.setDone(Boolean.parseBoolean(request.getTodoName()));
        todo.setUserId(request.getUserId());
        return todo;
    }

    public static todo.Todo.TodoResponse fromModelToGrpcResponse(Todo todo) {
        return TodoResponse.newBuilder()
            .setId(todo.getId().toString())
            .setName(todo.getName())
            .setTodoDesc(todo.getDescription())
            .setTodoDone(todo.isDone())
            .setTodoCreatedAt(todo.getCreatedAt().toString())
            .setTodoUpdatedAt(todo.getUpdatedAt().toString())
            .build();
    }

    public static TodoRequest fromGrpcRequestToDto(todo.Todo.TodoRequest request) {
        TodoRequest dto = new TodoRequest();
        dto.setName(request.getTodoName());
        dto.setDone(request.getTodoDone());
        dto.setDesc(request.getTodoDesc());
        dto.setUserId(request.getUserId());
        return dto;
    }

}
