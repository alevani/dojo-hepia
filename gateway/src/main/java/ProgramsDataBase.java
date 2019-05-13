import java.util.ArrayList;

public abstract class ProgramsDataBase {
    public abstract void createProgram(Program prg);

    public abstract void createKata(Kata kata);

    public abstract ArrayList<ProgramShowCase> getProgramsDetails();

    public abstract Kata getProgramKata(String programID, String kataID);

    public abstract ArrayList<KataShowCase> getProgramKatasDetails(String programID);

    public abstract ArrayList<String> getProgramDetailsByID(String id);

    public abstract ArrayList<ProgramShowCase> getProgramDetailsByResource(String type, String resource);

    public abstract ProgramSubscription getSubscriptionByID(String userid, String idrogram);

    public abstract void createProgramSubscritpion(ProgramSubscription p);

    public abstract void toggleSubscription(String userid, String idrogram);

    public abstract ArrayList<ProgramShowCase> getUserSubscription(String userid);

    public abstract ArrayList<ProgramShowCase> getUserProgram(String userid);

    public abstract ArrayList<KataSubscription> getKataSubscription(String programid, String userid);

    public abstract KataSubscription getKataSubscriptionByID(String kataid, String programid, String userid);

    public abstract void createKataSubscription(String kataid, String programid, String userid);

    public abstract void incKataSubscriptionAttempt(String kataid, String programid, String userid);

    public abstract void updateKataSubscription(String kataid, String programid, String userid, String sol);

}
