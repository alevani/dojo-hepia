import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;

public class compilation {
    public HashMap<String, Object> execute_kata(JSONObject input) {


        String filename = "", cmd = "", line, output = "", error = "";

        switch (input.get("language").toString()) {
            case "python":
                filename = "sample.py";
                cmd = "python " + filename;
                break;
            case "java":
                filename = "app.java";
                cmd = "java " + filename;
                break;
        }

        try (PrintWriter writer = new PrintWriter(filename, "UTF-8")) {
            writer.println(input.get("stream"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        long start,elapsed = 0;
        Process cmdProc = null;

        try {
            start = System.currentTimeMillis();
            cmdProc = Runtime.getRuntime().exec(cmd);
            cmdProc.waitFor();

            // Compute the execute time
            elapsed = System.currentTimeMillis() - start;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create Array lists that will contain output and error values of the executed program
        /**************************/

        try (BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()))) {
            while ((line = stdoutReader.readLine()) != null)
                output += line + "\n";
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader stderrReader = new BufferedReader(new InputStreamReader(cmdProc.getErrorStream()))) {
            while ((line = stderrReader.readLine()) != null)
                error += line + "\n";
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**************************/

        HashMap<String, Object> json = new HashMap<>();

        //exit value of the ran program
        json.put("exit", cmdProc.exitValue());
        json.put("output", output);
        json.put("error", error);
        json.put("time", elapsed);

        // For delete purpose, create a File object also targeting sample.py
        File file = new File(filename);
        file.delete();

        return json;
    }
}