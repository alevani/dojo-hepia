package ch.hepia.model.program;

import java.util.ArrayList;

public class ProgramShowCase {
    public String title, sensei, language, description, id,password;
    public int nbKata;
    public ArrayList<String> tags;
    public int nbKataDone;


    public ProgramShowCase(String title, String sensei, String language, String description, int nbKata, ArrayList<String> tags, String id, int nbKataDone) {
        this.title = title;
        this.sensei = sensei;
        this.language = language;
        this.description = description;
        this.nbKata = nbKata;
        this.tags = tags;
        this.id = id;
        this.nbKataDone = nbKataDone;
        

    }

    public ProgramShowCase(String title, String sensei, String language, String description, int nbKata, ArrayList<String> tags, String id, String password) {
        this.title = title;
        this.sensei = sensei;
        this.language = language;
        this.description = description;
        this.nbKata = nbKata;
        this.tags = tags;
        this.id = id;
        this.password = password;
    }

    public ProgramShowCase(){};
}