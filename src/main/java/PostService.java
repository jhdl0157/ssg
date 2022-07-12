import util.JsonParse;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class PostService{
    public  String FILE_PATH;
    public  String FILE_DIR_URL;
    private Scanner sc;
    private PostRepository postRepository;
    PostService(String path,String path2,Scanner sc){
        this.FILE_PATH=path;
        this.FILE_DIR_URL=path2;
        this.sc=sc;
        this.postRepository=new PostRepository(path,path2);
    }
    void printHeader() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
    }
    void printList(CopyOnWriteArrayList<Post> posts){
                printHeader();
            if (posts.isEmpty()) {
                System.out.println("게시글이 하나도 없습니다");
                return;
            }
            posts.stream()
                    .sorted(Comparator.comparing(Post::getId).reversed())
                    .forEach(x -> System.out.println(x.getId() + " /  " + x.getAuthor() + "  /  " + x.getContent()+ "  /  " + x.getImageUrl()));
    }
    void regist(CopyOnWriteArrayList<Post> posts) {
        Post post = new Post();
        System.out.print("명언 : ");
        post.setContent(sc.nextLine().trim());
        System.out.print("작가 : ");
        post.setAuthor(sc.nextLine().trim());
        App.fileLens++;
        post.setId(App.fileLens);
        File file = new File(FILE_DIR_URL + App.fileLens + ".json");
        fileWrite(file, post);
        System.out.println(App.fileLens + "번 명언이 등록되었습니다.");
        posts.add(post);
    }
    Boolean fileDelete(int index, String[] files,CopyOnWriteArrayList<Post> posts) {
        System.out.println("fileDelete 진입 index :" + index);
        System.out.println("fileDelete 진입 FileLen :" + App.fileLens);
        System.out.println("삭제하는 파일 명은 " + findByIndexId(files, index) + ".json");
        for (Post post : posts) {
            if (post.getId() == index) {
                posts.remove(post);
                Path path = Paths.get(FILE_DIR_URL + findByIndexId(files, index) + ".json");
                try {
                    Files.delete(path);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }
    int findByIndexId(final String[] files, final int index) {
        for (String s : files) {
            try {
                String filePath = FILE_DIR_URL + s;
                FileInputStream fileStream = null;
                fileStream = new FileInputStream(filePath);
                byte[] readBuffer = new byte[fileStream.available()];
                while (fileStream.read(readBuffer) != -1) {
                }
                fileStream.close();
                String result = new String(readBuffer);
                String str = JsonParse.jsonToString(result);
                String[] ars = str.split(",");
                if (Integer.parseInt(ars[0].trim()) == index) {
                    return Integer.parseInt(s.replace(".json", "").trim());
                }
                fileStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
    public void buildPost(CopyOnWriteArrayList<Post> posts ) {
        StringBuilder sb=new StringBuilder();
        sb.append("[ \n ");
        for(int i=0;i<posts.size();i++){
            sb.append(posts.get(i).toString());
            if(i<posts.size()-1) {
                sb.append(",");
                sb.append("\n");
            }
        }
        sb.append("\n]");
        System.out.println(sb);
        File dataJson=new File("/Users/jaeho/Desktop/ssg/src/main/java/data.json");
        fileWrite(dataJson,sb.toString());
    }
    void fileWrite(File file, Post post) {
        System.out.println("fileWrite 진입 num :" + App.fileLens);
        try {
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(post.toString());
            fileWriter.close();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void fileWrite(File file, String result) {
        System.out.println("fileWrite 진입 num :" + App.fileLens);
        try {
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(result);
            fileWriter.close();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
