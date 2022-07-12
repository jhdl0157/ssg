package util;

public class JsonParse {
    static public String jsonToString(final String result) {
        String str = result.replace("{", "").replace("}", "")
                .replace("\"id\" : ", "").replace("\"content\" : ", "")
                .replace("\"author\" : ", "").replaceAll("\"", "");
        return str;
    }
}
