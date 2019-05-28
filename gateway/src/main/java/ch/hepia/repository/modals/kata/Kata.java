package ch.hepia.repository.modal.kata;

public class Kata {
    private String _id, id,title, canva, cassert, solution, rules,difficulty,language;
    private boolean keepAssert,activated;
    private double nbAttempt;




    public boolean isKeepAssert() {
        return keepAssert;
    }

    public void setKeepAssert(boolean keepAssert) {
        this.keepAssert = keepAssert;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCanva() {
        return canva;
    }

    public void setCanva(String canva) {
        this.canva = canva;
    }

    public String getCassert() {
        return cassert;
    }

    public void setCassert(String cassert) {
        this.cassert = cassert;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public double getNbAttempt() {
        return nbAttempt;
    }

    public void setNbAttempt(double nbAttempt) {
        this.nbAttempt = nbAttempt;
    }


    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
