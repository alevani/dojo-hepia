import java.util.ArrayList;

public class LiveDB extends ProgramsDataBase {

    ArrayList<Kata> katas;

    ArrayList<Program> programs;

    public LiveDB() {
        this.programs = new ArrayList<>();
        this.katas = new ArrayList<>();
    }

    public void createProgram(Program prg) {
        this.programs.add(prg);
    }

    public void createKata(Kata kata) {

        for (Program p : this.programs)
            if (p.getId().equals(kata.getProgramID())) {
                p.setNbKata(p.getNbKata() + 1);
                p.getKatas().add(kata);
                break;
            }
    }

    public ArrayList<ProgramShowCase> getProgramsDetails() {
        ArrayList<ProgramShowCase> p = new ArrayList<>();

        for (Program prg : this.programs)
            p.add(new ProgramShowCase(prg.getTitle(), prg.getSensei(), prg.getLanguage(), prg.getDescription(), prg.getNbKata(), prg.getTags(), prg.getId()));

        return p;
    }

    public Kata getProgramKata(String programID, String kataID) {
        ArrayList<Kata> ktemp = new ArrayList<>();
        Kata kata = new Kata();
        for (Program prg : this.programs)
            if (prg.getId().equals(programID))
                ktemp = prg.getKatas();
            for(Kata k : ktemp)
                if(k.getId().equals(kataID))
                    kata = k;
        return kata;
    }

    public ArrayList<KataShowCase> getProgramKatasDetails(String programID, String userid) {
        ArrayList<KataShowCase> ktsc = new ArrayList<>();
        ArrayList<Kata> kt = new ArrayList<>();
        for (Program prg : this.programs)
            if (prg.getId().equals(programID)) {
                kt = prg.getKatas();
                break;
            }

        for (Kata k : kt)
            ktsc.add(new KataShowCase(k.getTitle(), k.getDifficulty(), k.getId(), "TODO"));
        return ktsc;
    }

    public ArrayList<String> getProgramDetailsByID(String id){
        ArrayList<String> infos = new ArrayList<>();

        for (Program p : this.programs)
            if(p.getId().equals(id)){
                infos.add(p.getTitle());
                infos.add(p.getLanguage());
                infos.add(p.getSensei());
                break;
            }

        return infos;
    }

    @Override
    public ArrayList<ProgramShowCase> getProgramDetailsByResource(String type, String resource) {
        return null;
    }

    @Override
    public ProgramSubscription getSubscriptionByID(String userid, String idrogram) {
        return null;
    }

    @Override
    public void createProgramSubscritpion(ProgramSubscription p) {

    }

    @Override
    public void toggleSubscription(String userid, String idrogram) {

    }

    @Override
    public ArrayList<ProgramShowCase> getUserSubscription(String userid) {
        return null;
    }

    @Override
    public ArrayList<ProgramShowCase> getUserProgram(String userid) {
        return null;
    }

    @Override
    public ArrayList<KataSubscription> getKataSubscription(String programid, String userid) {
        return null;
    }

    @Override
    public KataSubscription getKataSubscriptionByID(String kataid, String programid, String userid) {
        return null;
    }

    @Override
    public void createKataSubscription(String kataid, String programid, String userid) {

    }

    @Override
    public void incKataSubscriptionAttempt(String kataid, String programid, String userid) {

    }

    @Override
    public void updateKataSubscription(String kataid, String programid, String userid, String sol, String status) {

    }



}