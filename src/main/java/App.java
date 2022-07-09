import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class App {
    private static final String FILE_PATH = "/Users/jaeho/Desktop/ssg/src/main/java/data/";
    private static final String INIT_MSG = "== 명언 SSG ==";
    private static ArrayList<Post> postArrayList;
    private static String inputindex;
    private static int ids;
    static int fileLens;

    public void run(String[] files) {
        init(files);
        fileLens = getMaxNumberFileName(files);
        System.out.println(fileLens);
        System.out.println(INIT_MSG);
        Scanner sc = new Scanner(System.in);
        outer:
        while (true) {
            System.out.print("명령) ");
            String cmd = sc.nextLine().trim();
            //Command command = Command.getCommand(cmd);
            cmd = fixParsing(cmd);
            switch (cmd) {
                case "등록":
                    Post post = new Post();
                    System.out.print("명언 : ");
                    post.setContent(sc.nextLine().trim());
                    System.out.print("작가 : ");
                    post.setAuthor(sc.nextLine().trim());
                    fileLens++;
                    post.setId(fileLens);
                    File file = new File(FILE_PATH + fileLens + ".json");
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
                    inputindex = sc.nextLine().trim();
                    System.out.print(inputindex);
                    int index = Integer.parseInt(inputindex.replace("?id=", "").trim());
                    System.out.println(fileDelete(index) ? index + "번 명언이 삭제되었습니다." : index + "번 명언은 존재하지 않습니다.");
                    break;

                case "종료":
                    break outer;

                case "수정":
                    Post fixPost = postArrayList.stream()
                            .filter(x -> x.getId().equals(ids))
                            .findFirst().orElseThrow(() -> new NotFoundException(ids + "번 명언은 존재하지 않습니다."));
                    System.out.println("기존 명언 : " + fixPost.getContent());
                    System.out.print("새로운 명언 : ");
                    String newContent = sc.nextLine().trim();
                    System.out.println(newContent);
                    Post fixPosts = new Post(fixPost.getId(), newContent, fixPost.getAuthor());
                    if (!fileDelete(ids)) {
                        System.out.println(ids + "번 명언은 존재하지 않습니다.");
                        break;
                    }
                    File file1 = new File(FILE_PATH + fileLens + ".json");
                    fileWrite(file1, fixPosts);
                    System.out.println(fixPost.getId() + "번 명언이 수정되었습니다.");
                    postArrayList.add(fixPosts);
                    break;
            }
        }
        sc.close();
    }

    String fixParsing(String input) {
        if (input.contains("수정?id=")) {
            ids = Integer.parseInt(input.replace("수정?id=", "").trim());
            input = "수정";
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

    Boolean fileDelete(int index) {
        System.out.println("fileDelete 진입 num :" + fileLens);
        if (postArrayList.removeIf(post -> post.getId().equals(index))) {
            File file1 = new File(FILE_PATH + index + ".json");
            if (file1.delete()) {
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
        for (String s : files) {
            try {
                String filePath = FILE_PATH + s;
                FileInputStream fileStream = null;
                fileStream = new FileInputStream(filePath);
                byte[] readBuffer = new byte[fileStream.available()];
                while (fileStream.read(readBuffer) != -1) {
                }
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
                String filePath = FILE_PATH + s;
                FileInputStream fileStream = null;
                fileStream = new FileInputStream(filePath);
                byte[] readBuffer = new byte[fileStream.available()];
                while (fileStream.read(readBuffer) != -1) {
                }
                String result = new String(readBuffer);
                String str = jsonToString(result);
                String[] ars = str.split(",");
                if (Integer.parseInt(ars[0]) == index) {
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

