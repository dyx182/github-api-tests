package github_api.tests.repo;

import github_api.api.clients.RepoClient;
import github_api.api.config.ApiData;
import github_api.api.config.repo.RepoTestData;
import github_api.api.models.request.CreateRepoRequest;
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

import static github_api.api.config.ApiData.*;
import static github_api.api.config.repo.RepoTestData.*;


@Story("POST /repos/")
public class CreateRepoTest {

    @ParameterizedTest
    @DisplayName("Проверка возможности создания репозитория")
    @Description("""
            Проверяет корректность основных сценариев создания репозитория:
            - Ответ имеет ожидаемый статус код
            - Для успешных ответов происходит проверка соответсвия JSON-схеме
            """)
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
                Arguments.of(getRequestJsonFull(), TOKEN, ENDPOINT_USER_REPOS, 201, true),
                Arguments.of(getRequestJsonMinimal(), TOKEN, ENDPOINT_USER_REPOS, 201, true),
                Arguments.of(getRequestJsonFull(), TOKEN_WITHOUT_ACCESS, ENDPOINT_REPOS, 404, false),
                Arguments.of(getRequestJsonInvalid(), TOKEN, ENDPOINT_REPOS, 422, false),
                Arguments.of(getRequestJsonFull(), TOKEN, ORG_END_POINT, 404, false)
        );
    }
}
