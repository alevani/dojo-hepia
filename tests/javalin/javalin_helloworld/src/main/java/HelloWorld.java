import io.javalin.Javalin;

public class HelloWorld {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        /*
        HINT : HTML file rendering like in node is possible with ctx.render()
         */
        app.get("/", ctx -> ctx.result("Hello World"));
    }
}