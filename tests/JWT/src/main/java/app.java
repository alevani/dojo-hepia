import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.Handler;
import io.javalin.Javalin;
import io.javalin.security.Role;
import javalinjwt.JWTAccessManager;
import javalinjwt.JWTGenerator;
import javalinjwt.JWTProvider;
import javalinjwt.JavalinJWT;
import javalinjwt.examples.JWTResponse;

import java.util.HashMap;
import java.util.Map;

import static io.javalin.security.SecurityUtil.roles;

public class app {
    enum Roles implements Role {
        ANYONE,
        USER,
        ADMIN
    }
    public static void main(String[] args) {



        Algorithm algorithm = Algorithm.HMAC256("bush did the 9/11");

//2.
        JWTGenerator<MockUser> generator = (user, alg) -> {
            JWTCreator.Builder token = JWT.create()
                    .withClaim("name", user.name)
                    .withClaim("level", user.level);
            return token.sign(alg);
        };

//3.
        JWTVerifier verifier = JWT.require(algorithm).build();

//4.
        JWTProvider provider = new JWTProvider(algorithm, generator, verifier);

        Handler decodeHandler = JavalinJWT.createHeaderDecodeHandler(provider);



        Map<String, Role> rolesMapping = new HashMap<String, Role>() ;


        Javalin app = Javalin.create().enableCorsForAllOrigins();

        JWTAccessManager accessManager = new JWTAccessManager("level", rolesMapping, Roles.ANYONE);
        app.accessManager(accessManager);

        app.start(7000);

        app.before(decodeHandler);

        Handler generateHandler = context -> {
            MockUser mockUser = new MockUser("Mocky McMockface", "user");
            String token = provider.generateToken(mockUser);
            context.json(new JWTResponse(token));
        };

        Handler validateHandler = context -> {
            DecodedJWT decodedJWT = JavalinJWT.getDecodedFromContext(context);
            context.result("Hi " + decodedJWT.getClaim("name").asString());
        };

        app.get("/generate",  generateHandler, roles(Roles.ANYONE));
        app.get("/validate", validateHandler, roles(Roles.USER, Roles.ADMIN));
        app.get("/adminslounge", validateHandler, roles(Roles.ADMIN));
    }

}
