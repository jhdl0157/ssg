import java.io.File;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String DATA_DIRECTORY_FOR_MAC = "/Users/jaeho/Desktop/ssg/src/main/java/data";
        String DATA_DIRECTORY = "D:\\tomcat_test\\src\\main\\java\\data";
        File dir = new File(DATA_DIRECTORY);
        String[] filenames = dir.list();
        new App().run();
    }
}
