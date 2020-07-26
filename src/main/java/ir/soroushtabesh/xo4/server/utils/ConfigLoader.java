package ir.soroushtabesh.xo4.server.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ir.soroushtabesh.xo4.server.models.Config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigLoader {
    private ConfigLoader() {
    }

    public static Config loadConfig(File file) {
        try {
            String data = Files.readString(file.toPath());
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(data, Config.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
