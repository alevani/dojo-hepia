import java.util.ArrayList;

public class ProgramSubscription {

    private String _id,id, iduser, idprogram;
    private boolean status;
    private int nbKataDone;
    private ArrayList<KataSubscription> katas;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getIdprogram() {
        return idprogram;
    }

    public void setIdprogram(String idprogram) {
        this.idprogram = idprogram;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }



    public ArrayList<KataSubscription> getKatas() {
        return katas;
    }

    public void setKatas(ArrayList<KataSubscription> katas) {
        this.katas = katas;
    }

    public int getNbKataDone() {
        return nbKataDone;
    }

    public void setNbKataDone(int nbKataDone) {
        this.nbKataDone = nbKataDone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
