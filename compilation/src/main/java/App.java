import io.javalin.Javalin;
import org.json.JSONObject;

public class App {
    public static void main(String[] args) {

        Javalin app = Javalin.create().enableCorsForAllOrigins().start(6999);
        DockerCompilation cpl = new DockerCompilation();
        app.post("/", ctx -> {
            ctx.json(cpl.executeKata(new JSONObject(ctx.body())));
        });
    }
}
