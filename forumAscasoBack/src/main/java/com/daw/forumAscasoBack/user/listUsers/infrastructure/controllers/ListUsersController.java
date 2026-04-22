package com.daw.forumAscasoBack.user.listUsers.infrastructure.controllers;

import com.daw.forumAscasoBack.user.listUsers.application.ListUsersUseCase;
import com.daw.forumAscasoBack.user.shared.domain.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ListUsersController {

    private final ListUsersUseCase listUsersUseCase;

    public ListUsersController(ListUsersUseCase listUsersUseCase) {
        this.listUsersUseCase = listUsersUseCase;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return listUsersUseCase.execute();
    }
}