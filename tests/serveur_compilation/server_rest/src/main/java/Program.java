import java.util.ArrayList;

public class Program {
    private String sensei,language,title,descr,tags;
    private int nbKata;
    private ArrayList<Kata> kata;
/*
    public Program(String sensei, String language,int nbkata, String title, String descr, String tags, Kata[] katas){
        this.sensei = sensei;
        this.language = language;
        this.nbKata = nbkata;
        this.title = title;
        this.descr = descr;
        this.tags = tags;
        this.kata = katas;
    }
*/
    public String getSensei() {
        return sensei;
    }

    public void setSensei(String sensei) {
        this.sensei = sensei;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getNbKata() {
        return nbKata;
    }

    public void setNbKata(int nbKata) {
        this.nbKata = nbKata;
    }

    public ArrayList<Kata> getKata() {
        return kata;
    }

    public void setKata(ArrayList<Kata> kata) {
        this.kata = kata;
    }
}
