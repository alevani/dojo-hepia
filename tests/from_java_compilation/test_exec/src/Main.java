import java.io.*;

public class Main {

    // For now, all exceptions are thrown (for code visibility), but in the future, i must implement try catch.
    public static void main(String[] args) throws IOException, InterruptedException {

        // Create a PrintWriter object targeting sample.py
        PrintWriter writer = createPython3Writer();

        // For delete purpose, create a File object also targeting sample.py
        File file = new File("sample.py");

        // This simulate a bitfield / ascii field received from the rest server

        // Simple file
        //writer.println("#!/usr/bin/env python3\n# -*- coding: cp1252 -*-\nresult = 1+2\nprint(result)");

        // Complex file with tabulation structure for test purpose
        writer.println("def avg(marks):\n   assert len(marks) != 0\n   return sum(marks)/len(marks)\nmark1 = []\nprint('Average of mark1:',avg(mark1))");

        // Close the writer, the file has been filled and is ready to be executed
        writer.close();


        Process cmdProc = Runtime.getRuntime().exec("python sample.py");
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
            System.out.println(line);
        }

        int retValue = cmdProc.exitValue();

        System.out.println("exit value : " + retValue);
        file.delete();
    }

    public static PrintWriter createPython3Writer() throws FileNotFoundException, UnsupportedEncodingException {
        return  new PrintWriter("sample.py", "UTF-8");
    }


}
