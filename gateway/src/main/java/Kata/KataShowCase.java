package Kata;

public class KataShowCase {
    public String title,difficulty,id,status;
    boolean activated;

    public KataShowCase(String title, String difficulty, String id, String status, boolean activated){
     this.difficulty = difficulty;
     this.title = title;
     this.id = id;
     this.status = status;
     this.activated = activated;
    }
}
