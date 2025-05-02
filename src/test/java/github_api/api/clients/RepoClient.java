package github_api.api.clients;

import com.google.gson.Gson;
import github_api.api.config.ApiData;
import github_api.api.models.request.CreateRepoRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class RepoClient {

    private static final Gson gson = new Gson();

    public Response createRepo(CreateRepoRequest requestJson, String token, String endPoint) {

        String repoJson = gson.toJson(requestJson);

        return RestAssured.given()
                .baseUri(ApiData.BASE_URL)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(repoJson)
                .when()
                .post(endPoint);
    }

    public Response deleteRepo(String owner, String repoName, String token) {

        return RestAssured.given()
                .baseUri(ApiData.BASE_URL)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(ApiData.ENDPOINT_REPOS + owner + "/" + repoName);
    }

    public Response updateRepo(String owner, String repoName, CreateRepoRequest changeRepo, String token) {

        String changeRepoJson = gson.toJson(changeRepo);

        return RestAssured.given()
                .baseUri(ApiData.BASE_URL)
                .header("Authorization", "Bearer " + token)
                .body(changeRepoJson)
                .when()
                .patch(ApiData.ENDPOINT_REPOS + owner + "/" + repoName);
    }

    public Response getRepo(String owner, String repoName, String token) {

        return RestAssured.given()
                .baseUri(ApiData.BASE_URL)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(ApiData.ENDPOINT_REPOS + owner + "/" + repoName);
    }
}
