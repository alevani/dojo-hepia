import io.javalin.Javalin;
import org.json.JSONObject;

public class app {
    public static void main(String[] args) {

        Javalin app = Javalin.create().enableCorsForAllOrigins().start(6999);

        app.post("/", ctx -> {
            compilation cpl = new compilation();
            ctx.json(cpl.execute_kata(new JSONObject(ctx.body())));
        });
    }
}