import java.io.*;
import java.util.*;

public class App {
    static String FILE_PATH = FileUrl.WINDOW.getUrl();
    static String FILE_DIR_URL =FileUrl.WINDOW.getUrl()+"\\";
    private static final String INIT_MSG = "== 명언 SSG ==";
    private static ArrayList<Post> postArrayList;
    private static String inputindex;
    private static int ids;
    static int fileLens;

    public void run() {
        File dir = new File(FILE_PATH);
        String[] files = dir.list();
        init(files);
        fileLens = getMaxNumberFileName(files);
        System.out.println(INIT_MSG);
        Scanner sc = new Scanner(System.in);
        outer:
        while (true) {
            System.out.print("명령) ");
            String cmd = sc.nextLine().trim();
            cmd = fixOrDeleteCommandParsing(cmd);
            switch (cmd) {
                case "등록":
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
                    break;

                case "목록":
                    printHeader();
                    if (postArrayList.isEmpty()) {
                        System.out.println("게시글이 하나도 없습니다");
                        break;
                    }
                    printList();
                    break;

                case "삭제":
                    System.out.println("삭제 진입 ids = "+ids);
                    System.out.println(fileDelete(ids,files) ? ids + "번 명언이 삭제되었습니다." : ids + "번 명언은 존재하지 않습니다.");
                    break;

                case "종료":
                    break outer;

                case "수정":
                    System.out.println("수정 진입 ids = "+ids);
                    Optional<Post> fixPost = postArrayList.stream()
                            .filter(x -> x.getId().equals(ids))
                            .findFirst();
                    System.out.println("기존 명언 : " + fixPost.get().getContent());
                    System.out.print("새로운 명언 : ");
                    String newContent = sc.nextLine().trim();
                    System.out.println(newContent);
                    Post fixPosts = new Post(fixPost.get().getId(), newContent, fixPost.get().getAuthor());
                    if (!fileDelete(ids,files)) {
                        System.out.println(ids + "번 명언은 존재하지 않습니다.");
                        break;
                    }
                    File file1 = new File(FILE_PATH + fileLens + ".json");
                    fileWrite(file1, fixPosts);
                    System.out.println(fixPost.get().getId() + "번 명언이 수정되었습니다.");
                    postArrayList.add(fixPosts);
                    break;
            }
        }
        sc.close();
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

    String deleteParsing(String input) {
        if (input.contains("삭제?id=")) {
            ids = Integer.parseInt(input.replace("삭제?id=", "").trim());
            input = "삭제";
        }
        return input;
    }

    Boolean fileDelete(int index,String[] files) {
        System.out.println("fileDelete 진입 index :" + index);
        System.out.println("fileDelete 진입 FileLen :" + index);
        System.out.println("삭제하는 파일 명은 "+findByIndexId(files,index)+".json");
        if (postArrayList.removeIf(post -> post.getId().equals(index))) {
            File file1 = new File(FILE_DIR_URL + findByIndexId(files, index) + ".json");
            File fact=new File("D:\\SSG\\src\\main\\java\\data\\4.json");
            if(fact.exists()){
                System.out.println("파일은 존재함 ");
            }
            if (fact.delete()) {
                System.out.println("파일 삭제완료");
                return true;
            }
        }
        return false;
    }

    void printHeader() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
    }

    void printList() {
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
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void init(String[] files) {
        postArrayList = new ArrayList<>();
        if (files.length == 0) {
            return;
        }
        for (String file : files) {
            try {
                String filePath = FILE_DIR_URL + file;
                FileInputStream fileStream = null;
                fileStream = new FileInputStream(filePath);
                byte[] readBuffer = new byte[fileStream.available()];
                while (fileStream.read(readBuffer) != -1) {}
                String result = new String(readBuffer);
                String str = jsonToString(result);
                String[] ars = str.split(",");
                int index = Integer.parseInt(ars[0].trim());
                Post posts = new Post(index, ars[1].trim(), ars[2].trim());
                postArrayList.add(posts);
                fileStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    int findByIndexId(String[] files, int index) {
        for (String s : files) {
            try {
                String filePath = FILE_DIR_URL + s;
                FileInputStream fileStream = null;
                fileStream = new FileInputStream(filePath);
                byte[] readBuffer = new byte[fileStream.available()];
                while (fileStream.read(readBuffer) != -1) {
                }
                String result = new String(readBuffer);
                String str = jsonToString(result);
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

    private String jsonToString(String result) {
        String str = result.replace("{", "").replace("}", "")
                .replace("\"id\" : ", "").replace("\"content\" : ", "")
                .replace("\"author\" : ", "").replaceAll("\"", "");
        return str;
    }

    int getMaxNumberFileName(String[] files) {
        return Arrays.stream(files).mapToInt(file -> Integer.parseInt(file.replace(".json", "").trim()))
                .max().orElse(0);
    }
}

