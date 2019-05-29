package ch.hepia.repository;

import ch.hepia.repository.modals.kata.*;
import ch.hepia.repository.modals.program.*;
import ch.hepia.repository.modals.user.User;


import java.util.List;
import java.util.Optional;

public interface ProgramsDataBase {
    void create(Program program);

    void create(Kata kata, String programid);

    Optional<List<ProgramShowCase>> programsDetails();

    Optional<Kata> kata(String kataid);

    Optional<List<KataShowCase>> kataDetails(String programID, String userid);

    Optional<ProgramShowCase> programDetailsById(String id);

    Optional<List<ProgramShowCase>> programDetailsFiltered(String type, String resource);

    Optional<ProgramSubscription> subscriptionByID(String userid, String idrogram);

    void create(String userid, ProgramSubscription p);

    void toggleSubscription(String userid, String idprogram);


    Optional<KataSubscription> kataSubscriptionById(String kataid, String programid, String userid);

    void createKataSubscription(String kataid, String programid, String userid);


    void deleteProgram(String programid);


    void deleteKata(String kataid);

    boolean isProgramOwner(String userid, String programid);

    boolean isKataOwner(String userid, String kataid);

    void update(String programid, ProgramShowCase p);

    String update(Kata k);

    void toggleKataActivation(String kataid);

    boolean isKataActivated(String kataid);




    void incrementKataSubscriptionAttempt(String kataid, String programid, String userid);

    void updateKataSubscription(String kataid, String programid, String userid, String sol, String status);

    boolean isSubscribed(String userid, String programid);

    Optional<List<ProgramShowCase>> userSubscriptions(String userid);

    Optional<List<ProgramShowCase>> userPrograms(String userid);

    void create(User u);

    boolean isExisting(String username);

    Optional<User> checkUserCredentials(String username, String password);

}
