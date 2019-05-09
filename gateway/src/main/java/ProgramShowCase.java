public class ProgramShowCase {
    private String title,sensei,language,description,programID;
    private int nbKata;
    private String[] tags;

    public ProgramShowCase(String title, String sensei, String language, String description, int nbKata, String[] tags, String programID){
        this.title = title;
        this.sensei = sensei;
        this.language = language;
        this.description = description;
        this.nbKata = nbKata;
        this.tags = tags;
        this.programID = programID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNbKata() {
        return nbKata;
    }

    public void setNbKata(int nbKata) {
        this.nbKata = nbKata;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getProgramID() {
        return programID;
    }

    public void setProgramID(String programID) {
        this.programID = programID;
    }
}
