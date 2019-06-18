import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DockerCompilation {

    private Map<String, Map<String, String>> filenameDic = new HashMap<>();

    public DockerCompilation() {

        String userDir = System.getProperty("user.dir");
        Map<String, String> python = new HashMap<>();
        python.put("filename", "share_docker_file/sample.py");
        python.put("test", "share_docker_file/assert.py");
        python.put("cmd", "docker run --rm --mount type=bind,source=" + userDir + "/share_docker_file,dst=/env/ freakency/python:3.0 python assert.py");

        Map<String, String> java = new HashMap<>();

        java.put("filename", "share_docker_file/kata.java");
        java.put("test", "share_docker_file/Main.java");
        java.put("cmd", "docker run --rm --mount type=bind,source=" + userDir + "/share_docker_file,dst=/env/ freakency/java:1.0 ./java_test.sh");

        this.filenameDic.put("python", python);
        this.filenameDic.put("java", java);
    }

    public Map<String, Object> execute_kata(JSONObject input) {


        String line, output = "", error = "";

        Map<String, String> language = this.filenameDic.get(input.get("language").toString());

        try (PrintWriter filenameWriter = new PrintWriter(language.get("filename"), "UTF-8"); PrintWriter filenameTestWriter = new PrintWriter(language.get("test"), "UTF-8")) {
            filenameWriter.println(input.get("stream"));
            filenameTestWriter.println(input.get("assert"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        long start, elapsed = 0;
        Process cmdProc = null;

        try {
            start = System.currentTimeMillis();
            cmdProc = Runtime.getRuntime().exec(language.get("cmd"));
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

        try (BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(cmdProc.getInputStream())); BufferedReader stderrReader = new BufferedReader(new InputStreamReader(cmdProc.getErrorStream()))) {
            while ((line = stdoutReader.readLine()) != null)
                output += line + "\n";
            while ((line = stderrReader.readLine()) != null)
                error += line + "\n";
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**************************/

        Map<String, Object> json = new HashMap<>();

        //exit value of the ran program
        if (!error.equals(""))
            json.put("exit", 1);
        else
            json.put("exit", 0);
        json.put("output", output);
        json.put("error", error);
        json.put("time", elapsed);

        // For delete purpose, create a File object also targeting sample.py
        File file = new File(language.get("filename"));
        file.delete();

        file = new File(language.get("test"));
        file.delete();

        return json;
    }
}
