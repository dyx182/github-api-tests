package github_api.api.clients;

import com.google.gson.Gson;
import github_api.api.config.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class RepoClient<T> {

    private static final Gson gson = new Gson();

    public Response createRepo(T requestJson, String token, String endpoint) {

        String repoJson = gson.toJson(requestJson);

        return RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(repoJson)
                .when()
                .post(endpoint);
    }

    public Response deleteRepo(String token, String endpoint) {

        return RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(endpoint);
    }

    public Response updateRepo(T requestJson, String token, String endpoint) {

        String changeRepoJson = gson.toJson(requestJson);

        return RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .header("Authorization", "Bearer " + token)
                .body(changeRepoJson)
                .when()
                .patch(endpoint);
    }

    public Response getRepo(String token, String endpoint) {

        return RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(endpoint);
    }
}
