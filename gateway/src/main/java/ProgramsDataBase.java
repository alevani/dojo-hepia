import java.util.List;
import java.util.Optional;

public interface ProgramsDataBase {
    void create(Program program);

    void create(Kata kata);

    Optional<List<ProgramShowCase>> programsDetails();

    Optional<Kata> kata(String kataid);

    Optional<List<KataShowCase>> kataDetails(String programID, String userid);

    Optional<List<String>> programDetailsById(String id);

    Optional<List<ProgramShowCase>> programDetailsFiltered(String type, String resource);

    Optional<ProgramSubscription> subscriptionByID(String userid, String idrogram);

    void create(String userid,ProgramSubscription p);

    void toggleSubscription(String userid, String idprogram);

    Optional<List<ProgramShowCase>> userSubscriptions(String userid);

    Optional<List<ProgramShowCase>> userPrograms(String userid);

    KataSubscription kataSubscriptionById(String kataid, String programid, String userid);

    void createKataSubscription(String kataid, String programid, String userid);

    void incrementKataSubscriptionAttempt(String kataid, String programid, String userid);

    void updateKataSubscription(String kataid, String programid, String userid, String sol, String status);

    void deleteProgram(String programid);

    void create(User u);

    Optional<User> checkUserCredentials(String username, String password);

    boolean isExisting(String username);

}
