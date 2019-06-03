import ch.hepia.repository.Programs;
import ch.hepia.repository.Users;
import ch.hepia.model.kata.*;

import ch.hepia.model.program.*;
import ch.hepia.model.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
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
public class App {

    // Roles of the routes
    enum Roles implements Role {
        SHODAI,   // Super  role
        SENSEI,   // High   role
        MONJI,    // low    role
        ANYONE    // null   role
    }

    // Data base object, can be changed if your object extends "MongoDataBase.ProgramsDataBase"
    private static Users dbUsers = new Users();
    private static Programs dbPrograms = new Programs();

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

        // Every time a ch.hepia.database.modal.user is requesting a token, it passes trough here
        // It generates a token based on ch.hepia.database.modal.user's name and his level
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

        // Every time a ch.hepia.database.modal.user tries to reach a route of the server, it passes trough the before handler.
        // This ensure the ch.hepia.database.modal.user has a compatible token to reach routes, if the token is faked or deprecated, it returns a 401 status (Unauthorized)
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
         *  Create a ch.hepia.database.modal.program -> Expect complete object like Program.Program in body
         */
        app.post("/program/create", ctx -> {
            Program program = objectMapper.readValue(ctx.body(), Program.class);
            dbPrograms.create(program);
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        app.get("/program/details", ctx -> {
            Optional<List<ProgramShowCase>> prgsc = dbPrograms.programsDetails();

            if (prgsc.isPresent())
                ctx.json(prgsc.get());
            else
                ctx.status(404);

        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("program/details/:id", ctx -> {
            Optional<ProgramShowCase> s = dbPrograms.programDetailsById(ctx.pathParam("id"));
            if (s.isPresent())
                ctx.json(s.get());
            else
                ctx.status(404);

        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("/program/update", ctx -> {
            JSONObject input = new JSONObject(ctx.body());
            ProgramShowCase program = objectMapper.readValue(input.get("program").toString(), ProgramShowCase.class);
            dbPrograms.update(input.getString("programid"), program);
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        app.post("program/delete", ctx -> {
            dbPrograms.deleteProgram(new JSONObject(ctx.body()).getString("programid"));
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        app.post("program/duplicate", ctx -> {
            JSONObject input = new JSONObject(ctx.body());
            dbPrograms.duplicateProgram(input.getString("programid"), input.getString("newprogramid"), input.getString("newtitle"));
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        /******************/


        /** KATAS **/

        app.get("/kata/isactivated/:kataid/:programid", ctx -> {
            boolean isActivated = dbPrograms.isKataActivated(ctx.pathParam("kataid"), ctx.pathParam("programid"));
            ctx.status(200).json(isActivated);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("/kata/update", ctx -> {
            JSONObject input = new JSONObject(ctx.body());
            Kata kata = objectMapper.readValue(input.getString("kata"), Kata.class);
            String programid = dbPrograms.update(kata, input.getString("programid"));
            ctx.status(200).json(programid);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        app.post("kata/toggleactivation", ctx -> {
            JSONObject input = new JSONObject(ctx.body());
            dbPrograms.toggleKataActivation(input.getString("kid"), input.getString("pid"));
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        app.post("/kata/create", ctx -> {
            JSONObject input = new JSONObject(ctx.body());
            Kata kata = objectMapper.readValue(input.getString("kata"), Kata.class);
            dbPrograms.create(kata, input.getString("programid"), input.getBoolean("goal"));
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        app.get("/kata/details/:programid/:userid", ctx -> {
            Optional<List<KataShowCase>> ktsc = dbPrograms.kataDetails(ctx.pathParam("programid"), ctx.pathParam("userid"));
            ctx.json(ktsc.get());
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("/kata/:kataid/:programid", ctx -> {

            Optional<Kata> kata = dbPrograms.kata(ctx.pathParam("kataid"), ctx.pathParam("programid"));
            if (kata.isPresent())
                ctx.json(kata.get());
            else
                ctx.status(404);

        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("/kata/isowner/:programid/:kataid/:userid", ctx -> {
            boolean isOwner = dbPrograms.isKataOwner(ctx.pathParam("userid"), ctx.pathParam("kataid"), ctx.pathParam("programid"));
            ctx.status(200).json(isOwner);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));


        app.post("/kata/delete/", ctx -> {
            JSONObject input = new JSONObject(ctx.body());
            dbPrograms.deleteKata(input.getString("kid"), input.getString("pid"));
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        /******************/

        /** USER **/

        app.post("/user/tokenrequest/", ctx -> {
            JSONObject ids = new JSONObject(ctx.body());

            Optional<User> user = dbUsers.checkUserCredentials(ids.getString("username"), ids.getString("password"));

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
            Optional<List<ProgramShowCase>> p = dbPrograms.programDetailsFiltered(ctx.pathParam("type"), ctx.pathParam("resource"));
            if (p.isPresent())
                ctx.json(p.get());
            else
                ctx.status(404).json("No ch.hepia.database.modal.program matched the specified query");

        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        /******************/


        /** PROGRAM SUBSCRIPTION **/

        app.get("/program/checkpassword/:programid/:password", ctx -> {
            ctx.status(200).json(dbPrograms.check(ctx.pathParam("programid"), ctx.pathParam("password")));
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("/program/issubscribed/:userid/:programid", ctx -> {
            boolean isSubscribed = dbPrograms.isSubscribed(ctx.pathParam("userid"), ctx.pathParam("programid"));
            ctx.status(200).json(isSubscribed);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("/program/isowner/:userid/:programid", ctx -> {
            boolean isOwner = dbPrograms.isProgramOwner(ctx.pathParam("userid"), ctx.pathParam("programid"));
            ctx.status(200).json(isOwner);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("program/subscription/:programid/:userid", ctx -> {
            Optional<ProgramSubscription> p = dbPrograms.subscriptionByID(ctx.pathParam("userid"), ctx.pathParam("programid"));
            if (p.isPresent())
                ctx.json(p.get());
            else {
                ctx.status(404);
            }
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("program/subscription/:userid", ctx -> {
            Optional<List<ProgramShowCase>> prgsc = dbPrograms.userSubscriptions(ctx.pathParam("userid"));
            if (prgsc.isPresent())
                ctx.json(prgsc.get());
            else
                ctx.status(404);

        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("/program/:userid", ctx -> {
            Optional<List<ProgramShowCase>> prgsc = dbPrograms.userPrograms(ctx.pathParam("userid"));

            if (prgsc.isPresent())
                ctx.json(prgsc.get());
            else
                ctx.status(404);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        app.post("program/createsubscription", ctx -> {
            JSONObject obj = new JSONObject(ctx.body());
            ProgramSubscription programSubscription = objectMapper.readValue(obj.getString("obj"), ProgramSubscription.class);
            dbPrograms.create(obj.getString("userid"), programSubscription);
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("program/togglesubscription", ctx -> {
            JSONObject input = new JSONObject(ctx.body());
            dbPrograms.toggleSubscription(input.getString("userid"), input.getString("programid"));
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        /******************/

        /** KATA SUBSCRIPTION **/

        app.get("kata/get/subscriptioninfos/:userid/:programid/:kataid", ctx -> {
            Optional<KataSubscription> k = dbPrograms.kataSubscriptionById(ctx.pathParam("kataid"), ctx.pathParam("programid"), ctx.pathParam("userid"));

            if (k.isPresent())
                ctx.status(200).json(k.get());
            else
                ctx.status(404);

        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("kata/create/subscription", ctx -> {
            JSONObject obj = new JSONObject(ctx.body());
            dbPrograms.createKataSubscription(obj.getString("kataid"), obj.getString("programid"), obj.getString("userid"), obj.getString("status"));
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("kata/inc/subscription", ctx -> {
            JSONObject obj = new JSONObject(ctx.body());
            dbPrograms.incrementKataSubscriptionAttempt(obj.getString("kataid"), obj.getString("programid"), obj.getString("userid"));
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("kata/update/subscription", ctx -> {
            JSONObject obj = new JSONObject(ctx.body());
            dbPrograms.updateKataSubscription(obj.getString("kataid"), obj.getString("programid"), obj.getString("userid"), obj.getString("sol"), obj.getString("status"));
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        /******************/

        /** SIGN IN **/

        app.get("token/generate/:level/:time", ctx -> {

            Algorithm algotoken = Algorithm.HMAC256("tochange");
            String token = JWT.create()
                    .withClaim("exp", new Date())
                    .withClaim("level", ctx.pathParam("level"))
                    .withClaim("selectedExp", Integer.valueOf(ctx.pathParam("time")) * 60)
                    .sign(algotoken);
            ctx.json(token);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        app.post("user/signin", ctx -> {

            JSONObject input = new JSONObject(ctx.body());
            String username = input.getString("username");
            String password = input.getString("password");
            String id = input.getString("id");
            if (dbUsers.isExisting(username)) {
                ctx.status(400).json("Username '" + username + "' already exists");
            } else {

                try {
                    DecodedJWT decodedJWT = JWT.decode(input.getString("token"));

                    int time = decodedJWT.getClaim("selectedExp").asInt();
                    String level = decodedJWT.getClaim("level").asString();

                    Algorithm algotoken = Algorithm.HMAC256("tochange");
                    JWTVerifier checker = JWT.require(algotoken)
                            .acceptExpiresAt(time)
                            .build(); //Reusable verifier instance

                    checker.verify(input.getString("token"));
                    dbUsers.create(new User(id, username, level, password));
                    ctx.status(200);
                } catch (JWTVerificationException exception) {
                    ctx.status(400).json("Bad token");
                }
            }
        }, roles(Roles.ANYONE));
        /******************/
    }

}
