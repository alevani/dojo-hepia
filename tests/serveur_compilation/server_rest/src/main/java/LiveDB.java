import java.util.ArrayList;

public class LiveDB extends ProgramsDataBase{

    ArrayList<Program> programs;
    ArrayList<Kata> katas;


    public LiveDB() {
        programs = new ArrayList<>();


    }

    public String createProgram(Program prg) {

        programs.add(prg);
        return String.valueOf(programs.indexOf(prg));
    }

    public void createKata(Kata kata) {
        katas.add(kata);
        //programs.get(kata.getProgramID()).setKata();
    //    programs.get(kata.getProgramID()).getKata().add(kata);
    }

    public ArrayList<Program> getAllPrograms(){
        return programs;
    }

}
