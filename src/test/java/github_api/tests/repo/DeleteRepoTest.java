package github_api.tests.repo;

import github_api.api.clients.RepoClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static github_api.api.config.ApiData.*;

public class RepoDelete {

    @ParameterizedTest
    @DisplayName("Проверка возможности удаления репозитория")
    @Description()
    @MethodSource("testDataProvider")
    public void deleteRepo(String login, String repoName, String token, int statusCode) {

        Response response = new RepoClient().deleteRepo(login,
                repoName,
                token
        );

        response.then()
                .log()
                .ifError()
                .statusCode(statusCode);
    }

    static Stream<Arguments> testDataProvider() {
        return Stream.of(
                Arguments.of(LOGIN, "test-repo-api", TOKEN, 204),
                Arguments.of(LOGIN, "non-exist-repo", TOKEN, 404),
                Arguments.of(LOGIN,"test-repo-api", TOKEN_WITHOUT_ACCESS, 403)
        );
    }

}
