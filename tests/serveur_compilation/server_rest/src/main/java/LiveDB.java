import java.util.ArrayList;

public class LiveDB extends ProgramsDataBase{

    ArrayList<Program> programs;


    public LiveDB() {
        programs = new ArrayList<>();
    }

    public String createProgram(Program prg) {

        programs.add(prg);
        return String.valueOf(programs.indexOf(prg));
    }

    public void addKata(Kata kata) {
        //programs.get(kata.getProgramID()).setKata();
        programs.get(kata.getProgramID()).getKata().add(kata);
        int caca = 12;
    }

}
