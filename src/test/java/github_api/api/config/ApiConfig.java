package github_api.api.config;

public class ApiConfig {


    public static final String BASE_URL = "https://api.github.com";
    public static final String ENDPOINT_USERS = "/users/";
    public static final String ENDPOINT_USER_REPOS = "/user/repos";
    public static final String ENDPOINT_REPOS = "repos";

    public static String getOrgEndpoint(String org) {
        return "/orgs/" + org + "/repos";
    }

}
