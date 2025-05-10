package github_api.api.config;

import io.github.cdimascio.dotenv.Dotenv;

public class ApiData {

    private static final Dotenv dotenv = Dotenv.load();

    public static final String BASE_URL = "https://api.github.com";
    public static final String ENDPOINT_USERS = "/users/";
    public static final String ORG_END_POINT = "/orgs/" + ApiData.ORG + "/repos";
    public static final String ENDPOINT_USER_REPOS = "/user/repos";
    public static final String ENDPOINT_REPOS = "/repos/";

    public static final String ORG = dotenv.get("GITHUB_ORG");
    public static final String LOGIN = dotenv.get("GITHUB_LOGIN"); //your login here

    public static final String TOKEN = dotenv.get("GITHUB_TOKEN");//your token  here
    public static final String INVALID_TOKEN = null;
    public static final String TOKEN_WITHOUT_ACCESS = dotenv.get("GITHUB_TOKEN_WITHOUT_ACCESS"); //your token  here
}
