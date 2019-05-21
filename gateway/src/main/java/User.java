import java.util.ArrayList;

public class User {
    private String id, _id, username, password, level;
    private ArrayList<ProgramSubscription> programSubscriptions;

    public User(String id, String username, String level, String password) {
        this.id = id;
        this.username = username;
        this.level = level;
        this.password = password;
    }

    public User(){}

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public ArrayList<ProgramSubscription> getProgramSubscriptions() {
        return programSubscriptions;
    }

    public void setProgramSubscriptions(ArrayList<ProgramSubscription> programSubscriptions) {
        this.programSubscriptions = programSubscriptions;
    }
}