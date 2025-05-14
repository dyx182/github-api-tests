package github_api.tests.repo;

import github_api.api.clients.RepoClient;
import github_api.api.models.request.CreateRepoRequest;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static github_api.api.config.ApiConfig.ENDPOINT_REPOS;
import static github_api.api.config.ApiConfig.ENDPOINT_USER_REPOS;
import static github_api.api.config.EnvConfig.*;
import static github_api.api.utils.RepoTestData.getRequestJsonFull;

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
                           String login,
                           String repoName,
                           String token,
                           int statusCode,
                           boolean shouldCreateRepo) {

        //TODO Вынести в общий класс именно, как формат
        String endpointDelete =  String.format("/%s/%s/%s",ENDPOINT_REPOS, LOGIN, repoName);

        if (shouldCreateRepo) {
            new RepoClient().createRepo(requestJson, token, ENDPOINT_USER_REPOS);
        }

        Response response = new RepoClient().deleteRepo(token, endpointDelete);
            response.then()
                    .log()
                    .ifError()
                    .statusCode(statusCode);
    }

    static Stream<Arguments> testDataProvider() {
        return Stream.of(
                Arguments.of(getRequestJsonFull(), LOGIN, "test-repo", TOKEN, 204, true),
                Arguments.of(getRequestJsonFull(), LOGIN, "non-exist-repo", TOKEN, 404, false),
                Arguments.of(getRequestJsonFull(), LOGIN, "test-repo", TOKEN_WITHOUT_ACCESS, 403, false),
                Arguments.of(getRequestJsonFull(), LOGIN, "test-repo", TOKEN_WITHOUT_ACCESS, 401, false)
        );
    }
}
