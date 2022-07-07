import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class App {
    private static final String FILE_PATH="D:\\tomcat_test\\src\\main\\java\\data\\";
    private static ArrayList<Post> postArrayList;
    static int fileLens;
    public void run(int fileLen,String[] files) {
        fileLens=fileLen;
        init(files);
        System.out.println("== 명언 SSG ==");
        Scanner sc = new Scanner(System.in);

        outer:
        while (true) {
            System.out.printf("명령) ");
            String cmd = sc.nextLine().trim();

            switch (cmd) {
                case "등록":
                    Post post=new Post();
                    System.out.printf("명연 : ");
                    post.setContent(sc.nextLine().trim());
                    System.out.printf("작가 : ");
                    post.setAuthor(sc.nextLine().trim());
                    fileLens++;
                    post.setId(fileLens);
                    File file = new File(FILE_PATH+fileLens+".json");
                    fileWrite(file,post);
                    System.out.println(fileLens+"번 명언이 등록되었습니다.");
                    break ;

                case "목록":
                    System.out.println("번호 / 작가 / 명언");
                    System.out.println("----------------------");
                    break;
                case "종료":
                    break outer;
            }
        }

        sc.close();
    }
    void fileWrite(File file,Post post){
        try {
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(post.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    void init(String[] files){
        postArrayList=new ArrayList<>();
        for(String s:files){
            try {
                String filePath = FILE_PATH+s;
                FileInputStream fileStream = null;
                fileStream = new FileInputStream( filePath );
                byte[ ] readBuffer = new byte[fileStream.available()];
                while (fileStream.read( readBuffer ) != -1){}
                String result=new String(readBuffer);
                String str=result.replace("{","").replace("}","")
                        .replace("\"id\" : ","").replace("\"content\" : ","")
                        .replace("\"author\" : ","").replaceAll("\"","");
                //System.out.println(str);
                String[] ars=str.split(",");
                for(String s1 : ars){
                    System.out.println(s1);
                }
                int index=Integer.parseInt(ars[0]);
                Post posts=new Post(index,ars[1],ars[2]);

                System.out.println("ars[2]:"+ars[2]);

                System.out.println(posts.getAuthor());
                fileStream.close(); //스트림 닫기
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }
}

