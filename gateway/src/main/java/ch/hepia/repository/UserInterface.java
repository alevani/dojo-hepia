package ch.hepia.repository;

import ch.hepia.model.user.User;

import java.util.concurrent.CompletionStage;

public interface UserInterface {
    void create(User u);

    User checkUserCredentials(String username, String password);

    CompletionStage<Boolean> isExisting(String username);
}