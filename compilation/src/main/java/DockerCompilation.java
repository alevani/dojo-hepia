import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;

public class DockerCompilation {
    public HashMap<String, Object> execute_kata(JSONObject input) {


        String filename = "", filename_test="todeleteifitstayslikethis", cmd = "", line, output = "", error = "";

        switch (input.get("language").toString()) {
            case "python":
                filename = "share_docker_file/sample.py";
                filename_test = "share_docker_file/assert.py";
                cmd = "docker run --rm --mount type=bind,source=/Users/freak/Desktop/dojo-hepia/compilation/share_docker_file,dst=/env/ hey:1.1 python3 assert.py";
                break;
            case "java":
                filename = "share_docker_file/kata.java";
                filename_test = "share_docker_file/Main.java";
                cmd = "docker run --rm --mount type=bind,source=/Users/freak/Desktop/dojo-hepia/compilation/share_docker_file,dst=/env/ java:4.0 ./java_test.sh";
                break;
        }

        try (PrintWriter writer = new PrintWriter(filename, "UTF-8")) {
            writer.println(input.get("stream"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try(PrintWriter writer = new PrintWriter(filename_test,"UTF-8")){
            writer.println(input.get("assert"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        long start, elapsed = 0;
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

        file = new File(filename_test);
        file.delete();

        return json;
    }
}
