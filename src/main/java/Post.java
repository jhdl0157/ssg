import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class Post {
    private Integer id;
    private String content;
    private String author;

    public Post(int id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }
    public Post(){

    }

    @Override
    public String toString() {
        return "{\n" +
                " \"id\" : " +"\""+ id + "\",\n" +
                " \"content\" : " +"\""+ content + "\",\n" +
                " \"author\" : " +"\""+ author + "\" \n"+
                '}';
    }
}
