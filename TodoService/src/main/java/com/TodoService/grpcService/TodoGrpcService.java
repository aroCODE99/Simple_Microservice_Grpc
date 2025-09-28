package com.TodoService.grpcService;

import com.TodoService.Dto.TodoRequest;
import com.TodoService.Mapper.TodoMapper;
import com.TodoService.Repositories.TodoRepo;
import io.grpc.stub.StreamObserver;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import todo.Todo;
import todo.TodoServiceGrpc;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.grpc.Status.*;

@GrpcService
public class TodoGrpcService extends TodoServiceGrpc.TodoServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(TodoGrpcService.class);

    private final TodoRepo todoRepo;

    public TodoGrpcService(TodoRepo todoRepo) {
        this.todoRepo = todoRepo;
    }

    @Override
    public void hello(Todo.StringMessage request, StreamObserver<Todo.StringMessage> responseObserver) {
        Todo.StringMessage message = Todo.StringMessage.newBuilder()
            .setInfo("Hello " + request.getInfo())
            .build();
        responseObserver.onNext(message);
        responseObserver.onCompleted();
    }

    @Override
    public void getTodos(Todo.Id request, StreamObserver<Todo.TodoResponse> responseObserver) {
        try {
            List<com.TodoService.Entity.Todo> todos = todoRepo.findByUserId(request.getId()).orElseThrow(
                    () -> new UsernameNotFoundException("User not present in our database")
                    );
            for (com.TodoService.Entity.Todo todo : todos) {
                Todo.TodoResponse todoResponse = TodoMapper.fromModelToGrpcResponse(todo);
                responseObserver.onNext(todoResponse);
            }
            responseObserver.onCompleted();
        } catch (UsernameNotFoundException e) {
            log.warn("User not found: {}", e.getMessage());
            responseObserver.onError(NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            log.error("Unexpected error in getTodos: {}", e.getMessage(), e);
            responseObserver.onError(INTERNAL
                    .withDescription("Internal server error")
                    .asRuntimeException());
        }
    }

    @Override
    public void addTodo(Todo.TodoRequest request, StreamObserver<Todo.TodoResponse> responseObserver) {
        try {
            com.TodoService.Entity.Todo todo = TodoMapper.fromGrpcRequestToModel(request);
            Todo.TodoResponse resp = TodoMapper.fromModelToGrpcResponse(todoRepo.save(todo));
            responseObserver.onNext(resp);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Unexpected error in addTodo: {}", e.getMessage(), e);
            responseObserver.onError(INTERNAL
                .withDescription("Failed to add todo")
                .asRuntimeException());
        }
    }

    @Override
    public void deleteTodo(Todo.DeleteById request, StreamObserver<Todo.StringMessage> responseObserver) {
        try {
            if (!todoRepo.existsById(UUID.fromString(request.getId()))) {
                throw new EntityNotFoundException("Todo with the given id is not present");
            }
            todoRepo.deleteById(UUID.fromString(request.getId()));
            Todo.StringMessage respMessage = Todo.StringMessage.newBuilder().setInfo("Deleted successfully").build();
            responseObserver.onNext(respMessage);
            responseObserver.onCompleted();
        } catch (EntityNotFoundException e) {
            log.warn("Todo not found: {}", e.getMessage());
            responseObserver.onError(NOT_FOUND
                .withDescription(e.getMessage())
                .asRuntimeException());
        } catch (UsernameNotFoundException e) {
            log.warn("User not found: {}", e.getMessage());
            responseObserver.onError(NOT_FOUND
                .withDescription(e.getMessage())
                .asRuntimeException());
        } catch (Exception e) {
            log.error("Unexpected error in getTodos: {}", e.getMessage(), e);
            responseObserver.onError(INTERNAL
                .withDescription("Internal server error")
                .asRuntimeException());
        }
    }

//     @Override
//     public void updateTodo(Todo.TodoRequest request, StreamObserver<Todo.StringMessage> responseObserver) {
//         try {
//             if (!todoRepo.existsById(UUID.fromString(request.getId()))) {
//             }
//             todoRepo.deleteById(UUID.fromString(request.getId()));
//             Todo.StringMessage respMessage = Todo.StringMessage.newBuilder().setInfo("Deleted successfully").build();
//             responseObserver.onNext(respMessage);
//             responseObserver.onCompleted();
//         } catch (UsernameNotFoundException e) {
//             log.warn("User not found: {}", e.getMessage());
//             responseObserver.onError(NOT_FOUND
//                 .withDescription(e.getMessage())
//                 .asRuntimeException());
//         } catch (Exception e) {
//             log.error("Unexpected error in getTodos: {}", e.getMessage(), e);
//             responseObserver.onError(INTERNAL
//                 .withDescription("Internal server error")
//                 .asRuntimeException());
//         }
//     }

}

