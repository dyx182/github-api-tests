package github_api.api.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetRepoResponse {
    private String name;
    private String language;
    private String description;
    private int stargazers_count;
}
