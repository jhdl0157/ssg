import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Post {
    Post(){

    }
    private Integer id;
    private String content;
    private String author;
    private String imageUrl;

    public Post(int id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.imageUrl="https://pbs.twimg.com/media/D-VEwpHXYAA_ydF?format=png&name=900x900";
    }
    @Override
    public String toString() {
        return "{\n" +
                " \"id\" : " +"\""+ id + "\",\n" +
                " \"content\" : " +"\""+ content + "\",\n" +
                " \"author\" : " +"\""+ author + "\",\n"+
                " \"imageUrl\" : " +"\""+ imageUrl + "\" \n"+
                '}';
    }
}
