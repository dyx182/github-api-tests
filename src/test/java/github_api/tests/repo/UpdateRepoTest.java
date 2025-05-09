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

import java.util.stream.Stream;

import static github_api.api.config.ApiData.*;
import static github_api.api.config.repo.RepoTestData.changeNonExistRepoJson;
import static github_api.api.config.repo.RepoTestData.changeRepoJson;
import static org.hamcrest.Matchers.equalTo;

public class RepoUpdate {

    @ParameterizedTest
    @MethodSource("testDataProvider")
    public void updateRepo(String login,
                           String repoName,
                           CreateRepoRequest changeRepoJson,
                           String token,
                           int statusCode,
                           boolean validateSchema) {

        Response response = new RepoClient().updateRepo(
                login,
                repoName,
                changeRepoJson,
                token
        );

        response.then()
                .log()
                .ifError()
                .statusCode(statusCode);

        if(validateSchema) {
            response.then()
                    .body("name", equalTo(changeRepoJson.getName()))
                    .body("description", equalTo(changeRepoJson.getDescription()));
        }
    }

    static Stream<Arguments> testDataProvider() {
        return Stream.of(
                Arguments.of(LOGIN, "test-repo-api", changeRepoJson(), TOKEN, 200, true),
                Arguments.of(LOGIN, "non-exist-repo", changeNonExistRepoJson(), TOKEN, 404, false),
                Arguments.of(LOGIN, "test-repo-api", changeRepoJson(), TOKEN, 307, false),
                Arguments.of(LOGIN, "test-repo-api-2", changeRepoJson(), TOKEN_WITHOUT_ACCESS, 404, false)
        );
    }

}
