package com.AuthService.Controller;

import com.AuthService.Dto.TodoRequest;
import com.AuthService.grpc.TodoClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/todo")
public class TodoController {

    private final TodoClient todoClient;

    public TodoController(TodoClient todoClient) {
        this.todoClient = todoClient;
    }

    @GetMapping("/getAllTodos")
    public ResponseEntity<?> getTodos(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(todoClient.getTodos(authHeader));
    }

    @PostMapping
    public ResponseEntity<?> addTodo(@RequestHeader("Authorization") String authHeader,
                                     @RequestBody TodoRequest request) {
        return ResponseEntity.ok(todoClient.addTodo(authHeader, request));
    }

}
