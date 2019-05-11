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

        //this.mongoClient = new MongoClient("http://admin:pass@localhost", MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        this.database = mongoClient.getDatabase("DojoHepia");
        /*MongoCollection<Program> programs = database.getCollection("Programs",Program.class);
        programs.drop();*/
    }

    public void createProgram(Program prg) {
        MongoCollection<Program> programs = database.getCollection("Programs", Program.class);
        programs.insertOne(prg);
    }

    public void createKata(Kata kata) {
        MongoCollection<Program> programs = database.getCollection("Programs", Program.class);
        ArrayList katas = new ArrayList();

        for (Program p : programs.find())
            if (p.getId().equals(kata.getProgramID())) {
                katas = p.getKatas();
                break;
            }

        katas.add(kata);
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


    // [iduser, idprogram : 234, status : 1 , katas [{id:1,status:"resolved",mysol:".."}],done : 1]
    // ou
    // separer en deux tables ,plus simple surement pour les requetes
}
