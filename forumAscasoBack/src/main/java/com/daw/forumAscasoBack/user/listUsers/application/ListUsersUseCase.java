package com.daw.forumAscasoBack.user.listUsers.application;

import com.daw.forumAscasoBack.user.listUsers.domain.ListUsersRepositoryPort;
import com.daw.forumAscasoBack.user.shared.domain.model.User;
import java.util.List;

public class ListUsersUseCase {
    private final ListUsersRepositoryPort repository;

    public ListUsersUseCase(ListUsersRepositoryPort repository) {
        this.repository = repository;
    }

    public List<User> execute() {
        return repository.findAll();
    }
}