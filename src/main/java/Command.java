import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

@Getter
public enum Command {
    CREATE("등록"), UPDATE("수정"), DELETE("삭제"), GET("목록");

    private final String msg;
    //private final Function<ArrayList<Post>, ArrayList<Post>> process;

    Command(String msg) {
        this.msg = msg;
        //this.process = process;
    }

    public static Command getCommand(String input) {
        return Arrays.stream(Command.values()).filter(command -> command.getMsg().equals(input.trim())).findFirst().orElseThrow(IllegalArgumentException::new);

    }

}
