package github_api.api.models.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class CreateIssueRequest {
    private String title;
    private String body;
    private List<String> assignees;
    private List<String> labels;
    private int milestone;
}
