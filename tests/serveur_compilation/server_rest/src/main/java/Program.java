import java.util.ArrayList;

public class Program {
    private String id,sensei,language,title,description;
    private String[] tags;
    private int nbKata;

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

    public int getNbKata() {
        return nbKata;
    }

    public void setNbKata(int nbKata) {
        this.nbKata = nbKata;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
/*
    public ArrayList<Kata> getKata() {
        return kata;
    }

    public void setKata(ArrayList<Kata> kata) {
        this.kata = kata;
    }*/
}
