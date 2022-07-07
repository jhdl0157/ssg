import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class App {
    private static final String FILE_PATH="D:\\tomcat_test\\src\\main\\java\\data\\";
    private static ArrayList<Post> postArrayList;
    private static String inputindex;
    static int fileLens;
    public void run(String[] files) {
        init(files);
        fileLens=getMaxNumberFileName(files);
        System.out.println(fileLens);
        System.out.println("== 명언 SSG ==");
        Scanner sc = new Scanner(System.in);
        outer:
        while (true) {
            System.out.print("명령) ");
            String cmd = sc.nextLine().trim();
            switch (cmd) {
                case "등록":
                    Post post=new Post();
                    System.out.print("명언 : ");
                    post.setContent(sc.nextLine().trim());
                    System.out.print("작가 : ");
                    post.setAuthor(sc.nextLine().trim());
                    fileLens++;
                    post.setId(fileLens);
                    File file = new File(FILE_PATH+fileLens+".json");
                    fileWrite(file,post);
                    System.out.println(fileLens+"번 명언이 등록되었습니다.");
                    postArrayList.add(post);
                    break ;

                case "목록":
                    printHeader();
                    if(postArrayList.isEmpty()){
                        System.out.println("게시글이 하나도 없습니다");
                        break ;
                    }
                    printList();
                    break;

                case "삭제":
                    inputindex=sc.nextLine().trim();
                    System.out.print(inputindex);
                    int index=Integer.parseInt(inputindex.replace("?id=","").trim());
                    System.out.println(fileDelete(index) ? index+"번 명언이 삭제되었습니다.":index+"번 명언은 존재하지 않습니다.");
                    break ;

                    case "종료":
                    break outer;

                case "수정":
                    inputindex=sc.nextLine().trim();
                    System.out.println(inputindex);
                    index=Integer.parseInt(inputindex.replace("?id=","").trim());
                    Optional<Post> fixPost=postArrayList.stream()
                            .filter(x->x.getId()==index)
                            .findFirst();
                    if(fixPost.isEmpty()){
                        System.out.println(index+"번 명언은 존재하지 않습니다.");
                        break ;
                    }
                    fixPost.ifPresent(value -> System.out.println("기존 명언 : " + value.getContent()));
                    System.out.print("기존 명언 : ");
                    String newContent=sc.nextLine().trim();
                    System.out.println(newContent);
                    Post fixPosts=new Post(fixPost.get().getId(),newContent,fixPost.get().getAuthor());
                    if(!fileDelete(index)){
                        System.out.println(index+"번 명언은 존재하지 않습니다.");
                        break ;
                    }
                    File file1=new File(FILE_PATH+fileLens+".json");
                    fileWrite(file1,fixPosts);
                    fileLens++;
                    System.out.println(fixPost.get().getId()+"번 명언이 수정되었습니다.");
                    postArrayList.add(fixPosts);
                    break ;
            }
        }
        sc.close();
    }
    Boolean fileDelete(int index){
        System.out.println("fileDelete 진입 num :"+fileLens);
        for(Post p:postArrayList){
            if(p.getId()==index){
                postArrayList.remove(p);
                File file1=new File(FILE_PATH+fileLens+".json");
                if(file1.delete()){
                    System.out.println("파일 삭제완료");
                    return true;
                }
            }
        }
        return false;
    }
    void printHeader(){
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
    }
    void printList(){
        postArrayList.stream()
                .sorted(Comparator.comparing(Post::getId).reversed())
                .forEach(x-> System.out.println(x.getId()+" /  "+x.getAuthor()+"  /  "+x.getContent()));
    }
    void fileWrite(File file,Post post){
        System.out.println("fileWrite 진입 num :"+fileLens);
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
                postArrayList.add(posts);
                fileStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    int findByIndexId(String[] files,int index) {
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
               if(Integer.parseInt(ars[0])==index){
                   return Integer.parseInt(s.replace(".json","").trim());
               }
                fileStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
    int getMaxNumberFileName(String[] files){
        int max=0;
        for(String s:files){
            int number=Integer.parseInt(s.replace(".json","").trim());
            if(number>max){
                max=number;
            }
        }
        return max;
    }
}

