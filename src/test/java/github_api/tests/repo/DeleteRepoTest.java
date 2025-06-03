package github_api.tests.repo;

import github_api.api.clients.TestApiClients;
import github_api.api.models.request.CreateRepoRequest;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static github_api.api.config.ApiConfig.*;
import static github_api.api.config.ApiConfig.getCreateRepoEndpoint;
import static github_api.api.config.EnvConfig.*;
import static github_api.api.testdata.RepoTestData.getRequestJsonFull;

@Story("DELETE /repos/{owner}/{repo}")
public class DeleteRepoTest {

    @ParameterizedTest
    @DisplayName("Проверка возможности удаления репозитория")
    @Description("""
            Проверяет корректность работы эндпоинта:
            - Ответ имеет ожидаемый статус код
                204 No Content – успешное удаление.
                403 Forbidden – нет прав.
                404 Not Found – репозиторий не существует.
        """)
    @MethodSource("testDataProvider")
    public void deleteRepo(CreateRepoRequest requestJson,
                           String repoName,
                           String token,
                           String endpoint,
                           int statusCode,
                           boolean shouldCreateRepo) {

        String endpointDelete =  getDeleteRepoEndpoint(LOGIN, repoName);

        if (shouldCreateRepo) {
            new TestApiClients<>().post(requestJson, TOKEN, endpoint);
        }

        Response response = new TestApiClients<>().delete(token, endpointDelete);
            response.then()
                    .log()
                    .ifError()
                    .statusCode(statusCode);
    }

    static Stream<Arguments> testDataProvider() {
        return Stream.of(
                Arguments.of(getRequestJsonFull(), "test-repo", TOKEN, getCreateRepoEndpoint(), 204, true),
                Arguments.of(getRequestJsonFull(), "non-exist-repo", TOKEN, getCreateRepoEndpoint(), 404, false),
                Arguments.of(getRequestJsonFull(), "test-repo", TOKEN_WITHOUT_ACCESS, getCreateRepoEndpoint(), 403, false),
                Arguments.of(getRequestJsonFull(), "test-repo", TOKEN_WITHOUT_ACCESS, getCreateRepoEndpoint(), 401, false)
        );
    }
}
