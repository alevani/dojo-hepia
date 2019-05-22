import com.mongodb.*;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;

import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.*;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Projections.*;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDB implements ProgramsDataBase {

    private MongoClient mongoClient;
    private MongoDatabase database;

    CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    public MongoDB() {
        MongoCredential credential = MongoCredential.createCredential("shodai", "DojoHepia", "shodai".toCharArray());
        this.mongoClient = new MongoClient(new ServerAddress("localhost", 27017), Arrays.asList(credential), MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        this.database = mongoClient.getDatabase("DojoHepia");
        database.getCollection("Programs").createIndex(Indexes.ascending("title"));

        if (!isExisting("shodai")) {
            create(new User("0", "shodai", "shodai", "d033e22ae348aeb5660fc2140aec35850c4da997"));
        }

    }

    public void create(Program program) {
        database.getCollection("Programs", Program.class).insertOne(program);
    }

    public void create(Kata kata) {
        database.getCollection("Programs").updateOne(eq("_id", kata.getProgramID()), combine(inc("nbKata", 1), push("katas", kata)));
    }

    public Optional<List<ProgramShowCase>> programsDetails() {
        ArrayList<ProgramShowCase> p = new ArrayList<>();
        MongoCollection<Program> programs = database.getCollection("Programs", Program.class);

        // TODO voir si il y a pas moyen que je puisse aggregate et generer un programshowcase avec les project

        for (Program prg : programs.find())
            p.add(new ProgramShowCase(prg.getTitle(), prg.getSensei(), prg.getLanguage(), prg.getDescription(), prg.getNbKata(), prg.getTags(), prg.getId()));

        if (p == null)
            return Optional.empty();
        else if (p.size() == 0)
            return Optional.empty();
        else return Optional.of(p);
    }

    public Optional<Kata> kata(String kataid) {
        AggregateIterable<Kata> kata = database.getCollection("Programs", Kata.class).aggregate(Arrays.asList(
                unwind("$katas"),
                project(
                        fields(excludeId(), include("katas"))),
                match(eq("katas._id", kataid)),
                replaceRoot("$katas")
        ));

        if (kata.iterator().hasNext())
            return Optional.of(kata.iterator().next());
        else
            return Optional.empty();
    }

    public Optional<List<KataShowCase>> kataDetails(String programid, String userid) {

        AggregateIterable<Kata> kata = database.getCollection("Programs", Kata.class).aggregate(Arrays.asList(
                unwind("$katas"),
                match(eq("_id", programid)),
                replaceRoot("$katas")
        ));

        ArrayList<KataShowCase> ktsc = new ArrayList<>();
        for (Kata x : kata)
            ktsc.add(new KataShowCase(x.getTitle(), x.getDifficulty(), x.getId(), getKataStatus(x.getId(), programid, userid)));

        return Optional.of(ktsc);
    }

    private String getKataStatus(String kataid, String programid, String userid) {

        if (isSubscribed(userid, programid)) {
            AggregateIterable<KataSubscription> kata = database.getCollection("Users", KataSubscription.class).aggregate(Arrays.asList(
                    match(combine(eq("_id", userid), eq("programSubscriptions.katas._id", kataid))),
                    unwind("$programSubscriptions"),
                    replaceRoot("$programSubscriptions"),
                    unwind("$katas"),
                    match(eq("katas._id", kataid)),
                    replaceRoot("$katas"),
                    project(fields(excludeId(), include("status")))
            ));

            try {
                return kata.iterator().next().getStatus();
            }catch (NoSuchElementException e){
                return "TODO";
            }


        } else
            return "TODO";
    }

    public Optional<List<String>> programDetailsById(String id) {
        ArrayList<String> infos = new ArrayList<>();

        try {
            Program program = database.getCollection("Programs", Program.class).aggregate(Arrays.asList(match(eq("_id", id)))).iterator().next();
            infos.add(program.getTitle());
            infos.add(program.getLanguage());
            infos.add(program.getSensei());
            infos.add(program.getIdsensei());
            return Optional.of(infos);
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    public Optional<List<ProgramShowCase>> programDetailsFiltered(String type, String resource) {
        ArrayList<ProgramShowCase> p = new ArrayList<>();

        MongoCollection<Program> cprograms = database.getCollection("Programs", Program.class);
        Pattern regex = Pattern.compile(resource, Pattern.CASE_INSENSITIVE);
        Iterable<Program> programs = cprograms.find(eq(type, regex));


        programs.forEach(x -> {
            p.add(new ProgramShowCase(x.getTitle(), x.getSensei(), x.getLanguage(), x.getDescription(), x.getNbKata(), x.getTags(), x.getId(), -1));
        });

        if (p == null)
            return Optional.empty();
        else if (p.size() == 0)
            return Optional.empty();
        else return Optional.of(p);
    }

    public Optional<ProgramSubscription> subscriptionByID(String userid, String idrogram) {
        AggregateIterable<ProgramSubscription> prgsub = database.getCollection("Users", ProgramSubscription.class).aggregate(Arrays.asList(
                match(eq("_id", userid)),
                unwind("$programSubscriptions"),
                project(
                        fields(excludeId(), include("programSubscriptions"))),
                match(eq("programSubscriptions.idprogram", idrogram)),
                replaceRoot("$programSubscriptions")
        ));

        if (prgsub.iterator().hasNext())
            return Optional.of(prgsub.iterator().next());
        else
            return Optional.empty();

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

    public Optional<List<ProgramShowCase>> userSubscriptions(String userid) {

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
            Program p = cprograms.find(eq("_id", x.idprogram)).first();
            prgsc.add(new ProgramShowCase(p.getTitle(), p.getSensei(), p.getLanguage(), p.getDescription(), p.getNbKata(), p.getTags(), p.getId(), x.nbKataDone));
        }
        if (prgsc == null)
            return Optional.empty();
        else if (prgsc.size() == 0)
            return Optional.empty();
        else return Optional.of(prgsc);
    }

    public Optional<List<ProgramShowCase>> userPrograms(String userid) {
        Iterable<Program> cprograms = database.getCollection("Programs", Program.class).find(eq("idsensei", userid));
        ArrayList<ProgramShowCase> prgsc = new ArrayList<>();

        for (Program p : cprograms)
            prgsc.add(new ProgramShowCase(p.getTitle(), p.getSensei(), p.getLanguage(), p.getDescription(), p.getNbKata(), p.getTags(), p.getId(), -1));

        if (prgsc == null)
            return Optional.empty();
        else if (prgsc.size() == 0)
            return Optional.empty();
        else return Optional.of(prgsc);
    }

    public boolean isSubscribed(String userid, String programid) {
        try{
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
        }catch (NoSuchElementException e){
            return false;
        }
    }

    public Optional<KataSubscription> kataSubscriptionById(String kataid, String programid, String userid) {

        AggregateIterable<KataSubscription> kata = database.getCollection("Users", KataSubscription.class).aggregate(Arrays.asList(
                match(combine(eq("_id", userid), eq("programSubscriptions.katas._id", kataid))),
                unwind("$programSubscriptions"),
                replaceRoot("$programSubscriptions"),
                project(fields(excludeId(), include("katas"))),
                unwind("$katas"),
                match(eq("katas._id", kataid)),
                replaceRoot("$katas")

        ));

        if (kata.iterator().hasNext())
            return Optional.of(kata.iterator().next());
        else
            return Optional.empty();
    }


    public void createKataSubscription(String kataid, String programid, String userid) {
        database.getCollection("Users").updateOne(eq("_id", userid), push("programSubscriptions.$[index].katas", new KataSubscription(kataid, "ON-GOING", "", 0)), new UpdateOptions().arrayFilters(Arrays.asList(
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
    }

    public void create(User u) {
        database.getCollection("Users", User.class).insertOne(u);
    }

    public Optional<User> checkUserCredentials(String username, String password) {
        User u = database.getCollection("Users", User.class).find(combine(eq("username", username), eq("password", password))).first();

        if (u == null)
            return Optional.empty();
        else
            return Optional.of(u);
    }

    public void deleteKata(String kataid) {
        database.getCollection("Users").updateMany(eq("programSubscriptions.katas._id", kataid), pull("programSubscriptions.$[].katas", new BasicDBObject("_id", kataid)));

        // TODO not working
        /*database.getCollection("Users").updateMany(eq("programSubscriptions.katas._id", kataid), inc("programSubscriptions.$[i].nbKataDone", -1),new UpdateOptions().arrayFilters(Arrays.asList(
                eq("i.$[].katas.status","RESOLVED")
        )));*/
        database.getCollection("Programs").updateOne(eq("katas._id", kataid), pull("katas", new BasicDBObject("_id", kataid)));
    }

    public boolean isExisting(String username) {
        User u = database.getCollection("Users", User.class).find(eq("username", username)).first();
        if (u == null)
            return false;
        return true;
    }

}
