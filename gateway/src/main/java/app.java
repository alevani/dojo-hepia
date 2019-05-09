import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Handler;
import io.javalin.Javalin;
import io.javalin.security.Role;
import javalinjwt.JWTAccessManager;
import javalinjwt.JWTGenerator;
import javalinjwt.JWTProvider;
import javalinjwt.JavalinJWT;
import javalinjwt.examples.JWTResponse;

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

    static Map<String, Role> rolesMapping = new HashMap<String, Role>();


    private static ProgramsDataBase db = new LiveDB();
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {

        // JWT
        Algorithm algorithm = Algorithm.HMAC256("dojohepia");

        JWTGenerator<MockUser> generator = (user, alg) -> {
            JWTCreator.Builder token = JWT.create()
                    .withClaim("name", user.name)
                    .withClaim("password", user.password)
                    .withClaim("level", user.level);
            return token.sign(alg);
        };

        JWTVerifier verifier = JWT.require(algorithm).build();

        JWTProvider provider = new JWTProvider(algorithm, generator, verifier);

        MockUser user1 = new MockUser("Kevin", "Monji", "Kevin");
        MockUser user2 = new MockUser("Shodai", "Shodai", "Shodai");
        MockUser user3 = new MockUser("Sensei", "Monji", "Sensei");

        Handler decodeHandler = JavalinJWT.createHeaderDecodeHandler(provider);


        // OUT
        Javalin app = Javalin.create().enableCorsForAllOrigins();

        app.before(decodeHandler);


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


            //String inputLine;
            //while ((inputLine = in.readLine()) != null)
            //  System.out.println(inputLine);

            // For now, we can assume we will only need to fetch one response since the compilation
            // server does send one json object with all the required data.

        });

        app.post("/program/create", ctx -> {
            Program prg = objectMapper.readValue(ctx.body(), Program.class);
            db.createProgram(prg);
        });

        app.post("/kata/create", ctx -> {
            Kata kt = objectMapper.readValue(ctx.body(), Kata.class);
            db.createKata(kt);
        });

        app.get("/program/getdetails", ctx -> {
            ArrayList<ProgramShowCase> prgsc = db.getProgramsDetails();
            if (prgsc != null)
                if (prgsc.size() == 0)
                    ctx.status(404);
                else
                    ctx.json(prgsc);
            else
                ctx.status(404);
        }, roles(Roles.SHODAI));

        app.get("/program/getkatas/details/:id", ctx -> {
            ArrayList<KataShowCase> ktsc = db.getProgramKatasDetails(ctx.pathParam("id"));
            ctx.json(ktsc);
        });

        app.get("/program/getkata/:prid/:id", ctx -> {

            Kata kata = db.getProgramKata(ctx.pathParam("prid"), ctx.pathParam("id"));
            if (kata.get_id() == null)
                ctx.status(404);
            else
                ctx.json(kata);
        });

        app.get("program/getdetails/:id", ctx -> {
            ArrayList<String> s = db.getProgramDetailsByID(ctx.pathParam("id"));
            if (s.size() == 0)
                ctx.status(404);
            else
                ctx.json(s);
        });

        app.get("jwt/request/:user/:password", ctx -> {
            if (user2.name.equals(ctx.pathParam("user")) && user2.password.equals(ctx.pathParam("password"))) {
                String token = provider.generateToken(user2);
                ctx.json(new JWTResponse(token));
            } else {
                ctx.json("{jwt_error:'invalid ids'}");
            }

        }, roles(Roles.ANYONE));
    }
}
