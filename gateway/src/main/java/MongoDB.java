import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.Arrays;

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

        for (Program prg : programs.find()) {
            p.add(new ProgramShowCase(prg.getTitle(), prg.getSensei(), prg.getLanguage(), prg.getDescription(), prg.getNbKata(), prg.getTags(), prg.getId()));
        }

        return p;
    }

    public Kata getProgramKata(String programID, String kataID) {

        Kata kata = new Kata();

        MongoCollection<Program> programs = database.getCollection("Programs", Program.class);
        ArrayList<Kata> katas = programs.find(eq("_id", programID)).first().getKatas();

        for (Kata k : katas)
            if (k.getId().equals(kataID))
                kata = k;

        return kata;

    }

    public ArrayList<KataShowCase> getProgramKatasDetails(String programID) {
        ArrayList<KataShowCase> ktsc = new ArrayList<>();
        MongoCollection<Program> programs = database.getCollection("Programs", Program.class);

        ArrayList<Kata> k = programs.find(eq("_id", programID)).first().getKatas();

        for (Kata kt : k)
            ktsc.add(new KataShowCase(kt.getTitle(), kt.getDifficulty(), kt.getId(), "TODO"));

        return ktsc;
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
        Iterable<Program> programs = cprograms.find(eq(type, resource));

        for (Program prg : programs)
            p.add(new ProgramShowCase(prg.getTitle(), prg.getSensei(), prg.getLanguage(), prg.getDescription(), prg.getNbKata(), prg.getTags(), prg.getId()));

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
        Iterable<ProgramSubscription> s = database.getCollection("ProgramsSubscription", ProgramSubscription.class).find(combine(eq("iduser", userid), eq("status", true))).projection(include("idprogram"));
        ArrayList<ProgramShowCase> prgsc = new ArrayList<>();

        MongoCollection<Program> cprograms = database.getCollection("Programs", Program.class);
        for (ProgramSubscription sub : s) {
            Program p = cprograms.find(eq("_id", sub.getIdprogram())).first();
            prgsc.add(new ProgramShowCase(p.getTitle(), p.getSensei(), p.getLanguage(), p.getDescription(), p.getNbKata(), p.getTags(), p.getId()));
        }

        return prgsc;
    }

    public ArrayList<ProgramShowCase> getUserProgram(String userid) {
        Iterable<Program> cprograms = database.getCollection("Programs", Program.class).find(eq("idsensei", userid));
        ArrayList<ProgramShowCase> prgsc = new ArrayList<>();

        for (Program p : cprograms)
            prgsc.add(new ProgramShowCase(p.getTitle(), p.getSensei(), p.getLanguage(), p.getDescription(), p.getNbKata(), p.getTags(), p.getId()));

        return prgsc;
    }

    public ArrayList<KataSubscription> getKataSubscription(String programid, String userid) {
        ArrayList<KataSubscription> s = database.getCollection("ProgramsSubscription", ProgramSubscription.class).find(combine(eq("iduser", userid), eq("programid", programid))).projection(include("katas")).first().getKatas();

        return s;
    }

    public KataSubscription getKataSubscriptionByID(String kataid, String programid, String userid) {

        ProgramSubscription s = database.getCollection("ProgramsSubscription", ProgramSubscription.class).find(combine(eq("iduser", userid), eq("idprogram", programid),eq("status",true))).projection(include("katas")).first();

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
            if (k.getId().equals(kataid))
                k.setNbAttempt(k.getNbAttempt() + 1);

        }

        MongoCollection<ProgramSubscription> programs = database.getCollection("ProgramsSubscription", ProgramSubscription.class);
        programs.updateOne(combine(eq("iduser", userid), eq("idprogram", programid)), set("katas", katas));
    }

    public void updateKataSubscription(String kataid, String programid, String userid,String sol,String status) {
        ArrayList<KataSubscription> katas = database.getCollection("ProgramsSubscription", ProgramSubscription.class).find(combine(eq("iduser", userid), eq("idprogram", programid))).first().getKatas();

        for (KataSubscription k : katas) {
            if (k.getId().equals(kataid)){
                k.setStatus(status);
                k.setMysol(sol);
            }
        }

        MongoCollection<ProgramSubscription> programs = database.getCollection("ProgramsSubscription", ProgramSubscription.class);
        programs.updateOne(combine(eq("iduser", userid), eq("idprogram", programid)), set("katas", katas));
        programs.updateOne(combine(eq("iduser", userid), eq("idprogram", programid)), inc("nbKataDone",1));
    }


}
