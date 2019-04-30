import io.javalin.Javalin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.lang.StringEscapeUtils;


public class app {
    public static void main(String[] args)  {

        Javalin app = Javalin.create().enableCorsForAllOrigins().start(7000);

        app.get("/run/", ctx -> {


            URL compilator = new URL("http://localhost:6999");
            HttpURLConnection connection = (HttpURLConnection) compilator.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
            String type = StringEscapeUtils.escapeJava(ctx.queryParams("language").get(0));
            String code = StringEscapeUtils.escapeJava(ctx.queryParams("code").get(0));

            wr.writeBytes("{\"language\":\""+type+"\",\"stream\":\""+code+"\"}");

            wr.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            //String inputLine;
            //while ((inputLine = in.readLine()) != null)
            //  System.out.println(inputLine);

            // For now, we can assume we will only need to fetch one response since the compilation
            // server does send one json object with all the required data.
            String response = in.readLine();

            ctx.result(response);
            in.close();

        });
    }

}
