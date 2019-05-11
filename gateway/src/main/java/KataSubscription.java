public class KataSubscription {
    //katas [{id:1,status:"resolved",mysol:".."}]
    private String _id,id,status,mysol;

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
}
