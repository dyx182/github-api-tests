package github_api.tests.repo;

import github_api.api.clients.RepoClient;
import github_api.api.config.ApiData;
import github_api.api.models.request.CreateRepoRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class RepoUpdate {

    @Test
    @Step("Проверка обновления репозитория")
    public void updateRepo() {
        CreateRepoRequest changeRepoJson = CreateRepoRequest.builder()
                .name("test-repo-updated-")
                .description("Updated description via API")
                .homepage("https://updated.example.com")
                .isPrivate(false)
                .hasIssues(true)
                .hasProjects(true)
                .hasWiki(true)
                .hasDiscussions(false)
                .isTemplate(false)
                .allowSquashMerge(true)
                .allowMergeCommit(true)
                .allowRebaseMerge(true)
                .allowAutoMerge(false)
                .deleteBranchOnMerge(false)
                .useSquashPrTitleAsDefault(false)
                .squashMergeCommitTitle("PR_TITLE")
                .squashMergeCommitMessage("COMMIT_MESSAGES")
                .mergeCommitTitle("MERGE_MESSAGE")
                .mergeCommitMessage("PR_TITLE")
                .build();

        Response response = new RepoClient().updateRepo(
                ApiData.LOGIN,
                "test-repo-api",
                changeRepoJson,
                ApiData.TOKEN
        );

        response.then()
                .statusCode(200)
                .body("name", equalTo(changeRepoJson.getName()))
                .body("description", equalTo(changeRepoJson.getDescription()));
    }

    @Test
    @Step("Проверка обновления не существующего репозитория")
    public void updateNonExistRepo() {
        CreateRepoRequest changeRepoJson = CreateRepoRequest.builder()
                .name("non-exist-repo-")
                .build();

        Response response = new RepoClient().updateRepo(
                ApiData.LOGIN,
                "non-exist-repo",
                changeRepoJson,
                ApiData.TOKEN
        );

        response.then()
                .statusCode(404)
                .log()
                .body();
    }

    @Test
    @Step("Проверка обновления репо с токеном без прав доступа")
    public void updateRepoWitTokenWithoutAccess() {
        CreateRepoRequest changeRepoJson = CreateRepoRequest.builder()
                .name("new-repo")
                .build();

        Response response = new RepoClient().updateRepo(
                ApiData.LOGIN,
                "test-repo-api",
                changeRepoJson,
                ApiData.TOKEN_WITHOUT_ACCESS
        );

        response.then()
                .statusCode(404)
                .log()
                .body();
    }

}
