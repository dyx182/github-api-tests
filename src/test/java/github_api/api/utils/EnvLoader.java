package github_api.api.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {

    private static final Dotenv dotenv = Dotenv.load();

    public static String dotenvGet(String name) {
        return dotenv.get(name);
    }
}
