package com.daw.forumAscasoBack.user.listUsers.domain;

import com.daw.forumAscasoBack.user.shared.domain.model.User;
import java.util.List;

public interface ListUsersRepositoryPort {
    List<User> findAll();
}