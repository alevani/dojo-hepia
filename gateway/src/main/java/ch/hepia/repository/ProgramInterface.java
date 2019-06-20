package ch.hepia.repository;

import ch.hepia.model.kata.Kata;
import ch.hepia.model.kata.KataShowCase;
import ch.hepia.model.kata.KataSubscription;
import ch.hepia.model.program.Program;
import ch.hepia.model.program.ProgramShowCase;
import ch.hepia.model.program.ProgramSubscription;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

public interface ProgramInterface {
    void create(Program program);

    void create(Kata kata, String programid,boolean goal);

    Future<List<ProgramShowCase>> programsDetails();

    Optional<Kata> kata(String kataid, String programid);

    Optional<List<KataShowCase>> kataDetails(String programID, String userid);

    Optional<ProgramShowCase> programDetailsById(String id);

    Optional<List<ProgramShowCase>> programDetailsFiltered(String type, String resource);

    Optional<ProgramSubscription> subscriptionByID(String userid, String idrogram);

    void create(String userid, ProgramSubscription p);

    void toggleSubscription(String userid, String idprogram);

    Optional<KataSubscription> kataSubscriptionById(String kataid, String programid, String userid);

    void createKataSubscription(String kataid, String programid, String userid, String status  );

    void deleteProgram(String programid);

    void deleteKata(String kataid,String programid);

    boolean isProgramOwner(String userid, String programid);

    boolean isKataOwner(String userid, String kataid, String programid);

    void update(String programid, ProgramShowCase p);

    String update(Kata k, String programid);

    void toggleKataActivation(String kataid,String programid);

    boolean isKataActivated(String kataid, String programid);

    void duplicateProgram(String programid,String newprogramid,String title);

    void incrementKataSubscriptionAttempt(String kataid, String programid, String userid);

    void updateKataSubscription(String kataid, String programid, String userid, String sol, String status);

    boolean isSubscribed(String userid, String programid);

    boolean check(String programid, String password);

    Optional<List<ProgramShowCase>> userSubscriptions(String userid);

    Optional<List<ProgramShowCase>> userPrograms(String userid);
}
