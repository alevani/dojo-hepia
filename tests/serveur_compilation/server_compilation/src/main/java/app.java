import io.javalin.Javalin;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringEscapeUtils;


public class app {
    public static void main(String[] args) {

        Javalin app = Javalin.create().enableCorsForAllOrigins().start(6999);

        app.post("/", ctx -> {

            JSONObject input = new JSONObject(ctx.body());

            PrintWriter writer = null;
            File file = null;

            String filename = "";
            String cmd = "";

            if (input.get("language").equals("python")) {
                // Create a PrintWriter object targeting sample.py

                filename = "sample.py";
                writer = new PrintWriter(filename, "UTF-8");
                cmd = "python " + filename;

            } else if (input.get("language").equals("java")) {
                // Create a PrintWriter object targeting sample.py
                filename = "app.java";
                writer = new PrintWriter(filename, "UTF-8");
                cmd = "java " + filename;
            }

            // For delete purpose, create a File object also targeting sample.py
            file = new File(filename);

            // Complex file with tabulation structure for test purpose
            writer.println(input.get("stream"));

            // Close the writer, the file has been filled and is ready to be executed
            writer.close();

            long start = System.currentTimeMillis();
            Process cmdProc = Runtime.getRuntime().exec(cmd);
            cmdProc.waitFor();
            long elapsed = System.currentTimeMillis() - start;

            // Create Array lists that will contain output and error values of the executed program
            /**************************/

            String line, output = "", error = "";
            BufferedReader stdoutReaderl = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
            while ((line = stdoutReader.readLine()) != null)
                output += line + "\n";

            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(cmdProc.getErrorStream()));
            while ((line = stderrReader.readLine()) != null)
                error += line + "\n";

            /**************************/

            HashMap<String, Object> json = new HashMap<>();

            //exit value of the ran program
            json.put("exit", cmdProc.exitValue());
            json.put("output", output);
            json.put("error", error);
            json.put("time", elapsed);

            ctx.json(json);

            file.delete();
        });
    }
}
