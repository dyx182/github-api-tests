package github_api.api.testdata;

import github_api.api.models.request.CreateIssueRequest;
import github_api.api.models.request.UpdateIssueRequest;

import java.util.List;

import static github_api.api.config.EnvConfig.LOGIN;

public class IssueTestData {
    public static CreateIssueRequest getFullCreateIssue() {
        return CreateIssueRequest.builder()
                .title("New Issue")
                .body("Dyscription")
                .assignees(List.of(LOGIN))
                .labels(List.of("bug", "enhancement"))
                .build();
    }

    public static CreateIssueRequest getInvalidFields() {
        return CreateIssueRequest.builder()
                .title("Next Issue")
                .assignees(List.of("First User"))
                .build();
    }

    public static UpdateIssueRequest getUpdateFields() {
        return UpdateIssueRequest.builder()
                .title("Update Issue")
                .body("New information")
                .state("open")
                .assignees(List.of(LOGIN))
                .build();
    }

    public static UpdateIssueRequest getInvalidUpdateFields() {
        return UpdateIssueRequest.builder()
                .title("")
                .body("New information")
                .state("open")
                .state_reason("completed")
                .assignees(List.of(LOGIN))
                .build();
    }
}
