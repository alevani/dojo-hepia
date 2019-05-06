public class Kata {
    private String title, canva, cassert, solution, rules;
    private boolean keepAssert;
    private int nbAttempt,programID;


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

    public int getNbAttempt() {
        return nbAttempt;
    }

    public void setNbAttempt(int nbAttempt) {
        this.nbAttempt = nbAttempt;
    }

    public int getProgramID() {
        return programID;
    }

    public void setProgramID(int programID) {
        this.programID = programID;
    }
}
