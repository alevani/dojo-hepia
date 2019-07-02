package ch.hepia.repository;

import ch.hepia.model.kata.Kata;
import ch.hepia.model.kata.KataShowCase;
import ch.hepia.model.kata.KataSubscription;
import ch.hepia.model.program.Program;
import ch.hepia.model.program.ProgramShowCase;
import ch.hepia.model.program.ProgramSubscription;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface ProgramInterface {
    void create(Program program);

    void create(Kata kata, String programid, boolean goal);

    CompletionStage<List<ProgramShowCase>> programsDetails();

    CompletionStage<Kata> kata(String kataid, String programid);

    CompletionStage<Boolean> isKataActivated(String kataid, String programid);

    CompletionStage<List<KataShowCase>> kataDetails(String programID, String userid);

    CompletionStage<ProgramShowCase> programDetailsById(String id);

    CompletionStage<List<ProgramShowCase>> programDetailsFiltered(String type, String resource);

    CompletionStage<ProgramSubscription> subscriptionByID(String userid, String programid);

    void create(String userid, ProgramSubscription p);

    void toggleSubscription(String userid, String programid);

    CompletionStage<KataSubscription> kataSubscriptionById(String kataid, String programid, String userid);

    void createKataSubscription(String kataid, String programid, String userid, String status);

    void deleteProgram(String programid);

    void deleteKata(String kataid, String programid);

    CompletionStage<Boolean> isProgramOwner(String userid, String programid);

    CompletionStage<Boolean> isKataOwner(String userid, String kataid, String programid);

    void update(String programid, ProgramShowCase p);

    CompletionStage<String> update(Kata k, String programid);

    void toggleKataActivation(String kataid, String programid);


    void toggleIsClosed(String kataid, String programid);

    CompletionStage<Boolean> isKataClosed(String kataid, String programid);

    void duplicateProgram(String programid, String newprogramid, String title);

    void incrementKataSubscriptionAttempt(String kataid, String programid, String userid);

    void updateKataSubscription(String kataid, String programid, String userid, String sol, String status);

    CompletionStage<Boolean> isSubscribed(String userid, String programid);

    CompletionStage<Boolean> check(String programid, String password);

    CompletionStage<List<ProgramShowCase>> userSubscriptions(String userid);

    CompletionStage<List<ProgramShowCase>>  userPrograms(String userid);
}
