import io.javalin.Javalin;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.lang.StringEscapeUtils;


public class app {
    public static void main(String[] args)  {

        Javalin app = Javalin.create().enableCorsForAllOrigins().start(7000);

        app.get("/run/", ctx -> {

            HttpURLConnection connection = null;
            try{
                URL compilator = new URL("http://localhost:6999");
                connection = (HttpURLConnection) compilator.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
            }catch (ConnectException e){
                e.printStackTrace();
            }

            //Send request
            String type = StringEscapeUtils.escapeJava(ctx.queryParams("language").get(0));
            String code = StringEscapeUtils.escapeJava(ctx.queryParams("code").get(0));
            String vassert = StringEscapeUtils.escapeJava(ctx.queryParams("test").get(0));

            try(DataOutputStream wr = new DataOutputStream (connection.getOutputStream())){
                wr.writeBytes("{\"language\":\""+type+"\",\"stream\":\""+code+"\",\"assert\":\""+vassert+"\"}");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }catch (ConnectException e){
                e.printStackTrace();
            }

            try(BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
                String response = in.readLine();
                ctx.result(response);
            } catch(IOException e){
                e.printStackTrace();
            }


            //String inputLine;
            //while ((inputLine = in.readLine()) != null)
            //  System.out.println(inputLine);

            // For now, we can assume we will only need to fetch one response since the compilation
            // server does send one json object with all the required data.





        });
    }

}
