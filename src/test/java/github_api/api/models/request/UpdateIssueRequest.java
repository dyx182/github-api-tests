package github_api.api.models.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class UpdateIssueRequest {
    private String title;
    private String body;
    private String state;  //"open" или "closed"
    private String state_reason;  //("completed", "not_planned", "reopened"). Только для state: "closed".
    private List<String> assignees;
    private Integer milestone;
    private List<String> labels;
    private String due_on; //formatted YYYY-MM-DD
}
