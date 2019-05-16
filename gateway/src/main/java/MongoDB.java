import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Projections.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDB extends ProgramsDataBase {
    private MongoClient mongoClient;
    private MongoDatabase database;

    CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    public MongoDB() {
        MongoCredential credential = MongoCredential.createCredential("shodai", "DojoHepia", "shodai".toCharArray());
        this.mongoClient = new MongoClient(new ServerAddress("localhost", 27017), Arrays.asList(credential), MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        this.database = mongoClient.getDatabase("DojoHepia");
        database.getCollection("Programs").createIndex(Indexes.ascending("title"));

        if (!doUserExists("shodai")) {
            createUser(new MockUser("0", "shodai", "shodai", "d033e22ae348aeb5660fc2140aec35850c4da997"));
        }

    }

    public void createProgram(Program prg) {
        MongoCollection<Program> programs = database.getCollection("Programs", Program.class);
        programs.insertOne(prg);
    }

    public void createKata(Kata kata) {
        ArrayList<Kata> katas = database.getCollection("Programs", Program.class).find(eq("_id", kata.getProgramID())).first().getKatas();
        katas.add(kata);

        MongoCollection<Program> programs = database.getCollection("Programs", Program.class);
        programs.updateOne(eq("_id", kata.getProgramID()), combine(inc("nbKata", 1), set("katas", katas)));
    }

    public ArrayList<ProgramShowCase> getProgramsDetails() {
        ArrayList<ProgramShowCase> p = new ArrayList<>();
        MongoCollection<Program> programs = database.getCollection("Programs", Program.class);

        for (Program prg : programs.find())
            p.add(new ProgramShowCase(prg.getTitle(), prg.getSensei(), prg.getLanguage(), prg.getDescription(), prg.getNbKata(), prg.getTags(), prg.getId()));

        return p;
    }

    public Kata getProgramKata(String programID, String kataID) {

        Kata kata = new Kata();

        MongoCollection<Program> programs = database.getCollection("Programs", Program.class);
        ArrayList<Kata> katas = programs.find(eq("_id", programID)).first().getKatas();

        for (Kata k : katas)
            if (k.getId().equals(kataID)) {
                kata = k;
                break;
            }
        return kata;

    }

    public ArrayList<KataShowCase> getProgramKatasDetails(String programID, String userid) {
        ArrayList<KataShowCase> ktsc = new ArrayList<>();
        MongoCollection<Program> programs = database.getCollection("Programs", Program.class);

        ArrayList<Kata> k = programs.find(eq("_id", programID)).first().getKatas();

        k.forEach(x -> {
            ktsc.add(new KataShowCase(x.getTitle(), x.getDifficulty(), x.getId(), getKataStatus(x.getId(), programID, userid)));
        });

        return ktsc;
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

        ArrayList<KataSubscription> k = subscription.getKatas();

        for (KataSubscription ks : k) {
            if (ks.getId().equals(kataid))
                return ks.getStatus();
        }
        return "TODO";
    }

    public ArrayList<String> getProgramDetailsByID(String id) {
        ArrayList<String> infos = new ArrayList<>();

        MongoCollection<Program> programs = database.getCollection("Programs", Program.class);
        Program p = programs.find(eq("_id", id)).first();

        if (p != null) {
            infos.add(p.getTitle());
            infos.add(p.getLanguage());
            infos.add(p.getSensei());
            infos.add(p.getIdsensei());
        }
        return infos;
    }

    public ArrayList<ProgramShowCase> getProgramDetailsByResource(String type, String resource) {
        ArrayList<ProgramShowCase> p = new ArrayList<>();

        MongoCollection<Program> cprograms = database.getCollection("Programs", Program.class);
        Pattern regex = Pattern.compile(resource, Pattern.CASE_INSENSITIVE);
        Iterable<Program> programs = cprograms.find(eq(type, regex));


        programs.forEach(x -> {
            p.add(new ProgramShowCase(x.getTitle(), x.getSensei(), x.getLanguage(), x.getDescription(), x.getNbKata(), x.getTags(), x.getId(), -1));
        });

        return p;
    }

    public ProgramSubscription getSubscriptionByID(String userid, String idrogram) {
        MongoCollection<ProgramSubscription> programSubs = database.getCollection("ProgramsSubscription", ProgramSubscription.class);
        ProgramSubscription prgsub = programSubs.find(combine(eq("idprogram", idrogram), eq("iduser", userid))).first();

        return prgsub;
    }

    public void createProgramSubscritpion(ProgramSubscription p) {
        MongoCollection<ProgramSubscription> programSubs = database.getCollection("ProgramsSubscription", ProgramSubscription.class);
        programSubs.insertOne(p);
    }

    public void toggleSubscription(String userid, String idrogram) {
        MongoCollection<ProgramSubscription> programSubs = database.getCollection("ProgramsSubscription", ProgramSubscription.class);
        ProgramSubscription prog = programSubs.find(combine(eq("idprogram", idrogram), eq("iduser", userid))).first();

        if (prog.getStatus())
            programSubs.updateOne(combine(eq("idprogram", idrogram), eq("iduser", userid)), set("status", false));
        else
            programSubs.updateOne(combine(eq("idprogram", idrogram), eq("iduser", userid)), set("status", true));

    }

    public ArrayList<ProgramShowCase> getUserSubscription(String userid) {
        Iterable<ProgramSubscription> s = database.getCollection("ProgramsSubscription", ProgramSubscription.class).find(combine(eq("iduser", userid), eq("status", true))).projection(include("idprogram", "nbKataDone"));
        ArrayList<ProgramShowCase> prgsc = new ArrayList<>();

        MongoCollection<Program> cprograms = database.getCollection("Programs", Program.class);
        s.forEach(x -> {
            Program p = cprograms.find(eq("_id", x.getIdprogram())).first();
            prgsc.add(new ProgramShowCase(p.getTitle(), p.getSensei(), p.getLanguage(), p.getDescription(), p.getNbKata(), p.getTags(), p.getId(), x.getNbKataDone()));
        });

        return prgsc;
    }

    public ArrayList<ProgramShowCase> getUserProgram(String userid) {
        Iterable<Program> cprograms = database.getCollection("Programs", Program.class).find(eq("idsensei", userid));
        ArrayList<ProgramShowCase> prgsc = new ArrayList<>();

        for (Program p : cprograms)
            prgsc.add(new ProgramShowCase(p.getTitle(), p.getSensei(), p.getLanguage(), p.getDescription(), p.getNbKata(), p.getTags(), p.getId(), -1));

        return prgsc;
    }

    public ArrayList<KataSubscription> getKataSubscription(String programid, String userid) {
        ArrayList<KataSubscription> s = database.getCollection("ProgramsSubscription", ProgramSubscription.class).find(combine(eq("iduser", userid), eq("programid", programid))).projection(include("katas")).first().getKatas();

        return s;
    }

    public KataSubscription getKataSubscriptionByID(String kataid, String programid, String userid) {
/*
        ProgramSubscription p = database.getCollection("ProgramsSubscription", ProgramSubscription.class).find(combine(eq("iduser", userid), eq("idprogram", programid), eq("status", true),eq("katas._id",kataid ))).filter(();
        return p == null ? new KataSubscription() : p.getKatas().get(0);
  */
        ProgramSubscription s = database.getCollection("ProgramsSubscription", ProgramSubscription.class).find(combine(eq("iduser", userid), eq("idprogram", programid), eq("status", true))).projection(include("katas")).first();
        if (s == null) {
            return new KataSubscription();
        } else
            for (KataSubscription k : s.getKatas()) {

                if (k.getId().equals(kataid))
                    return k;
            }
        return null;
    }

    public void createKataSubscription(String kataid, String programid, String userid) {
        ArrayList<KataSubscription> katas = database.getCollection("ProgramsSubscription", ProgramSubscription.class).find(combine(eq("iduser", userid), eq("idprogram", programid))).first().getKatas();
        katas.add(new KataSubscription(kataid, "ON-GOING", "", 0));

        MongoCollection<ProgramSubscription> programs = database.getCollection("ProgramsSubscription", ProgramSubscription.class);
        programs.updateOne(combine(eq("iduser", userid), eq("idprogram", programid)), set("katas", katas));
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
    public void incKataSubscriptionAttempt(String kataid, String programid, String userid) {
        ArrayList<KataSubscription> katas = database.getCollection("ProgramsSubscription", ProgramSubscription.class).find(combine(eq("iduser", userid), eq("idprogram", programid))).first().getKatas();

        for (KataSubscription k : katas) {
            if (k.getId().equals(kataid)) {
                k.setNbAttempt(k.getNbAttempt() + 1);
                break;
            }
        }

        MongoCollection<ProgramSubscription> programs = database.getCollection("ProgramsSubscription", ProgramSubscription.class);
        programs.updateOne(combine(eq("iduser", userid), eq("idprogram", programid)), set("katas", katas));
    }

    public void updateKataSubscription(String kataid, String programid, String userid, String sol, String status) {
        ArrayList<KataSubscription> katas = database.getCollection("ProgramsSubscription", ProgramSubscription.class).find(combine(eq("iduser", userid), eq("idprogram", programid))).first().getKatas();

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

    public void deleteProgram(String programid) {
        database.getCollection("Programs", Program.class).deleteMany(eq("_id", programid));
        database.getCollection("ProgramsSubscription", ProgramSubscription.class).deleteMany(eq("idprogram", programid));
    }

    /*
        public void deletekatas(String kataid, String programid){

            database.getCollection("ProgramsSubscription",ProgramSubscription.class).deleteMany();//
            database.getCollection("Programs",Program.class).deleteMany(eq("_id",programid));
        }
    */

    public void createUser(MockUser u) {
        database.getCollection("Users", MockUser.class).insertOne(u);
    }

    public MockUser checkUser(String username, String password) {
        MockUser u = database.getCollection("Users", MockUser.class).find(combine(eq("username", username), eq("password", password))).first();
        if (u == null)
            return null;
        return u;
    }

    public boolean doUserExists(String username) {
        MockUser u = database.getCollection("Users", MockUser.class).find(eq("username", username)).first();
        if (u == null)
            return false;
        return true;
    }

    //§public void deleteKata()

}
