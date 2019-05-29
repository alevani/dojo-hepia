package ch.hepia.repository.cursors;

import ch.hepia.repository.modals.user.User;

import java.util.Optional;

public interface UserInterface {
    void create(User u);

    Optional<User> checkUserCredentials(String username, String password);

    boolean isExisting(String username);
}