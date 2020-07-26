package ir.soroushtabesh.xo4.server.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtil {
    private static final Gson gson = new GsonBuilder().create();

    public static Gson getGson() {
        return gson;
    }
}
