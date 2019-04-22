import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Process cmdProc = Runtime.getRuntime().exec("java hello.java");
        cmdProc.waitFor();

        BufferedReader stdoutReader = new BufferedReader(
                new InputStreamReader(cmdProc.getInputStream()));
        String line;
        while ((line = stdoutReader.readLine()) != null) {
            // process procs standard output here
            System.out.println("program output value : " + line);
        }

        BufferedReader stderrReader = new BufferedReader(
                new InputStreamReader(cmdProc.getErrorStream()));
        while ((line = stderrReader.readLine()) != null) {
            // process procs standard error here
            System.out.println("program error output value : " + line);
        }

        int retValue = cmdProc.exitValue();

        System.out.println("exit value : " + retValue);
    }
}
