package MongoDataBase;

import Kata.*;
import Program.*;
import User.User;


import java.util.List;
import java.util.Optional;

public interface ProgramsDataBase {
    void create(Program program);

    void create(Kata kata);

    Optional<List<ProgramShowCase>> programsDetails();

    Optional<Kata> kata(String kataid);

    Optional<List<KataShowCase>> kataDetails(String programID, String userid);

    Optional<ProgramShowCase> programDetailsById(String id);

    Optional<List<ProgramShowCase>> programDetailsFiltered(String type, String resource);

    Optional<ProgramSubscription> subscriptionByID(String userid, String idrogram);

    void create(String userid, ProgramSubscription p);

    void toggleSubscription(String userid, String idprogram);

    Optional<List<ProgramShowCase>> userSubscriptions(String userid);

    Optional<List<ProgramShowCase>> userPrograms(String userid);

    Optional<KataSubscription> kataSubscriptionById(String kataid, String programid, String userid);

    void createKataSubscription(String kataid, String programid, String userid);

    void incrementKataSubscriptionAttempt(String kataid, String programid, String userid);

    void updateKataSubscription(String kataid, String programid, String userid, String sol, String status);

    void deleteProgram(String programid);

    void create(User u);

    Optional<User> checkUserCredentials(String username, String password);

    boolean isExisting(String username);

    boolean isSubscribed(String userid, String programid);

    void deleteKata(String kataid);

    boolean isOwner(String userid, String programid);

    void update(String programid, ProgramShowCase p);

}
