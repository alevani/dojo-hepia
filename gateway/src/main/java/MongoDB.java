import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
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
}
