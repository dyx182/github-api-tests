package github_api.tests.repo;

import github_api.api.clients.TestApiClients;
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
import static github_api.api.config.ApiConfig.getDeleteRepoEndpoint;
import static github_api.api.config.EnvConfig.*;
import static github_api.api.testdata.RepoTestData.*;

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

        //TODO Убарть из принимаемых переменных endpoint
        //TODO УБарть второй тест (с минимальными данными)

        File schemaFile = new File("src/test/resources/github_create_repo_schema.json");

        String uniqueRepoName = "test-repo" + UUID.randomUUID();

        String endpointDelete =  getDeleteRepoEndpoint(LOGIN, uniqueRepoName);

        CreateRepoRequest requestRepo = getUpdateRequest(requestJson, uniqueRepoName);

        if(shouldCreateDuplicate) {
            new TestApiClients<>().post(requestRepo, token, endpoint);
        }

        Response response = new TestApiClients<>().post(requestRepo, token, endpoint);

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
                new TestApiClients<>().delete(token, endpointDelete);
            }
        }
    }

    static Stream<Arguments> testDataProvider() {
        return Stream.of(
                Arguments.of(getRequestJsonFull(), TOKEN, getCreateRepoEndpoint(), 201, true, false),
                Arguments.of(getRequestJsonMinimal(), TOKEN, getCreateRepoEndpoint(), 201, true, false),
                Arguments.of(getRequestJsonFull(), TOKEN_WITHOUT_ACCESS, getCreateRepoEndpoint(), 403, false, false),
                Arguments.of(getRequestJsonFull(), INVALID_TOKEN, getCreateRepoEndpoint(), 401, false, false),
                Arguments.of(getRequestJsonInvalid(), TOKEN, getCreateRepoEndpoint(), 422, false, true),
                Arguments.of(getRequestJsonFull(), TOKEN, getOrgEndpoint(ORG), 404, false, false)
        );
    }
}
