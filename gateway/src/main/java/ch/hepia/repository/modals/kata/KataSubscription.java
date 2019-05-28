package ch.hepia.repository.modal.kata;

public class KataSubscription {
    //katas [{id:1,status:"resolved",mysol:".."}]



    private String _id, id, status, mysol;
    private int nbAttempt;

    public KataSubscription(String id, String status, String mysol, int nbAttempt) {
        this.id = id;
        this.status = status;
        this.mysol = mysol;
        this.nbAttempt = nbAttempt;
    }

    public KataSubscription(){}

    public String getMysol() {
        return mysol;
    }

    public void setMysol(String mysol) {
        this.mysol = mysol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getNbAttempt() {
        return nbAttempt;
    }

    public void setNbAttempt(int nbAttempt) {
        this.nbAttempt = nbAttempt;
    }
}
