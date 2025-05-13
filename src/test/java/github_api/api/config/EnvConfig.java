package github_api.api.config;

import static github_api.api.utils.EnvLoader.dotenvGet;

public class EnvConfig {

    public static final String ORG = dotenvGet("GITHUB_ORG");
    public static final String LOGIN = dotenvGet("GITHUB_LOGIN"); //your login here
    public static final String TOKEN = dotenvGet("GITHUB_TOKEN");//your token  here
    public static final String TOKEN_WITHOUT_ACCESS = dotenvGet("GITHUB_TOKEN_WITHOUT_ACCESS"); //your token  here
    public static final String INVALID_TOKEN = null;

}
