
import util.JsonParse;
import util.Rq;

import java.io.*;
import java.nio.file.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class App {
    private PostController postController;
    private PostService postService;
    private PostRepository postRepository;
    App(){
        this.postController=new PostController();
        Path currentPath = Paths.get("");
        String path = currentPath.toAbsolutePath()+"/src/main/java/data";
        this.FILE_PATH=path;
        this.FILE_DIR_URL=path+"/";
        this.postService=new PostService(FILE_PATH,FILE_DIR_URL,sc);
        this.postRepository=new PostRepository(FILE_PATH,FILE_DIR_URL);
    }
    public  String FILE_PATH;
    public  String FILE_DIR_URL;
    private static final String INIT_MSG = "== 명언 SSG ==";
    private static CopyOnWriteArrayList<Post> postArrayList;
    static int fileLens;
    static Scanner sc = new Scanner(System.in);

    public void run() {
        MacOrWindow();
        init(getFileList());
        outer:
        while (true) {
            System.out.print("명령) ");
            String cmd = sc.nextLine().trim();
            Rq rq=new Rq(cmd);
            switch (rq.getPath()) {
                case "등록":
                    regist();
                    break;
                case "목록":
                    postService.printList(postArrayList);
                    break;
                case "삭제":
                    deletePost(rq);
                    break;
                case "수정":
                    fixPost(rq);
                    break;
                case "빌드":
                    buildPost();
                    break;
                case "종료":
                    break outer;
            }
        }
        sc.close();
    }

    private void buildPost() {
        StringBuilder sb=new StringBuilder();
        sb.append("[ \n ");
        for(int i=0;i<postArrayList.size();i++){
            sb.append(postArrayList.get(i).toString());
            if(i<postArrayList.size()-1) {
                sb.append(",");
                sb.append("\n");
                }
        }
        sb.append("\n]");
        System.out.println(sb);
        Path currentPath = Paths.get("");
        String path = currentPath.toAbsolutePath()+"/src/main/java/";
        File dataJson=new File(path+"data.json");
        fileWrite(dataJson,sb.toString());
    }

    void regist() {
        Post post = new Post();
        System.out.print("명언 : ");
        post.setContent(sc.nextLine().trim());
        System.out.print("작가 : ");
        post.setAuthor(sc.nextLine().trim());
        fileLens++;
        post.setId(fileLens);
        post.setImageUrl("https://pbs.twimg.com/media/D-VEwpHXYAA_ydF?format=png&name=900x900");
        File file = new File(FILE_DIR_URL + fileLens + ".json");
        fileWrite(file, post);
        System.out.println(fileLens + "번 명언이 등록되었습니다.");
        postArrayList.add(post);
    }

    void deletePost(Rq rq) {
        int paramId=rq.getIntParam("id",0);
        if (paramId == 0) {
            System.out.println("id를 입력해주세요.");
            return;
        }
        System.out.println("삭제 진입 ids = " + paramId);
        System.out.println(postService.fileDelete(paramId, getFileList(),postArrayList) ? paramId + "번 명언이 삭제되었습니다." : paramId + "번 명언은 존재하지 않습니다.");
    }

    void fixPost(Rq rq) {
        int paramId=rq.getIntParam("id",0);
        if (paramId == 0) {
            System.out.println("id를 입력해주세요.");
            return;
        }
        Optional<Post> fixPost = postArrayList.stream()
                .filter(x -> x.getId().equals(paramId))
                .findFirst();
        System.out.println("기존 명언 : " + fixPost.get().getContent());
        System.out.print("새로운 명언 : ");
        String newContent = sc.nextLine().trim();
        System.out.println(newContent);
        Post fixPosts = new Post(fixPost.get().getId(), newContent, fixPost.get().getAuthor());
        if (!postService.fileDelete(paramId,getFileList(),postArrayList)) {
            System.out.println(paramId + "번 명언은 존재하지 않습니다.");
            return;
        }
        fileLens++;
        File file1 = new File(FILE_DIR_URL + fileLens + ".json");
        fileWrite(file1, fixPosts);
        System.out.println(fixPost.get().getId() + "번 명언이 수정되었습니다.");
        postArrayList.add(fixPosts);
    }
    void MacOrWindow(){
        System.out.print("Window면 o입력, Mac이면 x입력 : ");
        String computerType = sc.nextLine().trim();
        if (computerType.equals("o")) {
            FILE_PATH = FILE_PATH.replace("/","\\");
            FILE_DIR_URL = FILE_DIR_URL.replace("/","\\");
        }
    }
    void fileWrite(File file, Post post) {
        System.out.println("fileWrite 진입 num :" + fileLens);
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
        System.out.println("fileWrite 진입 num :" + fileLens);
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

    void init(String[] files) {
        System.out.println("현재 작업 경로: " + FILE_PATH);
        System.out.println("파일에서 자료 불러오는중...");
        postArrayList = new CopyOnWriteArrayList<>();
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
                postArrayList.add(posts);
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
}

