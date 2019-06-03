package ch.hepia.repository;

import ch.hepia.model.user.User;

import java.util.Optional;

public interface UserInterface {
    void create(User u);

    Optional<User> checkUserCredentials(String username, String password);

    boolean isExisting(String username);
}