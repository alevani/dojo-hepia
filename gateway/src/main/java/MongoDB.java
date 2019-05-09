import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import java.util.ArrayList;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDB extends ProgramsDataBase {
    private MongoClient mongoClient;
    private MongoDatabase database;

    CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    public MongoDB() {
        this.mongoClient = new MongoClient("localhost",MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        this.database = mongoClient.getDatabase("DojoHepia");
        /*MongoCollection<Program> programs = database.getCollection("Programs",Program.class);
        programs.drop();*/
}

    public void createProgram(Program prg) {
        MongoCollection<Program> programs = database.getCollection("Programs",Program.class);
        programs.insertOne(prg);
    }

    public void createKata(Kata kata) {
        MongoCollection<Program> programs = database.getCollection("Programs",Program.class);

        //programs.updateOne((ClientSession) new BasicDBObject().put("_id",kata.getProgramID()), );
        for (Program p : programs.find())
            if (p.getId().equals(kata.getProgramID())) {
                p.setNbKata(p.getNbKata() + 1);
                p.getKatas().add(kata);
                break;
            }

    }

    public ArrayList<ProgramShowCase> getProgramsDetails() {
        ArrayList<ProgramShowCase> p = new ArrayList<>();
        MongoCollection<Program> programs = database.getCollection("Programs",Program.class);

        for (Program prg : programs.find()) {
            p.add(new ProgramShowCase(prg.getTitle(), prg.getSensei(), prg.getLanguage(), prg.getDescription(), prg.getNbKata(), prg.getTags(), prg.getId()));
        }

        return p;
    }

    public Kata getProgramKata(String programID, String kataID) {

      return null;

    }

    public ArrayList<KataShowCase> getProgramKatasDetails(String programID) {
        ArrayList<KataShowCase> ktsc = new ArrayList<>();
        ArrayList<Kata> kt = new ArrayList<>();

        MongoCollection<Program> programs = database.getCollection("Programs",Program.class);

        for (Program prg : programs.find())
            if (prg.getId().equals(programID)) {
                kt = prg.getKatas();
                break;
            }

        for (Kata k : kt)
            ktsc.add(new KataShowCase(k.getTitle(), k.getDifficulty(), k.getId(), "TODO"));
        return ktsc;
    }

    public ArrayList<String> getProgramDetailsByID(String id) {
        ArrayList<String> infos = new ArrayList<>();

        MongoCollection<Program> programs = database.getCollection("Programs",Program.class);
        //MongoCollection<Program> programs = database.getCollection("Programs",Program.class).find(eq("id",id)).first();

        for (Program p : programs.find())
           if(p.getId().equals(id)){
                infos.add(p.getTitle());
                infos.add(p.getLanguage());
                infos.add(p.getSensei());
                break;
            }

        return infos;
    }
}
