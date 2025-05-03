package github_api.tests.repo;

import github_api.api.clients.RepoClient;
import github_api.api.config.ApiData;
import io.qameta.allure.Step;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.File;

public class RepoGet {

    @Test
    @Step("Проверка получения информации о репозитории")
    public void getRepo() {

        File schemaFile = new File("src/test/resources/github_create_repo_schema.json");

        Response response = new RepoClient().getRepo(ApiData.LOGIN,
                "test-repo-api",
                ApiData.TOKEN
        );

        response.then()
                .statusCode(200)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
    }

    @Test
    @Step("Проверка получения информации о не существующем репозитории")
    public void getRepos() {

        File schemaFile = new File("src/test/resources/github_create_repo_schema.json");

        Response response = new RepoClient().getRepo(ApiData.LOGIN,
                "non-exist-repo",
                ApiData.TOKEN
        );

        response.then()
                .statusCode(404)
                .log()
                .body();
    }

    @Test
    @Step("Проверка получения информации о репозитории без токена")
    public void getReposWithoutToken() {

        File schemaFile = new File("src/test/resources/github_create_repo_schema.json");

        Response response = new RepoClient().getRepo(ApiData.LOGIN,
                "test-repo-api",
                "TOKEN"
        );

        response.then()
                .statusCode(401)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
    }



}
