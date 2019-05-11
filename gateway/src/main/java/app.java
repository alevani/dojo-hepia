import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
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
import java.util.HashMap;
import java.util.Map;

import static io.javalin.security.SecurityUtil.roles;

public class app {

    enum Roles implements Role {
        SHODAI,
        MONJI,
        SENSEI,
        ANYONE
    }

    private static ProgramsDataBase db = new MongoDB();
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static ArrayList<MockUser> users = new ArrayList<>();

    public static void main(String[] args) {

        // JWT
        Algorithm algorithm = Algorithm.HMAC256("dojohepia");

        JWTGenerator<MockUser> generator = (user, alg) -> {
            JWTCreator.Builder token = JWT.create()
                    .withClaim("username", user.name)
                    .withClaim("level", user.level);
            return token.sign(alg);
        };

        JWTVerifier verifier = JWT.require(algorithm).build();

        JWTProvider provider = new JWTProvider(algorithm, generator, verifier);

        users.add(new MockUser(0, "monji", "monji", "monji"));
        users.add(new MockUser(1, "shodai", "shodai", "shodai"));
        users.add(new MockUser(2, "sensei", "sensei", "sensei"));

        Handler decodeHandler = JavalinJWT.createHeaderDecodeHandler(provider);


        // OUT
        Javalin app = Javalin.create().enableCorsForAllOrigins();

        app.before(decodeHandler);

        Map<String, Role> rolesMapping = new HashMap<String, Role>();

        rolesMapping.put("shodai", Roles.SHODAI);
        rolesMapping.put("anyone", Roles.ANYONE);
        rolesMapping.put("monji", Roles.MONJI);
        rolesMapping.put("sensei", Roles.SENSEI);

        JWTAccessManager accessManager = new JWTAccessManager("level", rolesMapping, Roles.ANYONE);
        app.accessManager(accessManager);
        app.start(7000);
        // END OF JWT

        app.post("/run/", ctx -> {

            HttpURLConnection connection = null;

            try {
                URL compilator = new URL("http://localhost:6999");
                connection = (HttpURLConnection) compilator.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
            } catch (ConnectException e) {
                e.printStackTrace();
            }

            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.writeBytes(ctx.body());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ConnectException e) {
                e.printStackTrace();
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String response = in.readLine();
                ctx.result(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("/program/create", ctx -> {
            Program prg = objectMapper.readValue(ctx.body(), Program.class);
            db.createProgram(prg);
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI));


        app.post("program/createsubscription", ctx -> {
            ProgramSubscription prg = objectMapper.readValue(ctx.body(), ProgramSubscription.class);
            db.createProgramSubscritpion(prg);
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("program/togglesubscription", ctx -> {
            JSONObject input = new JSONObject(ctx.body());
            db.toggleSubscription(String.valueOf(input.get("userid")), String.valueOf(input.get("programid")));
            ctx.status(200);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("/kata/create", ctx -> {
            Kata kt = objectMapper.readValue(ctx.body(), Kata.class);
            db.createKata(kt);
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

        app.get("/program/getkatas/details/:id", ctx -> {
            ArrayList<KataShowCase> ktsc = db.getProgramKatasDetails(ctx.pathParam("id"));
            ctx.json(ktsc);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("/program/getkata/:prid/:id", ctx -> {

            Kata kata = db.getProgramKata(ctx.pathParam("prid"), ctx.pathParam("id"));
            if (kata.getId() == null)
                ctx.status(404);
            else
                ctx.json(kata);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("program/getdetails/:id", ctx -> {
            ArrayList<String> s = db.getProgramDetailsByID(ctx.pathParam("id"));
            if (s.size() == 0)
                ctx.status(404);
            else
                ctx.json(s);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.post("jwt/request/", ctx -> {
            MockUser u = checkUser(ctx);

            if (!(u == null)) {
                String token = provider.generateToken(u);

                HashMap<String, String> p = new HashMap<>();
                p.put("id", String.valueOf(u.id));
                p.put("username", u.name);
                p.put("token", token);
                p.put("role", u.level);

                ctx.json(p);
            } else {
                ctx.status(400).json("Username or password is incorrect");
            }

        }, roles(Roles.ANYONE));

        app.get("search/:type/:resource", ctx -> {
            ArrayList<ProgramShowCase> p = db.getProgramDetailsByResource(ctx.pathParam("type"), ctx.pathParam("resource"));

            if (p != null)
                if (p.size() == 0)
                    ctx.status(404);
                else
                    ctx.json(p);
            else
                ctx.status(404);
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));

        app.get("program/getsubscription/:programid/:userid", ctx -> {
            ProgramSubscription p = db.getSubscriptionByID(ctx.pathParam("userid"), ctx.pathParam("programid"));
            if (!(p == null))
                ctx.json(p);
            else {
                ctx.status(404);
            }
        }, roles(Roles.SHODAI, Roles.SENSEI, Roles.MONJI));
    }

    public static MockUser checkUser(Context ctx) {
        JSONObject ids = new JSONObject(ctx.body());

        for (MockUser u : users)
            if (u.name.equals(ids.get("username")) && u.password.equals(ids.get("password")))
                return u;
        return null;
    }
}
