package ch.hepia.repository.modals.interfaces;
import ch.hepia.repository.modals.user.User;

import java.util.Optional;

public interface Users {
    void create(User u);

    Optional<User> checkUserCredentials(String username, String password);

    boolean isExisting(String username);
}
