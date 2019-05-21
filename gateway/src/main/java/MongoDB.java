import com.mongodb.*;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;

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

    /**
     * TODO Vraiment très couteux........
     *
     * @param kataid
     * @param programid
     * @param userid
     * @return
     */

    private String getKataStatus(String kataid, String programid, String userid) {
        MongoCollection<ProgramSubscription> programs = database.getCollection("ProgramsSubscription", ProgramSubscription.class);
        ProgramSubscription subscription = programs.find(combine(eq("iduser", userid), eq("idprogram", programid))).first();

        if (subscription == null)
            return "TODO";

        ArrayList<KataSubscription> k = subscription.katas;

        for (KataSubscription ks : k) {
            if (ks.getId().equals(kataid))
                return ks.getStatus();
        }
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

        database.getCollection("Users").updateOne(combine(eq("programSubscriptions.idprogram", idprogram), eq("_id", userid)), set("programSubscriptions.0.status", !status.getBoolean("status")));
    }

    public Optional<List<ProgramShowCase>> userSubscriptions(String userid) {

        AggregateIterable<ProgramSubscription> programids = database.getCollection("Users", ProgramSubscription.class).aggregate(Arrays.asList(
                match(combine(eq("_id", userid), eq("programSubscriptions.status", true))),
                unwind("$programSubscriptions"),
                project(
                        fields(excludeId(), exclude("katas"))),
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
// TODO
    public KataSubscription kataSubscriptionById(String kataid, String programid, String userid) {

        ProgramSubscription s = database.getCollection("ProgramsSubscription", ProgramSubscription.class).find(combine(eq("iduser", userid), eq("idprogram", programid), eq("status", true))).projection(include("katas")).first();
        if (s == null) {
            return new KataSubscription();
        } else
            for (KataSubscription k : s.katas) {

                if (k.getId().equals(kataid))
                    return k;
            }
        return null;
    }
// TODO
    public void createKataSubscription(String kataid, String programid, String userid) {
        database.getCollection("ProgramsSubscription").updateOne(combine(eq("iduser", userid), eq("idprogram", programid)), push("katas", new KataSubscription(kataid, "ON-GOING", "", 0)));
    }

    /**
     * TODO trouver un moyen simple de rentrer dans la liste de kata avec mongo db, parce que la c'est vraiment très couteu à chaque appel.
     * TODO voir si je peux pas transformer kataSubrription en une mongo collection dans ProgramSub.. et pourquoi pas
     * TODO si 4a marche changer Arraylist kata en Mongo collection kata dans Program
     *
     * @param kataid
     * @param programid
     * @param userid
     */
    public void incrementKataSubscriptionAttempt(String kataid, String programid, String userid) {
        ArrayList<KataSubscription> katas = database.getCollection("ProgramsSubscription", ProgramSubscription.class).find(combine(eq("iduser", userid), eq("idprogram", programid))).first().katas;

        for (KataSubscription k : katas) {
            if (k.getId().equals(kataid)) {
                k.setNbAttempt(k.getNbAttempt() + 1);
                break;
            }
        }

        MongoCollection<ProgramSubscription> programs = database.getCollection("ProgramsSubscription", ProgramSubscription.class);
        programs.updateOne(combine(eq("iduser", userid), eq("idprogram", programid)), set("katas", katas));
    }
// TODO
    public void updateKataSubscription(String kataid, String programid, String userid, String sol, String status) {
        ArrayList<KataSubscription> katas = database.getCollection("ProgramsSubscription", ProgramSubscription.class).find(combine(eq("iduser", userid), eq("idprogram", programid))).first().katas;

        for (KataSubscription k : katas) {
            if (k.getId().equals(kataid)) {
                k.setStatus(status);
                k.setMysol(sol);
                break;
            }
        }

        MongoCollection<ProgramSubscription> programs = database.getCollection("ProgramsSubscription", ProgramSubscription.class);
        programs.updateOne(combine(eq("iduser", userid), eq("idprogram", programid)), set("katas", katas));
        if (status.equals("RESOLVED"))
            programs.updateOne(combine(eq("iduser", userid), eq("idprogram", programid)), inc("nbKataDone", 1));
    }
// TODO
    public void deleteProgram(String programid) {
        database.getCollection("Programs").deleteMany(eq("_id", programid));
        database.getCollection("ProgramsSubscription").deleteMany(eq("idprogram", programid));
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

    public boolean isExisting(String username) {
        User u = database.getCollection("Users", User.class).find(eq("username", username)).first();
        if (u == null)
            return false;
        return true;
    }

}
