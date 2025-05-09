package github_api.tests.repo;

import github_api.api.clients.RepoClient;
import github_api.api.config.ApiData;
import github_api.api.models.request.CreateRepoRequest;
import io.qameta.allure.Step;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.stream.Stream;

public class RepoCreate {

    @ParameterizedTest
    @MethodSource("testDataProvider")
    public void createRepo(CreateRepoRequest requestJson,
                           String token,
                           String endpoint,
                           int statusCode,
                           boolean validateSchema) {

        Response response = new RepoClient().createRepo(requestJson,
                token,
                endpoint
        );

        response.then()
                .log()
                .ifError()
                .statusCode(statusCode);

        if(validateSchema) {

            File schemaFile = new File("src/test/resources/github_create_repo_schema.json");

            response.then()
                    .assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
        }
    }

     static Stream<Arguments> testDataProvider() {
        return Stream.of(
                Arguments.of(requestJsonFull, ApiData.TOKEN, ApiData.ENDPOINT_USER_REPOS, 201, true),
                Arguments.of(requestJson, ApiData.TOKEN, ApiData.ENDPOINT_USER_REPOS, 201, true),
                Arguments.of(requestJsonFull, ApiData.TOKEN_WITHOUT_ACCESS, ApiData.ENDPOINT_REPOS, 404, false),
                Arguments.of(requestJsonInvalid, ApiData.TOKEN, ApiData.ENDPOINT_REPOS, 422, false),
                Arguments.of(requestJsonFull, ApiData.TOKEN, orgEndpoint, 404, false)
        );
    }

    static CreateRepoRequest requestJsonFull = CreateRepoRequest.builder()
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

    static CreateRepoRequest requestJson = CreateRepoRequest.builder()
            .name("test-repo-api-2")
            .build();

    static CreateRepoRequest requestJsonInvalid = CreateRepoRequest.builder()
            .description("test repo from API")
            .isPrivate(false)
            .build();

    static String orgEndpoint = "/orgs/" + ApiData.ORG + "/repos";

}
