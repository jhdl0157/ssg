import java.io.File;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String DATA_DIRECTORY = "/Users/jaeho/Desktop/ssg/src/main/java/data";
        File dir = new File(DATA_DIRECTORY);
        String[] filenames = dir.list();
        new App().run(filenames);
    }
}
