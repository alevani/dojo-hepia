package ch.hepia.repository;

import ch.hepia.model.user.User;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Users implements UserInterface {

    private MongoClient mongoClient;
    private MongoDatabase database;

    CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    public Users() {
        MongoCredential credential = MongoCredential.createCredential("shodai", "DojoHepia", "shodai".toCharArray());
        this.mongoClient = new MongoClient(new ServerAddress("localhost", 27017), Arrays.asList(credential), MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        this.database = mongoClient.getDatabase("DojoHepia");
    }

    public void create(User u) {
        CompletableFuture.supplyAsync(() -> {
            database.getCollection("Users", User.class).insertOne(u);
            return null;
        });
    }

    public User checkUserCredentials(String username, String password) {

        return database.getCollection("Users", User.class).find(combine(eq("username", username), eq("password", password))).first();

    }

    public CompletionStage<Boolean> isExisting(String username) {
        CompletableFuture<Boolean> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            User u = database.getCollection("Users", User.class).find(eq("username", username)).first();
            if (u == null)
                return false;
            else
                return true;
        });

        return completableFuture;
    }
}
