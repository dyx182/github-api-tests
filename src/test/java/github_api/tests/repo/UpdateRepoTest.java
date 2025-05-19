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

import static github_api.api.config.ApiConfig.ENDPOINT_REPOS;
import static github_api.api.config.ApiConfig.ENDPOINT_USER_REPOS;
import static github_api.api.config.EnvConfig.*;
import static github_api.api.utils.RepoTestData.*;
import static github_api.api.utils.TestUtils.sleep;
import static org.hamcrest.Matchers.equalTo;

@Story("PATCH /repos/{owner}/{repo}")
public class UpdateRepoTest {

    @ParameterizedTest
    @DisplayName("Проверка обновления информации в репозитории")
    @Description("""
            Проверяет корректность работы эндпоинта:
            - Ответ имеет ожидаемый статус код
                200 OK – успешное обновление.
                404 Not Found – репозиторий не найден.
                403 Forbidden – нет прав.
                401 Unauthorized – нет авторизации.
                422 Unprocessable Entity – ошибка валидации.
            - Для успешных ответов проверяется соответствие JSON-схеме
            """)
    @MethodSource("testDataProvider")
    public void updateRepo(CreateRepoRequest requestJson,
                           String login,
                           String repoName,
                           CreateRepoRequest changeRepoJson,
                           String token,
                           int statusCode,
                           boolean validateSchema,
                           boolean shouldCreateRepo) {


        String endpointUpdate = String.format("/%s/%s/%s", ENDPOINT_REPOS, login, repoName);
        String endpointUpdateDelete = String.format("/%s/%s/%s", ENDPOINT_REPOS, login, changeRepoJson.getName());

        if (shouldCreateRepo) {
            new TestApiClients<>().post(requestJson, TOKEN, ENDPOINT_USER_REPOS);
        }

        sleep(300);

        Response response = new TestApiClients<>().patch(changeRepoJson, token, endpointUpdate);
        try {
            response.then()
                    .log()
                    .ifError()
                    .statusCode(statusCode);

            if (validateSchema) {
                response.then()
                        .body("name", equalTo(changeRepoJson.getName()))
                        .body("description", equalTo(changeRepoJson.getDescription()));
            }
        } finally {
            if (validateSchema) {
                new TestApiClients<>().delete(TOKEN, endpointUpdateDelete);
            }
            else {
                new TestApiClients<>().delete(TOKEN, endpointUpdate);
            }
        }
    }

    static Stream<Arguments> testDataProvider() {
        return Stream.of(
                Arguments.of(getRequestJsonFull(), LOGIN, "test-repo", changeRepoJson(), TOKEN, 200, true, true),
                Arguments.of(getRequestJsonFull(), LOGIN, "non-exist-repo", changeRepoJson(), TOKEN, 404, false, false),
                Arguments.of(getRequestJsonFull(), LOGIN, "test-repo", changeRepoJson(), TOKEN_WITHOUT_ACCESS, 403, false, true),
                Arguments.of(getRequestJsonFull(), LOGIN, "test-repo", changeInvalidRepoJson(), TOKEN, 422, false, true),
                Arguments.of(getRequestJsonFull(), LOGIN, "test-repo", changeRepoJson(), INVALID_TOKEN, 401, false, true)
        );
    }
}
