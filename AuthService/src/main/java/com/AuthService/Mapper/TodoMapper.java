package com.AuthService.Mapper;

import com.AuthService.Dto.TodoResponse;
import todo.Todo;

import java.util.UUID;

public class TodoMapper {
    public static TodoResponse toDto(Todo.TodoResponse grpcResponse) {
        TodoResponse dto = new TodoResponse();
        dto.setId(UUID.fromString(grpcResponse.getId()));
        dto.setDesc(grpcResponse.getTodoDesc());
        dto.setName(grpcResponse.getName());
        dto.setCreatedAt(grpcResponse.getTodoCreatedAt());
        dto.setUpdatedAt(grpcResponse.getTodoUpdatedAt());
        return dto;
    }
}
