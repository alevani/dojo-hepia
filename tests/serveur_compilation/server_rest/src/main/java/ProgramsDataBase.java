import java.util.ArrayList;

public abstract class ProgramsDataBase {
    public abstract String createProgram(Program prg);
    public abstract void createKata(Kata kata);
    public abstract ArrayList<Program> getAllPrograms();
}
