import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;

/**
 * First version of the compilation server
 */
public class HostCompilation {

    /**
     * This method expect a json input which contains at least key 'language' and key 'code'
     * @param input JSON object sent by the front-end
     * @return stdout output of the executed file
     * @throws IOException // for code visibility purpose
     */
    public HashMap<String, Object> hostcompilation(JSONObject input) throws IOException, InterruptedException {

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
        BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
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

        file.delete();

        return json;
    }
}

