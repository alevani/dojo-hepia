import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class app {

    private static ProgramsDataBase db = new LiveDB();
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {

        Javalin app = Javalin.create().enableCorsForAllOrigins().start(7000);

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
            ctx.json(db.getProgramsDetails());
        });

        app.get("/program/getkatas/details/:id", ctx -> {
            ctx.json(db.getProgramKatasDetails(ctx.pathParam("id")));
        });

        app.get("/program/getkata/:prid/:id", ctx -> {
            ctx.json(db.getProgramKata(ctx.pathParam("prid"),ctx.pathParam("id")));
        });

    }

}
