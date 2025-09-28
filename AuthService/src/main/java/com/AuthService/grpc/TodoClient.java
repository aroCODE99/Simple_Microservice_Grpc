package com.AuthService.grpc;

import com.AuthService.Dto.TodoRequest;
import com.AuthService.Dto.TodoResponse;
import com.AuthService.Entity.AppUser;
import com.AuthService.Mapper.TodoMapper;
import com.AuthService.Services.JwtService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import todo.Todo;
import todo.TodoServiceGrpc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class TodoClient {

    private static final Logger log = LoggerFactory.getLogger(TodoClient.class);

    private final TodoServiceGrpc.TodoServiceBlockingStub blockingStub;

    private final JwtService jwtService;

    public TodoClient(JwtService jwtService) {
        this.jwtService = jwtService;
        ManagedChannel channel = ManagedChannelBuilder
            .forAddress("todo-service", 9090) // this was causing errror
            .usePlaintext()
            .build();
        this.blockingStub = TodoServiceGrpc.newBlockingStub(channel);
    }

    public String sayHello(String message) {
        System.out.println(message);
        Todo.StringMessage request = Todo.StringMessage.newBuilder()
            .setInfo(message)
            .build();
        return blockingStub.hello(request).getInfo(); 
    }

    // so this is the normal spring service 
    public List<TodoResponse> getTodos(String authHeader) {
        List<TodoResponse> todosResponse = new ArrayList<>();
        String userId = jwtService.getUserId(authHeader);
        try {
            // first build the Request (Todo.request)
            Todo.Id request = Todo.Id.newBuilder()
                .setId(userId)
                .build();

            Iterator<Todo.TodoResponse> todos = blockingStub.getTodos(request);
            while (todos.hasNext()) {
                todosResponse.add(TodoMapper.toDto(todos.next())); // filling the todosResponse List
            }
        } catch (Exception e) {
            log.error("Error fetching todos", e);
        }
        return todosResponse;
    }

    public TodoResponse addTodo(String authHeader, TodoRequest todo) {
        String userId = jwtService.getUserId(authHeader);
        Todo.TodoRequest request = Todo.TodoRequest.newBuilder()
            .setTodoName(todo.getName())
            .setTodoDone(todo.isDone())
            .setTodoDesc(todo.getDesc())
            .setUserId(userId)
            .build();
        Todo.TodoResponse grpcResponse = blockingStub.addTodo(request);
        return TodoMapper.toDto(grpcResponse);
    }

    public Todo.StringMessage updateTodo(TodoRequest todo) {
        Todo.TodoUpdateRequest request = Todo.TodoUpdateRequest.newBuilder()
            .setId(todo.getId())
            .setTodoName(todo.getName())
            .setTodoDesc(todo.getDesc())
            .setTodoDone(todo.isDone())
            .build();
        return blockingStub.updateTodo(request);
    }

    public Todo.StringMessage deleteTodo(String id) {
        Todo.DeleteById request = Todo.DeleteById.newBuilder()
            .setId(id)
            .build();
        return blockingStub.deleteTodo(request);
    }

}

