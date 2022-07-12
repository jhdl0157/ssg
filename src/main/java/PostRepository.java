import lombok.Getter;
import util.JsonParse;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class PostRepository {
    private int fileLens;
    private static final String INIT_MSG = "== 명언 SSG ==";
    private CopyOnWriteArrayList<Post> posts=new CopyOnWriteArrayList<>();
    private   String FILE_PATH;
    private   String FILE_DIR_URL;
    public PostRepository(String path,String path2){
        this.FILE_PATH=path;
        this.FILE_DIR_URL=path2;
    }

    void init() {
        String[] files=getFileList();
        System.out.println("현재 작업 경로: " + FILE_PATH);
        System.out.println("파일에서 자료 불러오는중...");
        if (files.length == 0) {
            return;
        }
        for (String file : files) {
            try {
                String filePath = FILE_DIR_URL + file;
                FileInputStream fileStream = null;
                fileStream = new FileInputStream(filePath);
                byte[] readBuffer = new byte[fileStream.available()];
                while (fileStream.read(readBuffer) != -1) {
                }
                String result = new String(readBuffer);
                String str = JsonParse.jsonToString(result);
                String[] ars = str.split(",");
                int index = Integer.parseInt(ars[0].trim());
                Post posts = new Post(index, ars[1].trim(), ars[2].trim());
                this.posts.add(posts);
                fileStream.close();
                System.out.println("파일 : "+file+" 불러오기 완료!!");
                fileLens = getMaxNumberFileName(getFileList());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(INIT_MSG);
    }
    int getMaxNumberFileName(final String[] files) {
        return Arrays.stream(files).mapToInt(file -> Integer.parseInt(file.replace(".json", "").trim()))
                .max().orElse(0);
    }
    String[] getFileList() {
        File dir = new File(FILE_PATH);
        return dir.list();
    }
    void fileLensPlus(){
        this.fileLens++;
    }
}
