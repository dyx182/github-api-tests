package github_api.tests.repo;

import github_api.api.clients.RepoClient;
import github_api.api.config.ApiData;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.io.File;
import java.util.stream.Stream;

@Story("GET /repos/{owner}/{repo}")
public class GetRepoTest {

    @ParameterizedTest
    @DisplayName("Проверка получения информации о репозитории")
    @Description("""
        Проверяет корректность работы эндпоинта:
        - Ответ имеет ожидаемый статус код
        - Для успешных ответов проверяется соответствие JSON-схеме
        """)
    @MethodSource("testDataProvider")
    public void getRep(String repoName, String token, int statusCode, boolean validateSchema) {

        Response response = new RepoClient().getRepo(ApiData.LOGIN,
                repoName,
                token
        );

        response.then()
                .log()
                .ifError()
                .statusCode(statusCode);

        if (validateSchema) {
            File schemaFile = new File("src/test/resources/github_create_repo_schema.json");

            response.then()
                    .assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
        }
    }
    static Stream<Arguments> testDataProvider() {
        return Stream.of(
                Arguments.of("test-repo-api", ApiData.TOKEN, 200, true),
                Arguments.of("non-exist-repo", ApiData.TOKEN, 404, false),
                Arguments.of("test-repo-api", "token", 401, false)
        );
    }
}
