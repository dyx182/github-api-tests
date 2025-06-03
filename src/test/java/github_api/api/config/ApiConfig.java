package github_api.api.config;


import static github_api.api.utils.TestUtils.urlFormatRepo;

public class ApiConfig {

    public static final String BASE_URL = "https://api.github.com";

    public static String getCreateRepoEndpoint() {
        return "/user/repos";
    }

    public static String getUpdateRepoEndpoint(String login, String repoName) {
        return urlFormatRepo("repos", login, repoName);
    }

    public static String getOrgEndpoint(String org) {
        return urlFormatRepo("orgs", org, "repos");
    }

    public static String getDeleteRepoEndpoint(String login, String repoName) {
        return urlFormatRepo("repos", login, repoName);
    }




//TODO Переработать, для не авторизованной зоны
    public static final String ENDPOINT_USERS = "users";

}
