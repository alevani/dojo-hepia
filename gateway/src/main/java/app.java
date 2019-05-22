import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Handler;
import io.javalin.Javalin;
import io.javalin.security.Role;
import javalinjwt.JWTAccessManager;
import javalinjwt.JWTGenerator;
import javalinjwt.JWTProvider;
import javalinjwt.JavalinJWT;
import org.json.JSONObject;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static io.javalin.security.SecurityUtil.roles;

/**
 * Gateway of DojoHepia
 * Created by Alexandre Vanini for Hepia © HEPIA 2019 (Bachelor Thesis project)
 * Purpose : Http Server
 */
public class app {

    // Roles of the routes
    enum Roles implements Role {
        SHODAI,   // Super user role
        SENSEI,   // High user role
        MONJI,    // low user role
        ANYONE    // null user role
    }

    // Data base object, can be changed if your object extends "ProgramsDataBase"
    private static ProgramsDataBase db = new MongoDB();

    // Jackson Object mapper, convert received stream into Java Designed Object
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Entry point of the gateway, no args needed
     *
     * @param args - Not needed
     */
    public static void main(String[] args) {

        // JWT                                   //secret
        Algorithm algorithm = Algorithm.HMAC256("cd47488e09fa86d6e2549b824cb1f47422921539");

        // Every time a user is requesting a token, it passes trough here
        // It generates a token based on user's name and his level
        JWTGenerator<User> generator = (user, alg) -> {
            JWTCreator.Builder token = JWT.create()
                    .withClaim("username", user.getUsername())
                    .withClaim("level", user.getLevel())
                    .withClaim("exp", new Date());
            return token.sign(alg);
        };

        // Make the token expire after six days
        JWTVerifier verifier = JWT.require(algorithm).acceptExpiresAt(518400).build();
        JWTProvider provider = new JWTProvider(algorithm, generator, verifier);

        // Decoder Handler
        Handler decodeHandler = JavalinJWT.createHeaderDecodeHandler(provider);


        // Javalin server creation
        Javalin app = Javalin.create().enableCorsForAllOrigins();

        // Every time a user tries to reach a route of the server, it passes trough the before handler.
        // This ensure the user has a compatible token to reach routes, if the token is faked or deprecated, it returns a 401 status (Unauthorized)
        app.before(decodeHandler);

        Map<String, Role> rolesMapping = new HashMap<String, Role>();

        rolesMapping.put("shodai", Roles.SHODAI);
        rolesMapping.put("anyone", Roles.ANYONE);
        rolesMapping.put("monji", Roles.MONJI);
        rolesMapping.put("sensei", Roles.SENSEI);


        // Tells the access manager that, ANYONE is the most basic role.
        JWTAccessManager accessManager = new JWTAccessManager("level", rolesMapping, Roles.ANYONE);
        app.accessManager(accessManager);

        // Start the server at port 7000
        app.start(7000);
        // END OF JWT


        /**
         * Compilation route - body must contain data to compile
         */
        app.post("/kata/run/", ctx -> {

            HttpURLConnection connection = null;

            try {
                // Create a new connection on compilation server (port 6999)
                URL compilator = new URL("http://localhost:6999");
                connection = (HttpURLConnection) compilator.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
            } catch (ConnectException e) {
                e.printStackTrace();
            }

            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                // Write body content into http post
                wr.writeBytes(ctx.body());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ConnectException e) {
                e.printStackTrace();
            }

            // Reads the result got from the compilation server
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String response = in.readLine();
                ctx.result(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // This tells that /run/ is only accessible by users who have these roles
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        /** PROGRAM **/

        /**
         *  Create a program -> Expect complete object like Program in body
         */
        app.post("/program/create", ctx -> {
            Program program = objectMapper.readValue(ctx.body(), Program.class);
            db.create(program);
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        app.get("/program/details", ctx -> {
            Optional<List<ProgramShowCase>> prgsc = db.programsDetails();

            if (prgsc.isPresent())
                ctx.json(prgsc.get());
            else
                ctx.status(404);

        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("program/details/:id", ctx -> {
            Optional<List<String>> s = db.programDetailsById(ctx.pathParam("id"));

            if (s.isPresent())
                ctx.json(s.get());
            else
                ctx.status(404);

        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("/kata/create", ctx -> {
            Kata kata = objectMapper.readValue(ctx.body(), Kata.class);
            db.create(kata);
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        app.post("program/delete", ctx -> {
            db.deleteProgram(new JSONObject(ctx.body()).getString("programid"));
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        /******************/


        /** KATAS **/

        app.get("/kata/details/:programid/:userid", ctx -> {
            Optional<List<KataShowCase>> ktsc = db.kataDetails(ctx.pathParam("programid"), ctx.pathParam("userid"));
            ctx.json(ktsc.get());
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("/kata/:kataid", ctx -> {

            Optional<Kata> kata = db.kata(ctx.pathParam("kataid"));
            if (kata.isPresent())
                ctx.json(kata.get());
            else
                ctx.status(404);

        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));


        app.post("/kata/delete/", ctx -> {
            db.deleteKata(ctx.body());
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        /******************/

        /** USER **/

        app.post("jwt/request/", ctx -> {
            JSONObject ids = new JSONObject(ctx.body());

            Optional<User> user = db.checkUserCredentials(ids.getString("username"), ids.getString("password"));

            if (user.isPresent()) {
                User u = user.get();
                String token = provider.generateToken(u);

                HashMap<String, String> p = new HashMap<>();
                p.put("id", String.valueOf(u.getId()));
                p.put("username", u.getUsername());
                p.put("token", token);
                p.put("role", u.getLevel());

                ctx.json(p);
            } else {
                ctx.status(400).json("Username or password is incorrect");
            }

        }, roles(Roles.ANYONE));

        /******************/

        /** PROGRAM SEARCH **/
        app.get("program/search/:type/:resource", ctx -> {
            Optional<List<ProgramShowCase>> p = db.programDetailsFiltered(ctx.pathParam("type"), ctx.pathParam("resource"));
            if (p.isPresent())
                ctx.json(p.get());
            else
                ctx.status(404).json("No program matched the specified query");

        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        /******************/


        /** PROGRAM SUBSCRIPTION **/

        app.get("/program/issubscribed/:userid/:programid", ctx -> {
            boolean isSubscribed = db.isSubscribed(ctx.pathParam("userid"), ctx.pathParam("programid"));
            ctx.status(200).json(isSubscribed);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("program/subscription/:programid/:userid", ctx -> {
            Optional<ProgramSubscription> p = db.subscriptionByID(ctx.pathParam("userid"), ctx.pathParam("programid"));
            if (p.isPresent())
                ctx.json(p.get());
            else {
                ctx.status(404);
            }
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("program/subscription/:userid", ctx -> {
            Optional<List<ProgramShowCase>> prgsc = db.userSubscriptions(ctx.pathParam("userid"));
            if (prgsc.isPresent())
                ctx.json(prgsc.get());
            else
                ctx.status(404);

        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("/program/:userid", ctx -> {
            Optional<List<ProgramShowCase>> prgsc = db.userPrograms(ctx.pathParam("userid"));

            if (prgsc.isPresent())
                ctx.json(prgsc.get());
            else
                ctx.status(404);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        app.post("program/createsubscription", ctx -> {
            JSONObject obj = new JSONObject(ctx.body());
            ProgramSubscription programSubscription = objectMapper.readValue(obj.getString("obj"), ProgramSubscription.class);
            db.create(obj.getString("userid"), programSubscription);
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("program/togglesubscription", ctx -> {
            JSONObject input = new JSONObject(ctx.body());
            db.toggleSubscription(input.getString("userid"), input.getString("programid"));
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        /******************/

        /** KATA SUBSCRIPTION **/

        app.get("kata/get/subscriptioninfos/:userid/:programid/:kataid", ctx -> {
            Optional<KataSubscription> k = db.kataSubscriptionById(ctx.pathParam("kataid"), ctx.pathParam("programid"), ctx.pathParam("userid"));

            if (k.isPresent())
                ctx.status(200).json(k.get());
            else
                ctx.status(404);

        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("kata/create/subscription", ctx -> {
            JSONObject obj = new JSONObject(ctx.body());
            db.createKataSubscription(obj.getString("kataid"), obj.getString("programid"), obj.getString("userid"));
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("kata/inc/subscription", ctx -> {
            JSONObject obj = new JSONObject(ctx.body());
            db.incrementKataSubscriptionAttempt(obj.getString("kataid"), obj.getString("programid"), obj.getString("userid"));
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("kata/update/subscription", ctx -> {
            JSONObject obj = new JSONObject(ctx.body());
            db.updateKataSubscription(obj.getString("kataid"), obj.getString("programid"), obj.getString("userid"), obj.getString("sol"), obj.getString("status"));
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        /******************/

        /** SIGN IN **/

        app.get("token/generate", ctx -> {
            Algorithm algotoken = Algorithm.HMAC256("tochange");
            String token = JWT.create()
                    .withClaim("exp", new Date())
                    .sign(algotoken);
            ctx.json(token);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        app.post("user/signin", ctx -> {
            JSONObject input = new JSONObject(ctx.body());
            String username = input.getString("username");
            String password = input.getString("password");
            String id = input.getString("id");
            if (db.isExisting(username)) {
                ctx.status(400).json("Username '" + username + "' already exists");
            } else {
                if (!input.get("token").equals("")) {
                    try {
                        Algorithm algotoken = Algorithm.HMAC256("tochange");
                        JWTVerifier checker = JWT.require(algotoken)
                                .acceptExpiresAt(600)
                                .build(); //Reusable verifier instance

                        checker.verify(input.getString("token"));
                        db.create(new User(id, username, "sensei", password));
                        ctx.status(200);
                    } catch (JWTVerificationException exception) {
                        ctx.status(400).json("Bad token");
                    }
                } else {
                    db.create(new User(id, username, "monji", password));
                    ctx.status(200);
                }

            }
        }, roles(Roles.ANYONE));
        /******************/
    }

}
