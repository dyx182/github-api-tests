package github_api.tests.repo;

import github_api.api.clients.RepoClient;
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
import java.util.UUID;
import java.util.stream.Stream;

import static github_api.api.config.ApiConfig.*;
import static github_api.api.config.EnvConfig.*;
import static github_api.api.utils.RepoTestData.*;


@Story("POST /repos/")
public class CreateRepoTest {

    @ParameterizedTest
    @DisplayName("Проверка возможности создания репозитория")
    @Description("""
            Тест проверяет корректность основных сценариев создания репозитория:
            - Ответ имеет ожидаемый статус код:
                201 Created – успешное создание.
                403 Forbidden – нет прав.
                401 Unauthorized – нет авторизации.
                422 Unprocessable Entity – ошибка валидации.
                404 Not Found – организация не найдена.
            - Для успешных ответов происходит проверка соответсвия JSON-схеме
            """)
    @MethodSource("testDataProvider")
    public void createRepo(CreateRepoRequest requestJson,
                           String token,
                           String endpoint,
                           int statusCode,
                           boolean validateSchema,
                           boolean shouldCreateDuplicate) {

        File schemaFile = new File("src/test/resources/github_create_repo_schema.json");

        String uniqueRepoName = "test-repo" + UUID.randomUUID();

        CreateRepoRequest request = getUpdateRequest(requestJson, uniqueRepoName);

        if(shouldCreateDuplicate) {
            new RepoClient().createRepo(request, token, endpoint);
        }

        Response response = new RepoClient().createRepo(request,
                token,
                endpoint
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
            if (validateSchema || shouldCreateDuplicate) {
                new RepoClient().deleteRepo(LOGIN, uniqueRepoName, TOKEN);
            }
        }
    }

    static Stream<Arguments> testDataProvider() {
        return Stream.of(
                Arguments.of(getRequestJsonFull(), TOKEN, ENDPOINT_USER_REPOS, 201, true, false),
                Arguments.of(getRequestJsonMinimal(), TOKEN, ENDPOINT_USER_REPOS, 201, true, false),
                Arguments.of(getRequestJsonFull(), TOKEN_WITHOUT_ACCESS, ENDPOINT_USER_REPOS, 403, false, false),
                Arguments.of(getRequestJsonFull(), INVALID_TOKEN, ENDPOINT_USER_REPOS, 401, false, false),
                Arguments.of(getRequestJsonInvalid(), TOKEN, ENDPOINT_USER_REPOS, 422, false, true),
                Arguments.of(getRequestJsonFull(), TOKEN, getOrgEndpoint(ORG), 404, false, false)
        );
    }
}
