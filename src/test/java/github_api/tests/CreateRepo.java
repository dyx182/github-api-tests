package github_api.tests;

import github_api.api.clients.RepoClient;
import github_api.api.config.ApiData;
import github_api.api.models.request.CreateRepoRequest;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;

public class CreateRepo {

    @Test
    @Step("Создание репо")
    public void createNewRepo() {

        CreateRepoRequest requestJson = CreateRepoRequest.builder()
                .name("test-repo-api")
                .description("test repo from API")
                .isPrivate(false)
                .build();

        new RepoClient().createRepo(requestJson, ApiData.TOKEN, ApiData.ENDPOINT_USER_REPOS)
                .then()
                .statusCode(201)
                .log()
                .body();
    }

    @Test
    @Step("Создание репо с обязательным параметром")
    public void createRepoWithRequiredParam() {

        CreateRepoRequest requestJson = CreateRepoRequest.builder()
                .name("test-repo-api")
                .build();

        new RepoClient().createRepo(requestJson, ApiData.TOKEN, ApiData.ENDPOINT_USER_REPOS)
                .then()
                .statusCode(201)
                .log()
                .body();
    }

    @Test
    @Step("Создание репо c невлидным токеном")
    public void createRepoWithoutRequiredParam() {

        CreateRepoRequest requestJson = CreateRepoRequest.builder()
                .name("test-repo-api")
                .build();

        new RepoClient().createRepo(requestJson, "Token", ApiData.ENDPOINT_USER_REPOS)
                .then()
                .statusCode(401)
                .log()
                .body();
    }

    @Test
    @Step("Создание репо с неверным типом данных")
    public void createRepoWithUnvalidDataType() {

        CreateRepoRequest requestJson = CreateRepoRequest.builder()
                .description("test repo from API")
                .isPrivate(false)
                .build();

        new RepoClient().createRepo(requestJson, ApiData.TOKEN, ApiData.ENDPOINT_USER_REPOS)
                .then()
                .statusCode(422)
                .log()
                .body();
    }

    @Test
    @Step("Создание репо в профиле несуществующей организации")
    public void createRepoWithInvalidToken() {

        String orgEndpoint = "/orgs/" + ApiData.ORG + "/repos";

        CreateRepoRequest requestJson = CreateRepoRequest.builder()
                .name("test-repo-api")
                .build();

        new RepoClient().createRepo(requestJson, ApiData.TOKEN, orgEndpoint)
                .then()
                .statusCode(404)
                .log()
                .body();
    }
}
