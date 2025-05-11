package github_api.api.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUserResponse {
    private String login;
    private int id;
    private String name;
    private String location;
    private String bio;
    private int public_repos;

    @Override
    public String toString() {
        return "User {\n" +
                "  login='" + login + "',\n" +
                "  id=" + id + ",\n" +
                "  name='" + name + "',\n" +
                "  location='" + location + "',\n" +
                "  bio='" + bio + "',\n" +
                "  public_repos=" + public_repos + "\n" +
                "}";
    }
}
