package com.AuthService.Controller;

import java.util.List;
import com.AuthService.Services.AuthService;
import com.AuthService.Dto.RegisterDto;
import com.AuthService.grpc.TodoClient;
import com.AuthService.Dto.LoginDto;
import com.AuthService.Entity.AppUser;
import com.AuthService.Repositorys.AuthRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final AuthRepo authRepo;

    private final TodoClient todoClient;

    public AuthController(AuthService authService, AuthRepo authRepo, TodoClient todoClient) {
        this.authService = authService;
        this.authRepo = authRepo;
        this.todoClient = todoClient;
    }

    @GetMapping("/hello")
    public ResponseEntity<?> sayHello(@RequestParam("mes") String message) {
        try{
            return ResponseEntity.ok(todoClient.sayHello(message));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: Server Error");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto dto) {
        System.out.println("Registering the user......");
        AppUser user = authService.registerUser(dto);
        if (user == null) {
            ResponseEntity.badRequest().body("Server Error: Unable to register the user");
        }
        return ResponseEntity.ok("User registered SuccessFully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto dto) {
        return ResponseEntity.ok(authService.loginUser(dto));
    }

    @GetMapping("/users")
    public List<AppUser> getUsers() {
        return authRepo.findAll();
    }
}
