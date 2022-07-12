package util;

import java.util.HashMap;
import java.util.Map;

public class Rq {
    String url;
    String path;
    Map<String, String> queryParams;

    public Rq(String url) {
        this.url = url;
        String[] urlBits = url.split("\\?", 2);
        this.path = urlBits[0];

        queryParams = new HashMap<>();

        if (urlBits.length == 2) {
            String queryStr = urlBits[1];

            String[] paramBits = queryStr.split("&");

            for (String paramBit : paramBits) {
                String[] paramNameAndValue = paramBit.split("=", 2);

                if (paramNameAndValue.length == 1) {
                    continue;
                }

                String paramName = paramNameAndValue[0].trim();
                String paramValue = paramNameAndValue[1].trim();

                queryParams.put(paramName, paramValue);
            }
        }
    }

    public int getIntParam(String paramName, int defaultValue) {
        if (queryParams.containsKey(paramName) == false) {
            return defaultValue;
        }

        String paramValue = queryParams.get(paramName);

        if (paramValue.length() == 0) {
            return defaultValue;
        }

        return Integer.parseInt(paramValue);
    }

    public String getPath() {
        return path;
    }
}