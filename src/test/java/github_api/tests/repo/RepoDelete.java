package github_api.tests.repo;

import github_api.api.clients.RepoClient;
import github_api.api.config.ApiData;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class RepoDelete {

    @Test
    @Step("Провекра удаления существующего репо")
    public void deleteRepo() {

        Response response = new RepoClient().deleteRepo(ApiData.LOGIN,
                "test-repo-api",
                ApiData.TOKEN
        );

        response.then()
                .statusCode(204)
                .log()
                .body();
    }

    @Test
    @Step("Проверка удаления не существующего репо")
    public void deleteANonExistentRepo() {

        Response response = new RepoClient().deleteRepo(ApiData.LOGIN,
                "non-exist-repo",
                ApiData.TOKEN
        );

        response.then()
                .statusCode(404)
                .log()
                .body();
    }

    @Test
    @Step("Провекра удаления репо с токеном без прав доступа")
    public void deleteRepoWitTokenWithoutAccess() {

        Response response = new RepoClient().deleteRepo(ApiData.LOGIN,
                "test-repo-api",
                ApiData.TOKEN_WITHOUT_ACCESS
        );

        response.then()
                .statusCode(403)
                .log()
                .body();
    }

}
