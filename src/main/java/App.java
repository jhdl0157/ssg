
import util.JsonParse;

import java.io.*;
import java.nio.file.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class App {
    public static String FILE_PATH= FileUrl.MAC.getUrl();
    public static String FILE_DIR_URL=FileUrl.MAC.getMacDirUrl();
    private static final String INIT_MSG = "== 명언 SSG ==";
    private static CopyOnWriteArrayList<Post> postArrayList;
    private static int ids;
    static int fileLens;
    static Scanner sc = new Scanner(System.in);

    public void run() {
        MacOrWindow();
        init(getFileList());
        outer:
        while (true) {
            System.out.print("명령) ");
            String cmd = sc.nextLine().trim();
            cmd = fixOrDeleteCommandParsing(cmd);
            switch (cmd) {
                case "등록":
                    regist();
                    break;
                case "목록":
                    printList();
                    break;
                case "삭제":
                    deletePost();
                    break;
                case "수정":
                    fixPost();
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
        File dataJson=new File("/Users/jaeho/Desktop/ssg/src/main/java/data.json");
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
        File file = new File(FILE_DIR_URL + fileLens + ".json");
        fileWrite(file, post);
        System.out.println(fileLens + "번 명언이 등록되었습니다.");
        postArrayList.add(post);
    }

    void deletePost() {
        System.out.println("삭제 진입 ids = " + ids);
        System.out.println(fileDelete(ids, getFileList()) ? ids + "번 명언이 삭제되었습니다." : ids + "번 명언은 존재하지 않습니다.");

    }

    void fixPost() {
        System.out.println("수정 진입 ids = " + ids);
        Optional<Post> fixPost = postArrayList.stream()
                .filter(x -> x.getId().equals(ids))
                .findFirst();
        System.out.println("기존 명언 : " + fixPost.get().getContent());
        System.out.print("새로운 명언 : ");
        String newContent = sc.nextLine().trim();
        System.out.println(newContent);
        Post fixPosts = new Post(fixPost.get().getId(), newContent, fixPost.get().getAuthor());
        if (!fileDelete(ids, getFileList())) {
            System.out.println(ids + "번 명언은 존재하지 않습니다.");
            return;
        }
        fileLens++;
        File file1 = new File(FILE_DIR_URL + fileLens + ".json");
        fileWrite(file1, fixPosts);
        System.out.println(fixPost.get().getId() + "번 명언이 수정되었습니다.");
        postArrayList.add(fixPosts);
    }

    String fixOrDeleteCommandParsing(String input) {
        if (input.contains("수정?id=")) {
            ids = Integer.parseInt(input.replace("수정?id=", "").trim());
            input = "수정";
            return input;
        }
        if (input.contains("삭제?id=")) {
            ids = Integer.parseInt(input.replace("삭제?id=", "").trim());
            input = "삭제";
            return input;
        }
        return input;
    }
    void MacOrWindow(){
        System.out.print("Mac 이면 1, Window면 0 입력 : ");
        String computerType = sc.nextLine().trim();
        if (computerType.equals("1")) {
            FILE_PATH = FileUrl.MAC.getUrl();
            FILE_DIR_URL = FileUrl.MAC.getMacDirUrl();
        }
        if (computerType.equals("0")) {
            FILE_PATH = FileUrl.WINDOW.getUrl();
            FILE_DIR_URL = FileUrl.WINDOW.getWindowDirUrl();
        }
    }

    Boolean fileDelete(int index, String[] files) {
        System.out.println("fileDelete 진입 index :" + index);
        System.out.println("fileDelete 진입 FileLen :" + fileLens);
        System.out.println("삭제하는 파일 명은 " + findByIndexId(files, index) + ".json");
        for (Post post : postArrayList) {
            if (post.getId() == index) {
                postArrayList.remove(post);
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

    void printHeader() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
    }

    void printList() {
        printHeader();
        if (postArrayList.isEmpty()) {
            System.out.println("게시글이 하나도 없습니다");
            return;
        }
        postArrayList.stream()
                .sorted(Comparator.comparing(Post::getId).reversed())
                .forEach(x -> System.out.println(x.getId() + " /  " + x.getAuthor() + "  /  " + x.getContent()));
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
                System.out.println("파일"+file+" 불러오기 완료!!");
                fileLens = getMaxNumberFileName(getFileList());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(INIT_MSG);
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


    int getMaxNumberFileName(final String[] files) {
        return Arrays.stream(files).mapToInt(file -> Integer.parseInt(file.replace(".json", "").trim()))
                .max().orElse(0);
    }
     String[] getFileList() {
        File dir = new File(FILE_PATH);
        return dir.list();
    }
}

