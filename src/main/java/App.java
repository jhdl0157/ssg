import java.io.*;
import java.util.*;

public class App {
    private static final String FILE_PATH="D:\\tomcat_test\\src\\main\\java\\data\\";
    private static ArrayList<Post> postArrayList;
    static int fileLens;
    public void run(int fileLen,String[] files) {
        fileLens=fileLen;
        init(files);
        System.out.println("== 명언 SSG ==");
        Scanner sc = new Scanner(System.in);
        String ids;
        outer:
        while (true) {
            System.out.printf("명령) ");
            String cmd = sc.nextLine().trim();
            switch (cmd) {
                case "등록":
                    Post post=new Post();
                    System.out.printf("명언 : ");
                    post.setContent(sc.nextLine().trim());
                    System.out.printf("작가 : ");
                    post.setAuthor(sc.nextLine().trim());
                    fileLens++;
                    post.setId(fileLens);
                    File file = new File(FILE_PATH+fileLens+".json");
                    fileWrite(file,post);
                    System.out.println(fileLens+"번 명언이 등록되었습니다.");
                    postArrayList.add(post);
                    break ;

                case "목록":
                    System.out.println("번호 / 작가 / 명언");
                    System.out.println("----------------------");
                    postArrayList.stream()
                            .sorted(Comparator.comparing(Post::getId).reversed())
                            .forEach(x-> System.out.println(x.getId()+" /  "+x.getAuthor()+"  /  "+x.getContent()));
                    break;

                case "삭제":
                    boolean flag=false;
                    String sw=sc.nextLine().trim();
                    System.out.println(sw);
                    int index=Integer.parseInt(sw.replace("?id=","").trim());
                    for(Post p:postArrayList){
                        if(p.getId()==index){
                            flag=true;
                            postArrayList.remove(p);
                            File file1=new File(FILE_PATH+index+".json");
                            if(file1.delete()){
                                System.out.println("파일 삭제완료");
                            }
                            break ;
                        }
                    }
                    System.out.println(flag? index+"번 명언이 삭제되었습니다.":index+"번 명언은 존재하지 않습니다.");
                    break ;

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
        if(files.length==0){
            return;
        }
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
                String[] ars=str.split(",");
                int index=Integer.parseInt(ars[0].trim());
                Post posts=new Post(index,ars[1].trim(),ars[2].trim());
                //System.out.println("sad"+posts.getAuthor());
                postArrayList.add(posts);
                fileStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

