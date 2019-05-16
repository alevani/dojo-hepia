import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Context;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        MONJI,    // Middle user role
        ANYONE    // Low user role
    }

    // Data base object, can be changed if your object extends "ProgramsDataBase"
    private static ProgramsDataBase db = new MongoDB();

    // Jackson Object mapper, convert received stream into Java Designed Object
    private static ObjectMapper objectMapper = new ObjectMapper();

    // Temporary list of users
    private static ArrayList<MockUser> users = new ArrayList<>();

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
        JWTGenerator<MockUser> generator = (user, alg) -> {
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
        app.post("/run/", ctx -> {

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
            Program prg = objectMapper.readValue(ctx.body(), Program.class);
            db.createProgram(prg);
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        app.get("/program/getdetails", ctx -> {
            ArrayList<ProgramShowCase> prgsc = db.getProgramsDetails();
            if (prgsc != null)
                if (prgsc.size() == 0)
                    ctx.status(404);
                else
                    ctx.json(prgsc);
            else
                ctx.status(404);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("program/getdetails/:id", ctx -> {
            ArrayList<String> s = db.getProgramDetailsByID(ctx.pathParam("id"));
            if (s.size() == 0)
                ctx.status(404);
            else
                ctx.json(s);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("/kata/create", ctx -> {
            Kata kt = objectMapper.readValue(ctx.body(), Kata.class);
            db.createKata(kt);
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        app.post("program/delete", ctx -> {
            db.deleteProgram(new JSONObject(ctx.body()).getString("programid"));
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        /******************/


        /** KATAS **/

        app.get("/program/getkatas/details/:id/:userid", ctx -> {
            ArrayList<KataShowCase> ktsc = db.getProgramKatasDetails(ctx.pathParam("id"), ctx.pathParam("userid"));
            ctx.json(ktsc);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("/program/getkata/:prid/:id", ctx -> {

            Kata kata = db.getProgramKata(ctx.pathParam("prid"), ctx.pathParam("id"));
            if (kata.getId() == null)
                ctx.status(404);
            else
                ctx.json(kata);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        /******************/

        /** USER **/

        app.post("jwt/request/", ctx -> {
            JSONObject ids = new JSONObject(ctx.body());
            MockUser u = db.checkUser(ids.getString("username"), ids.getString("password"));

            if (!(u == null)) {
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
        app.get("search/:type/:resource", ctx -> {
            ArrayList<ProgramShowCase> p = db.getProgramDetailsByResource(ctx.pathParam("type"), ctx.pathParam("resource"));

            if (p != null)
                if (p.size() == 0)
                    ctx.status(404).json("No program matched the specified query");
                else
                    ctx.json(p);
            else
                ctx.status(404).json("No program matched the specified query");
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        /******************/


        /** PROGRAM SUBSCRIPTION **/

        app.get("program/getsubscription/:programid/:userid", ctx -> {
            ProgramSubscription p = db.getSubscriptionByID(ctx.pathParam("userid"), ctx.pathParam("programid"));
            if (!(p == null))
                ctx.json(p);
            else {
                ctx.status(404);
            }
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("/subscription/get/:userid", ctx -> {
            ArrayList<ProgramShowCase> prgsc = db.getUserSubscription(ctx.pathParam("userid"));
            if (prgsc != null)
                if (prgsc.size() == 0)
                    ctx.status(404);
                else
                    ctx.json(prgsc);
            else
                ctx.status(404);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("/subscription/mine/:userid", ctx -> {
            ArrayList<ProgramShowCase> prgsc = db.getUserProgram(ctx.pathParam("userid"));
            if (prgsc != null)
                if (prgsc.size() == 0)
                    ctx.status(404);
                else
                    ctx.json(prgsc);
            else
                ctx.status(404);
        }, roles(Roles.SHODAI, Roles.SENSEI));

        app.post("program/createsubscription", ctx -> {
            ProgramSubscription prg = objectMapper.readValue(ctx.body(), ProgramSubscription.class);
            db.createProgramSubscritpion(prg);
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
            KataSubscription k = db.getKataSubscriptionByID(ctx.pathParam("kataid"), ctx.pathParam("programid"), ctx.pathParam("userid"));

            if (!(k == null))
                if (k.getId() == null)
                    ctx.status(402);
                else
                    ctx.status(200).json(k);
            else if (k == null)
                ctx.status(404);


        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("kata/create/subscription", ctx -> {
            JSONObject obj = new JSONObject(ctx.body());
            db.createKataSubscription(obj.getString("kataid"), obj.getString("programid"), obj.getString("userid"));
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("kata/inc/subscription", ctx -> {
            JSONObject obj = new JSONObject(ctx.body());
            db.incKataSubscriptionAttempt(obj.getString("kataid"), obj.getString("programid"), obj.getString("userid"));
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
            if (db.doUserExists(username)) {
                ctx.status(400).json("Username '" + username + "' already exists");
            } else {
                if (!input.get("token").equals("")) {
                    try {
                        Algorithm algotoken = Algorithm.HMAC256("tochange");
                        JWTVerifier checker = JWT.require(algotoken)
                                .acceptExpiresAt(600)
                                .build(); //Reusable verifier instance

                        checker.verify(input.getString("token"));
                        db.createUser(new MockUser(id, username, "sensei", password));
                        ctx.status(200);
                    } catch (JWTVerificationException exception) {
                        ctx.status(400).json("Bad token");
                    }
                } else {
                    db.createUser(new MockUser(id, username, "monji", password));
                    ctx.status(200);
                }

            }
        }, roles(Roles.ANYONE));
        /******************/
    }

}
