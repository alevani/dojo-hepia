import java.util.ArrayList;
import java.util.Optional;

public abstract class ProgramsDataBase {
    public abstract void create(Program program);

    public abstract void create(Kata kata);

    public abstract ArrayList<ProgramShowCase> programsDetails();

    public abstract Kata kata(String programID, String kataID);

    public abstract ArrayList<KataShowCase> kataDetails(String programID, String userid);

    public abstract ArrayList<String> programDetailsById(String id);

    public abstract ArrayList<ProgramShowCase> programDetailsFiltered(String type, String resource);

    public abstract ProgramSubscription subscriptionByID(String userid, String idrogram);

    public abstract void create(ProgramSubscription p);

    public abstract void toggleSubscription(String userid, String idrogram);

    public abstract ArrayList<ProgramShowCase> userSubscriptions(String userid);

    public abstract ArrayList<ProgramShowCase> userPrograms(String userid);

    public abstract KataSubscription kataSubscriptionById(String kataid, String programid, String userid);

    public abstract void createKataSubscription(String kataid, String programid, String userid);

    public abstract void incrementKataSubscriptionAttempt(String kataid, String programid, String userid);

    public abstract void updateKataSubscription(String kataid, String programid, String userid, String sol, String status);

    public abstract void deleteProgram(String programid);

    public abstract void create(MockUser u);

    public abstract Optional<MockUser> checkUserCredentials(String username, String password);

    public abstract boolean isExisting(String username);

}
