package ch.hepia.model.program;

import ch.hepia.model.kata.Kata;

import java.util.List;

public class Program {
    private String _id, id, sensei, language, title, description, idsensei, password;
    private List<String> tags;
    private int nbKata;
    private List<Kata> katas;

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


    public List<Kata> getKatas() {
        return katas;
    }

    public void setKatas(List<Kata> katas) {
        this.katas = katas;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdsensei() {
        return idsensei;
    }

    public void setIdsensei(String idsensei) {
        this.idsensei = idsensei;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean check(String password){
        return this.password.equals(password);
    }
}
