package github_api.tests.issue;

import github_api.api.clients.TestApiClients;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static github_api.api.config.ApiConfig.*;
import static github_api.api.config.ApiConfig.getCreateOrGetIssueEndpoint;
import static github_api.api.config.EnvConfig.*;
import static github_api.api.config.EnvConfig.TOKEN;
import static github_api.api.testdata.IssueTestData.getFullCreateIssue;
import static github_api.api.testdata.RepoTestData.getRequestJsonFull;


@Story("GET /repos/{owner}/{repo}/issues")
public class GetIssueTest {


    @ParameterizedTest
    @DisplayName("Создание сущности")
    @Description("""
            Тест проверяет корректность основных сценариев получения сущностей репозитория
            В данном тесте сущности не исполльзуется фильтрация, т.к. проверяются основные сценарии
            - Ответ имеет ожидаемый статус код:
                200 OK – успешный запрос.
                301 Moved Permanently – репозиторий переименован (редирект на новый URL).
                404 Not Found – репозиторий не существует или нет доступа.
            - Для успешных ответов происходит проверка соответсвия JSON-схеме
            """)
    @MethodSource("testDataProvider")
    public void getIssue(
            String token,
            int expectedCode,
            boolean shouldCreateRepo
    ) {

        String endpointCreateIssue = getCreateOrGetIssueEndpoint(LOGIN, getRequestJsonFull().getName());
        String endpointDeleteRepo = getDeleteRepoEndpoint(LOGIN, getRequestJsonFull().getName());

        if (shouldCreateRepo) {
            new TestApiClients<>().post(getRequestJsonFull(), TOKEN, getCreateRepoEndpoint());
            new TestApiClients<>().post(getFullCreateIssue(), TOKEN, endpointCreateIssue);
            new TestApiClients<>().post(getFullCreateIssue(), TOKEN, endpointCreateIssue);
        }

        Response getIssue = new TestApiClients<>().get(token, endpointCreateIssue);

        getIssue.then()
                .log()
                .body()
                .statusCode(expectedCode);

        if (shouldCreateRepo) {
            new TestApiClients<>().delete(TOKEN, endpointDeleteRepo);
        }
    }

    static Stream<Arguments> testDataProvider() {
        return Stream.of(
                Arguments.of(TOKEN, 200, true),
                Arguments.of(TOKEN, 404, false),
                Arguments.of(INVALID_TOKEN, 401, true)
        );
    }
}

