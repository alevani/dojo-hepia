package ch.hepia.repository;

import ch.hepia.model.kata.Kata;
import ch.hepia.model.kata.KataShowCase;
import ch.hepia.model.kata.KataSubscription;
import ch.hepia.model.program.Program;
import ch.hepia.model.program.ProgramShowCase;
import ch.hepia.model.program.ProgramSubscription;

import com.mongodb.*;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.PushOptions;
import com.mongodb.client.model.UpdateOptions;
import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.ne;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Updates.pull;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class Programs implements ProgramInterface {

    private MongoClient mongoClient;
    private MongoDatabase database;

    CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    public Programs() {
        MongoCredential credential = MongoCredential.createCredential("shodai", "DojoHepia", "shodai".toCharArray());
        this.mongoClient = new MongoClient(new ServerAddress("localhost", 27017), Arrays.asList(credential), MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        this.database = mongoClient.getDatabase("DojoHepia");

        database.getCollection("Programs").createIndex(Indexes.ascending("title"));
    }


    public void create(Program program) {
        database.getCollection("Programs", Program.class).insertOne(program);
    }

    public void create(Kata kata, String programid, boolean goal) {
        if (goal)
            database.getCollection("Programs").updateOne(eq("_id", programid), combine(inc("nbKata", 1), pushEach("katas", Arrays.asList(kata), new PushOptions().position(0))));
        else
            database.getCollection("Programs").updateOne(eq("_id", programid), combine(inc("nbKata", 1), push("katas", kata)));
    }

    public CompletionStage<List<ProgramShowCase>> programsDetails() {

        CompletableFuture<List<ProgramShowCase>> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            ArrayList<ProgramShowCase> p = new ArrayList<>();
            MongoCollection<Program> programs = database.getCollection("Programs", Program.class);

            for (Program prg : programs.find())
                p.add(new ProgramShowCase(prg.getTitle(), prg.getSensei(), prg.getLanguage(), prg.getDescription(), prg.getNbKata(), prg.getTags(), prg.getId(), ""));
            return p;
        });

        return completableFuture;
    }

    public CompletionStage<Kata> kata(String kataid, String programid) {
        CompletableFuture<Kata> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            try {
                AggregateIterable<Kata> kata = database.getCollection("Programs", Kata.class).aggregate(Arrays.asList(
                        match(eq("_id", programid)),
                        unwind("$katas"),
                        project(
                                fields(excludeId(), include("katas"))),
                        match(eq("katas._id", kataid)),
                        replaceRoot("$katas")
                ));
                return kata.iterator().next();
            } catch (NoSuchElementException e) {
                return new Kata();
            }
        });

        return completableFuture;
    }

    public CompletionStage<Boolean> isKataActivated(String kataid, String programid) {
        CompletableFuture<Boolean> completableFuture
                = CompletableFuture.supplyAsync(() -> database.getCollection("Programs", Kata.class).aggregate(Arrays.asList(
                match(eq("_id", programid)),
                unwind("$katas"),
                project(
                        fields(excludeId(), include("katas"))),
                match(eq("katas._id", kataid)),
                replaceRoot("$katas")
        )).iterator().next().isActivated());

        return completableFuture;

    }

    public CompletionStage<List<KataShowCase>> kataDetails(String programid, String userid) {

        CompletableFuture<List<KataShowCase>> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            AggregateIterable<Kata> kata = database.getCollection("Programs", Kata.class).aggregate(Arrays.asList(
                    unwind("$katas"),
                    match(eq("_id", programid)),
                    replaceRoot("$katas")

            ));

            ArrayList<KataShowCase> ktsc = new ArrayList<>();
            for (Kata x : kata)
                ktsc.add(new KataShowCase(x.getTitle(), x.getDifficulty(), x.getId(), getKataStatus(x.getId(), programid, userid), x.isActivated()));

            return ktsc;
        });

        return completableFuture;
    }

    private void decrementResolvedKata(String kataid, String programid) {
        database.getCollection("Users").updateMany(combine(eq("programSubscriptions.katas._id", kataid), eq("programSubscriptions.idprogram", programid)), inc("programSubscriptions.$[i].nbKataDone", -1), new UpdateOptions().arrayFilters(Arrays.asList(
                eq("i.katas.status", "RESOLVED")
        )));
    }

    private void incrementResolvedKata(String kataid, String programid) {
        database.getCollection("Users").updateMany(combine(eq("programSubscriptions.katas._id", kataid), eq("programSubscriptions.idprogram", programid)), inc("programSubscriptions.$[i].nbKataDone", 1), new UpdateOptions().arrayFilters(Arrays.asList(
                eq("i.katas.status", "RESOLVED")
        )));
    }

    private String getKataStatus(String kataid, String programid, String userid) {

        try {
            if (isSubscribed(userid, programid).toCompletableFuture().get() || hasBeenSubscribed(userid, programid)) {
                AggregateIterable<KataSubscription> kata = database.getCollection("Users", KataSubscription.class).aggregate(Arrays.asList(
                        match(eq("_id", userid)),
                        unwind("$programSubscriptions"),
                        replaceRoot("$programSubscriptions"),
                        match(eq("idprogram", programid)),
                        unwind("$katas"),
                        match(eq("katas._id", kataid)),
                        replaceRoot("$katas"),
                        project(fields(excludeId(), include("status")))
                ));

                try {
                    return kata.iterator().next().getStatus();
                } catch (NoSuchElementException e) {
                    return "TODO";
                }


            } else
                return "TODO";
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "TODO";
    }

    public CompletionStage<ProgramShowCase> programDetailsById(String id) {
        CompletableFuture<ProgramShowCase> completableFuture
                = CompletableFuture.supplyAsync(() -> {

            try {
                Program program = database.getCollection("Programs", Program.class).aggregate(Arrays.asList(match(eq("_id", id)))).iterator().next();
                return new ProgramShowCase(program.getTitle(), program.getSensei(), program.getLanguage(), program.getDescription(), program.getNbKata(), program.getTags(), program.getId(), program.getPassword());
            } catch (NoSuchElementException e) {
                return new ProgramShowCase();
            }
        });

        return completableFuture;
    }

    public CompletionStage<List<ProgramShowCase>> programDetailsFiltered(String type, String resource) {

        CompletableFuture<List<ProgramShowCase>> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            ArrayList<ProgramShowCase> p = new ArrayList<>();

            MongoCollection<Program> cprograms = database.getCollection("Programs", Program.class);
            Pattern regex = Pattern.compile(resource, Pattern.CASE_INSENSITIVE);
            Iterable<Program> programs = cprograms.find(eq(type, regex));


            programs.forEach(x -> {
                p.add(new ProgramShowCase(x.getTitle(), x.getSensei(), x.getLanguage(), x.getDescription(), x.getNbKata(), x.getTags(), x.getId(), -1));
            });
            return p;
        });

        return completableFuture;
    }

    public CompletionStage<ProgramSubscription> subscriptionByID(String userid, String idrogram) {
        CompletableFuture<ProgramSubscription> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            AggregateIterable<ProgramSubscription> prgsub = database.getCollection("Users", ProgramSubscription.class).aggregate(Arrays.asList(
                    match(eq("_id", userid)),
                    unwind("$programSubscriptions"),
                    project(
                            fields(excludeId(), include("programSubscriptions"))),
                    match(eq("programSubscriptions.idprogram", idrogram)),
                    replaceRoot("$programSubscriptions")
            ));

            try {
                return prgsub.iterator().next();
            } catch (NoSuchElementException e) {
                return new ProgramSubscription();
            }
        });

        return completableFuture;
    }


    public void create(String userid, ProgramSubscription p) {
        database.getCollection("Users").updateOne(eq("_id", userid), push("programSubscriptions", p));
    }

    public void toggleSubscription(String userid, String idprogram) {
        Document status = database.getCollection("Users").aggregate(Arrays.asList(
                match(eq("_id", userid)),
                unwind("$programSubscriptions"),
                project(
                        fields(excludeId(), include("programSubscriptions"))),
                match(eq("programSubscriptions.idprogram", idprogram)),
                replaceRoot("$programSubscriptions"),
                project(
                        fields(excludeId(), include("status")))
        )).iterator().next();

        database.getCollection("Users").updateOne(eq("_id", userid), set("programSubscriptions.$[i].status", !status.getBoolean("status")), new UpdateOptions().arrayFilters(Arrays.asList(
                eq("i.idprogram", idprogram)
        )));
    }

    public void toggleKataActivation(String kataid, String programid) {
        int number;
        boolean isActivated = database.getCollection("Programs", Kata.class).aggregate(Arrays.asList(
                match(eq("_id", programid)),
                project(fields(excludeId(), include("katas"))),
                unwind("$katas"),
                replaceRoot("$katas"),
                match(eq("_id", kataid))

        )).first().isActivated();

        if (isActivated) {
            decrementResolvedKata(kataid, programid);
            number = -1;
        } else {
            number = 1;
            incrementResolvedKata(kataid, programid);
        }

        database.getCollection("Programs").updateOne(eq("_id", programid), combine(inc("nbKata", number), set("katas.$[i].activated", !isActivated)), new UpdateOptions().arrayFilters(Arrays.asList(
                eq("i._id", kataid)
        )));
    }

    public CompletionStage<List<ProgramShowCase>> userSubscriptions(String userid) {

        CompletableFuture<List<ProgramShowCase>> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            AggregateIterable<ProgramSubscription> programids = database.getCollection("Users", ProgramSubscription.class).aggregate(Arrays.asList(
                    match(eq("_id", userid)),
                    project(
                            fields(excludeId(), include("programSubscriptions"))),
                    unwind("$programSubscriptions"),
                    project(
                            fields(excludeId(), exclude("katas"))),
                    match(eq("programSubscriptions.status", true)),
                    replaceRoot("$programSubscriptions")
            ));

            ArrayList<ProgramShowCase> prgsc = new ArrayList<>();
            MongoCollection<Program> cprograms = database.getCollection("Programs", Program.class);

            for (ProgramSubscription x : programids) {
                Program p = cprograms.find(combine(eq("_id", x.idprogram), ne("idsensei", userid))).first();
                if (!(p == null))
                    prgsc.add(new ProgramShowCase(p.getTitle(), p.getSensei(), p.getLanguage(), p.getDescription(), p.getNbKata(), p.getTags(), p.getId(), x.nbKataDone));
            }
            return prgsc;
        });

        return completableFuture;
    }

    public CompletionStage<List<ProgramShowCase>> userPrograms(String userid) {
        CompletableFuture<List<ProgramShowCase>> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            Iterable<Program> cprograms = database.getCollection("Programs", Program.class).find(eq("idsensei", userid));
            ArrayList<ProgramShowCase> prgsc = new ArrayList<>();

            for (Program p : cprograms)
                prgsc.add(new ProgramShowCase(p.getTitle(), p.getSensei(), p.getLanguage(), p.getDescription(), p.getNbKata(), p.getTags(), p.getId(), -1));
            return prgsc;
        });

        return completableFuture;
    }

    public void duplicateProgram(String programid, String newprogramid, String title) {
        Program p = database.getCollection("Programs", Program.class).find(eq("_id", programid)).first();

        File index = new File("kataDocuments/" + programid);
        if (index.exists()) {
            try {
                FileUtils.copyDirectory(index, new File("kataDocuments/" + newprogramid));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        p.set_id(newprogramid);
        p.setId(newprogramid);
        p.setTitle(title);
        create(p);
    }

    public CompletionStage<Boolean> isProgramOwner(String userid, String programid) {
        CompletableFuture<Boolean> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            try {
                database.getCollection("Programs", Program.class).find(combine(eq("idsensei", userid), eq("_id", programid))).first().getId();
                return true;
            } catch (NullPointerException e) {
                return false;
            }
        });

        return completableFuture;
    }

    public CompletionStage<Boolean> isKataOwner(String userid, String kataid, String programid) {
        CompletableFuture<Boolean> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            try {
                database.getCollection("Programs", Program.class).find(combine(eq("_id", programid), eq("idsensei", userid), eq("katas._id", kataid))).first().getId();
                return true;
            } catch (NullPointerException e) {
                return false;
            }
        });

        return completableFuture;
    }

    private boolean hasBeenSubscribed(String userid, String programid) {
        try {
            database.getCollection("Users").aggregate(Arrays.asList(
                    match(eq("_id", userid)),
                    unwind("$programSubscriptions"),
                    project(
                            fields(excludeId(), include("programSubscriptions"))),
                    match(eq("programSubscriptions.idprogram", programid)),
                    replaceRoot("$programSubscriptions"),
                    project(
                            fields(excludeId(), include("status")))
            )).iterator().next().getBoolean("status");
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /*
     CompletableFuture<> completableFuture
             = CompletableFuture.supplyAsync(() -> {

     });

         return completableFuture;*/
    public CompletionStage<Boolean> isSubscribed(String userid, String programid) {
        CompletableFuture<Boolean> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            try {
                return database.getCollection("Users").aggregate(Arrays.asList(
                        match(eq("_id", userid)),
                        unwind("$programSubscriptions"),
                        project(
                                fields(excludeId(), include("programSubscriptions"))),
                        match(eq("programSubscriptions.idprogram", programid)),
                        replaceRoot("$programSubscriptions"),
                        project(
                                fields(excludeId(), include("status")))
                )).iterator().next().getBoolean("status");
            } catch (NoSuchElementException e) {
                return false;
            }
        });

        return completableFuture;
    }

    public CompletionStage<KataSubscription> kataSubscriptionById(String kataid, String programid, String userid) {
        CompletableFuture<KataSubscription> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            AggregateIterable<KataSubscription> kata = database.getCollection("Users", KataSubscription.class).aggregate(Arrays.asList(
                    match(eq("_id", userid)),
                    unwind("$programSubscriptions"),
                    replaceRoot("$programSubscriptions"),
                    match(eq("idprogram", programid)),
                    project(fields(excludeId(), include("katas"))),
                    unwind("$katas"),
                    match(eq("katas._id", kataid)),
                    replaceRoot("$katas")

            ));
            try {
                return kata.iterator().next();
            } catch (NoSuchElementException e) {
                return new KataSubscription();
            }
        });

        return completableFuture;
    }


    public void createKataSubscription(String kataid, String programid, String userid, String status) {
        database.getCollection("Users").updateOne(eq("_id", userid), push("programSubscriptions.$[index].katas", new KataSubscription(kataid, status, "", 0)), new UpdateOptions().arrayFilters(Arrays.asList(
                eq("index.idprogram", programid)
        )));
    }

    public void incrementKataSubscriptionAttempt(String kataid, String programid, String userid) {
        database.getCollection("Users").updateOne(eq("_id", userid), inc("programSubscriptions.$[i].katas.$[j].nbAttempt", 1), new UpdateOptions().arrayFilters(Arrays.asList(
                eq("i.idprogram", programid),
                eq("j._id", kataid)
        )));
    }

    public void updateKataSubscription(String kataid, String programid, String userid, String sol, String status) {
        database.getCollection("Users").updateOne(eq("_id", userid), combine(
                set("programSubscriptions.$[i].katas.$[j].mysol", sol),
                set("programSubscriptions.$[i].katas.$[j].status", status),
                inc("programSubscriptions.$[i].nbKataDone", 1)
        ), new UpdateOptions().arrayFilters(Arrays.asList(
                eq("i.idprogram", programid),
                eq("j._id", kataid)
        )));
    }

    public void deleteProgram(String programid) {
        database.getCollection("Users").updateMany(eq("programSubscriptions.idprogram", programid), pull("programSubscriptions", new BasicDBObject("idprogram", programid)));
        database.getCollection("Programs").deleteOne(eq("_id", programid));
        File index = new File("kataDocuments/" + programid);

        // CODE COPIED FROM https://stackoverflow.com/questions/20281835/how-to-delete-a-folder-with-files-using-java MOST LIKED ANSWER (EDITED)
        if (index.exists()) {
            String[] entries = index.list();
            for (String s : entries) {
                File currentFile = new File(index.getPath(), s);
                currentFile.delete();
            }
            index.delete();
        }
    }

    public void update(String programid, ProgramShowCase p) {
        database.getCollection("Programs").updateOne(eq("_id", programid), combine(
                set("title", p.title),
                set("description", p.description),
                set("tags", p.tags),
                set("password", p.password)

        ));
    }

    public CompletionStage<String> update(Kata k, String programid) {
        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            deleteKata(k.getId(), programid);
            database.getCollection("Programs").updateOne(eq("_id", programid), push("katas", k));
            create(k,programid,k.getTitle() != "GOAL");
            return programid;

        });

        return completableFuture;
    }

    public void deleteKata(String kataid, String programid) {

        isKataActivated(kataid, programid).toCompletableFuture().thenAccept(x -> {
            if (x) {
                decrementResolvedKata(kataid, programid);
                database.getCollection("Programs").updateOne(eq("_id", programid), inc("nbKata", -1));
            }
        });

        database.getCollection("Users").updateMany(eq("programSubscriptions.katas._id", kataid), pull("programSubscriptions.$[i].katas", new BasicDBObject("_id", kataid)), new UpdateOptions().arrayFilters(Arrays.asList(
                eq("i.idprogram", programid)
        )));


        kata(kataid, programid).toCompletableFuture().thenAccept(x -> {
            new File("kataDocuments/" + programid + "/" + x.getFilename()).delete();
        });

        database.getCollection("Programs").updateOne(eq("_id", programid), pull("katas", new BasicDBObject("_id", kataid)));
    }

    public CompletionStage<Boolean> check(String programid, String password) {
        CompletableFuture<Boolean> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            Program p = database.getCollection("Programs", Program.class).find(eq("_id", programid)).first();
            return p.check(password);
        });

        return completableFuture;

    }


}
