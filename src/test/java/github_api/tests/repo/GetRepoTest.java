package github_api.tests.repo;

import github_api.api.clients.RepoClient;
import github_api.api.models.request.CreateRepoRequest;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.UUID;
import java.util.stream.Stream;

import static github_api.api.config.ApiConfig.*;
import static github_api.api.config.EnvConfig.*;
import static github_api.api.utils.RepoTestData.getRequestJsonFull;
import static github_api.api.utils.RepoTestData.getUpdateRequest;
import static github_api.api.utils.TestUtils.sleep;
import static org.hamcrest.Matchers.containsString;

@Story("GET /repos/{owner}/{repo}")
public class GetRepoTest {

    @ParameterizedTest
    @DisplayName("Проверка получения информации о репозитории")
    @Description("""
            Проверяет корректность работы эндпоинта:
            - Ответ имеет ожидаемый статус код
                200 OK – успешный запрос.
                403 Forbidden – нет доступа.
                404 Not Found – репозиторий не существует.
                301 Moved Permanently – репозиторий переименован.
            Примечание:
                Тест для кода ответа 301 исключен, из за особенностей Github api (задержка переименования).
            - Для успешных ответов проверяется соответствие JSON-схеме
            """)
    @MethodSource("testDataProvider")
    public void getRepo(CreateRepoRequest requestJson,
                       String repoName,
                       String token,
                       int statusCode,
                       boolean validateSchema,
                       boolean shouldCreateRepo) {

        File schemaFile = new File("src/test/resources/github_create_repo_schema.json");

        if (shouldCreateRepo) {
            new RepoClient().createRepo(requestJson, TOKEN, ENDPOINT_USER_REPOS);
        }

        Response response = new RepoClient().getRepo(LOGIN,
                repoName,
                token
        );

        try {
            response.then()
                    .log()
                    .ifError()
                    .statusCode(statusCode);
            if (validateSchema) {
                response.then()
                        .assertThat()
                        .body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
            }
        } finally {

               new RepoClient().deleteRepo(LOGIN, repoName, TOKEN);
        }
    }

    static Stream<Arguments> testDataProvider() {
        return Stream.of(
                Arguments.of(getRequestJsonFull(), "test-repo5fd9c581-d1ad-41b7-901e-a1f619804136", TOKEN, 200, true, true),
                Arguments.of(getRequestJsonFull(), "non-exist-repo", TOKEN, 404, false, false),
                Arguments.of(getRequestJsonFull(), "test-repo", INVALID_TOKEN, 401, false, true),
                Arguments.of(getRequestJsonFull(), "test-repo", TOKEN_WITHOUT_ACCESS, 403, false, true)
        );
    }

    @Disabled("GitHub API обрабатывает переименование репозитория с задержкой. " +
            "Этот тест требует отдельного запуска и проверки через несколько минут после переименования.")
    @Test
    @DisplayName("Проверка ответа 301 Moved Permanently")
    void getRepo_shouldReturn301WhenRenamed() {

        String originalName = "test-repo";

        String newName = originalName + UUID.randomUUID();

        CreateRepoRequest request = getRequestJsonFull().toBuilder()
                .name(originalName)
                .build();

        new RepoClient().createRepo(request, TOKEN, ENDPOINT_USER_REPOS);
        sleep(300);
        new RepoClient().updateRepo(LOGIN, originalName, getUpdateRequest(request, newName), TOKEN);

        Response response = new RepoClient().getRepo(LOGIN, originalName, TOKEN);
        response.then()
                .log()
                .body()
                .statusCode(301)
                .header("Location", containsString(newName));

        new RepoClient().deleteRepo(LOGIN, newName, TOKEN);
    }
}
