package github_api.tests;

import github_api.api.clients.TestApiClients;
import github_api.api.clients.UserClient;
import github_api.api.models.response.GetUserResponse;
import github_api.api.models.response.GetUserRepoResponse;
import io.qameta.allure.Story;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static github_api.api.config.ApiConfig.ENDPOINT_USERS;
import static github_api.api.config.EnvConfig.LOGIN;
import static github_api.api.config.EnvConfig.TOKEN;

public class UserTests {

    @Test
    public void getUserInfoOnUserName() {
        File schemaFile = new File("src/test/resources/github_users_schema.json");
        String endpoint = ENDPOINT_USERS + LOGIN;

        Response response = new TestApiClients<>().get(TOKEN, endpoint);

        response.then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(schemaFile));

        GetUserResponse user = response.then()
                .statusCode(200)
                .extract().as(GetUserResponse.class);
        System.out.println(user.toString());
    }

    @Test
    public void getUserInfoOnNonExistUserName() {
        new UserClient().getUser("аывтамг123323")
                .then()
                .statusCode(404);
    }

    @Test
    @Story("Проверка работы и получение ТОП 3 репозитория пользователя")
    public void getUserRepos() {
        File schemaFile = new File("src/test/resources/github_users_repo_schema.json");
        Response response = new UserClient().getUserRepos(LOGIN);

        response.then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(schemaFile));

        List<GetUserRepoResponse> getRepoResponses = response.then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", GetUserRepoResponse.class);

        List<GetUserRepoResponse> topGetRepoResponses = getRepoResponses.stream()
                .filter(r -> r.getStargazers_count() >= 0)
                .sorted((r1, r2) -> r2.getStargazers_count() - r1.getStargazers_count())
                .limit(3)
                .toList();

        System.out.println("Топ 3 репозитория:");
        topGetRepoResponses.forEach(System.out::println);
    }
}

