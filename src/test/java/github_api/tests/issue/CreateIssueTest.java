package github_api.tests.issue;

import github_api.api.clients.TestApiClients;
import github_api.api.models.request.CreateIssueRequest;
import io.qameta.allure.Story;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.stream.Stream;

import static github_api.api.config.ApiConfig.*;
import static github_api.api.config.EnvConfig.*;
import static github_api.api.testdata.IssueTestData.getFullCreateIssue;
import static github_api.api.testdata.IssueTestData.getInvalidFields;
import static github_api.api.testdata.RepoTestData.getRequestJsonFull;

@Story("POST /repos/{owner}/{repo}/issues")
public class CreateIssueTest {


    @ParameterizedTest
    @DisplayName("Создание сущности")
    @Description("""
            Тест проверяет корректность основных сценариев создания сущностей в репозитории:
            - Ответ имеет ожидаемый статус код:
                201 Created – Issue успешно создан.
                403 Forbidden – нет прав на создание Issue.
                404 Not Found – репозиторий не существует.
                401 Bad credentials - неверные данные для входа.
                422 Unprocessable Entity – ошибка валидации (например, пустой title).
            - Для успешных ответов происходит проверка соответсвия JSON-схеме
            """)
    @MethodSource("testDataProvider")
    public void createIssue(
            CreateIssueRequest requestJson,
            String token,
            int expectedCode,
            boolean validateSchema,
            boolean shouldCreateRepo
    ) {

        File schemaFile = new File("src/test/resources/github_create_issue_schema.json");

        String endpointCreateIssue = getCreateOrGetIssueEndpoint(LOGIN, getRequestJsonFull().getName());
        String endpointDeleteRepo = getDeleteRepoEndpoint(LOGIN, getRequestJsonFull().getName());

        if (shouldCreateRepo) {
            new TestApiClients<>().post(getRequestJsonFull(), TOKEN, getCreateRepoEndpoint());
        }

        Response createIssue = new TestApiClients<>().post(requestJson, token, endpointCreateIssue);

        try {
            createIssue.then()
                    .log()
                    .ifError()
                    .statusCode(expectedCode);
            if (validateSchema) {
                createIssue.then()
                        .assertThat()
                        .body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
            }
        }

        finally {
            if (validateSchema || shouldCreateRepo) {
                new TestApiClients<>().delete(TOKEN, endpointDeleteRepo);
            }
        }
    }

    static Stream<Arguments> testDataProvider() {
        return Stream.of(
                Arguments.of(getFullCreateIssue(), TOKEN, 201, true, true),
                Arguments.of(getFullCreateIssue(), TOKEN_WITHOUT_ACCESS, 403, true, true),
                Arguments.of(getFullCreateIssue(), TOKEN, 404, false, false),
                Arguments.of(getFullCreateIssue(), INVALID_TOKEN, 401, false, false),
                Arguments.of(getInvalidFields(), TOKEN, 422, false, true)
        );
    }
}
