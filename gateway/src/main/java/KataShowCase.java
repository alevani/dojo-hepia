public class KataShowCase {
    private String title,difficulty,id,status;

    public KataShowCase(String title, String difficulty, String id, String status){
     this.difficulty = difficulty;
     this.title = title;
     this.id = id;
     this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getId() {
        return id;
    }

    public void setId(String programID) {
        this.id = programID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
