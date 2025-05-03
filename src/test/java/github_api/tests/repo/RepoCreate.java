package github_api.tests.repo;

import github_api.api.clients.RepoClient;
import github_api.api.config.ApiData;
import github_api.api.models.request.CreateRepoRequest;
import io.qameta.allure.Step;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.File;

public class RepoCreate {

    @Test
    @Step("Проверка создания репо со всеми возможными полями")
    public void createNewRepo() {

        File schemaFile = new File("src/test/resources/github_create_repo_schema.json");

        CreateRepoRequest requestJson = CreateRepoRequest.builder()
                .name("test-repo-api")
                .description("test repo from API")
                .homepage("https://github.com")
                .isPrivate(false)
                .hasIssues(true)
                .hasProjects(true)
                .hasWiki(true)
                .hasDiscussions(false)
                .isTemplate(false)
                .autoInit(false)
                .gitignoreTemplate("Java")
                .licenseTemplate("mit")
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

        Response response = new RepoClient().createRepo(requestJson,
                ApiData.TOKEN,
                ApiData.ENDPOINT_USER_REPOS
        );

        response.then()
                .statusCode(201)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
    }

    @Test
    @Step("Проверка создания репо только с обязательными параметрами")
    public void createRepoWithRequiredParam() {

        File schemaFile = new File("src/test/resources/github_create_repo_schema.json");

        CreateRepoRequest requestJson = CreateRepoRequest.builder()
                .name("test-repo-api")
                .build();

        Response response = new RepoClient().createRepo(requestJson,
                ApiData.TOKEN,
                ApiData.ENDPOINT_USER_REPOS
        );

        response.then()
                .statusCode(201)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
    }

    @Test
    @Step("Проверка создания репо c токеном без прав доступа")
    public void createRepoWithoutRequiredParam() {

        CreateRepoRequest requestJson = CreateRepoRequest.builder()
                .name("test-repo-api")
                .build();

        Response response = new RepoClient().createRepo(requestJson,
                ApiData.TOKEN_WITHOUT_ACCESS,
                ApiData.ENDPOINT_USER_REPOS
        );

        response.then()
                .statusCode(404)
                .log()
                .body();
    }

    @Test
    @Step("Проверка создания репо с неверным типом данных")
    public void createRepoWithUnvalidDataType() {

        CreateRepoRequest requestJson = CreateRepoRequest.builder()
                .description("test repo from API")
                .isPrivate(false)
                .build();

        Response response = new RepoClient().createRepo(requestJson,
                ApiData.TOKEN,
                ApiData.ENDPOINT_USER_REPOS
        );

        response.then()
                .statusCode(422)
                .log()
                .body();
    }

    @Test
    @Step("Проверка создания репо в профиле несуществующей организации")
    public void createRepoWithInvalidToken() {

        String orgEndpoint = "/orgs/" + ApiData.ORG + "/repos";

        CreateRepoRequest requestJson = CreateRepoRequest.builder()
                .name("test-repo-api")
                .build();

        Response response = new RepoClient().createRepo(requestJson,
                ApiData.TOKEN,
                orgEndpoint
        );

        response.then()
                .statusCode(404)
                .log()
                .body();
    }
}
