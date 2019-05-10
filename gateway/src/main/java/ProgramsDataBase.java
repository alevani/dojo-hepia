import java.util.ArrayList;

public abstract class ProgramsDataBase {
    public abstract void createProgram(Program prg);
    public abstract void createKata(Kata kata);
    public abstract ArrayList<ProgramShowCase> getProgramsDetails();
    public abstract Kata getProgramKata(String programID, String kataID);
    public abstract ArrayList<KataShowCase> getProgramKatasDetails(String programID);
    public abstract ArrayList<String> getProgramDetailsByID(String id);
    public abstract ArrayList<ProgramShowCase> getProgramDetailsByResource(String type, String resource);
}
