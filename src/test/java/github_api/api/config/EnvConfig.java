package github_api.api.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {

    private static final Dotenv dotenv = Dotenv.load();

    public static final String ORG = dotenvGet("GITHUB_ORG");
    public static final String LOGIN = dotenvGet("GITHUB_LOGIN"); //your login here
    public static final String TOKEN = dotenvGet("GITHUB_TOKEN");//your token  here
    public static final String INVALID_TOKEN = null;
    public static final String TOKEN_WITHOUT_ACCESS = dotenvGet("GITHUB_TOKEN_WITHOUT_ACCESS"); //your token  here

    public static String dotenvGet(String name) {
        return dotenv.get(name);
    }

}
