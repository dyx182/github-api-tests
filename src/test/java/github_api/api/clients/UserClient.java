package github_api.api.clients;

import github_api.api.config.ApiConfig;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;


public class UserClient {

    @Step("Получение информации о пользвателе по {userName}")
    public Response getUser(String userName) {
        return RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .when()
                .get(ApiConfig.ENDPOINT_USERS + userName);
    }

    @Step("Получение репозитория пользователя")
    public Response getUserRepos(String userName) {
        return RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .when()
                .get(ApiConfig.ENDPOINT_USERS + userName + "/repos");
    }
}
