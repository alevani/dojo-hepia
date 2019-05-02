import static org.junit.Assert.*;

public class Main {

    public static void main(String[] args) {
        assertArrayEquals(kata.bytwo(new int[]{2, 3, 5}), new int[]{4, 6, 10});
        assertArrayEquals(kata.bytwo(new int[]{12, 34, 52}), new int[]{24, 68, 104});
    }
}
