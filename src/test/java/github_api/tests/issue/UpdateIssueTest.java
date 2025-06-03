package github_api.tests.issue;


import github_api.api.clients.TestApiClients;
import github_api.api.models.request.UpdateIssueRequest;
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
import static github_api.api.config.EnvConfig.TOKEN;
import static github_api.api.testdata.IssueTestData.*;
import static github_api.api.testdata.IssueTestData.getFullCreateIssue;
import static github_api.api.testdata.RepoTestData.getRequestJsonFull;

@Story("PATCH /repos/{owner}/{repo}/issues/{issue_number}")
public class UpdateIssueTest {
    @ParameterizedTest
    @DisplayName("Создание сущности")
    @Description("""
            Тест проверяет корректность основных сценариев получения сущностей в репозитории:
            - Ответ имеет ожидаемый статус код:
                200 OK – успешный запрос.
                404 Not Found – репозиторий не существует или нет доступа.
                422 Unprocessable Entity – ошибка валидации.
                401 Bad credentials - неверные данные для входа.
                403 Forbidden – нет прав на редактирование Issue.
            - Для успешных ответов происходит проверка соответсвия JSON-схеме
            """)
    @MethodSource("testDataProvider")
    public void createIssue(
            UpdateIssueRequest requestJson,
            String token,
            int expectedCode,
            boolean validateSchema,
            boolean shouldCreateRepo
    ) {

        File schemaFile = new File("src/test/resources/github_create_issue_schema.json");

        String repoName = getRequestJsonFull().getName();
        String endpointUpdateIssue = getUpdateIssueEndpoint(LOGIN, repoName, 1);
        String endpointCreateIssue = getCreateIssueEndpoint(LOGIN, repoName);
        String endpointDeleteRepo = getDeleteRepoEndpoint(LOGIN, repoName);

        if (shouldCreateRepo) {
            new TestApiClients<>().post(getRequestJsonFull(), TOKEN, getCreateRepoEndpoint());
            new TestApiClients<>().post(getFullCreateIssue(), TOKEN, endpointCreateIssue);
        }
        Response updateIssue = new TestApiClients<>().patch(requestJson, token, endpointUpdateIssue);

        try {
            updateIssue.then()
                    .log()
                    .ifError()
                    .statusCode(expectedCode);
            if (validateSchema) {
                updateIssue.then()
                        .assertThat()
                        .body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
            }
        } finally {
            if(shouldCreateRepo || validateSchema) {
                new TestApiClients<>().delete(TOKEN, endpointDeleteRepo);
            }
        }
    }

    static Stream<Arguments> testDataProvider() {
        return Stream.of(
                Arguments.of(getUpdateFields(), TOKEN, 200, true, true),
                Arguments.of(getUpdateFields(), TOKEN, 404, false, false),
                Arguments.of(getInvalidUpdateFields(), TOKEN, 422, false, true),
                Arguments.of(getInvalidUpdateFields(), INVALID_TOKEN, 401, false, true),
                Arguments.of(getInvalidUpdateFields(), TOKEN_WITHOUT_ACCESS, 403, false, true)
        );
    }
}

