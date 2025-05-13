package github_api.api.utils;

import github_api.api.models.request.CreateRepoRequest;

public class RepoTestData {
    public static CreateRepoRequest getRequestJsonFull() {
        return CreateRepoRequest.builder()
                .name("test-repo")
                .description("test repo from API")
                .homepage("https://github.com")
                .isPrivate(false)
                .hasIssues(true)
                .hasProjects(true)
                .hasWiki(true)
                .hasDiscussions(false)
                .isTemplate(false)
                .autoInit(false)
                .gitignoreTemplate("Java")
                .licenseTemplate("mit")
                .allowSquashMerge(true)
                .allowMergeCommit(true)
                .allowRebaseMerge(true)
                .allowAutoMerge(false)
                .deleteBranchOnMerge(false)
                .useSquashPrTitleAsDefault(false)
                .squashMergeCommitTitle("PR_TITLE")
                .squashMergeCommitMessage("COMMIT_MESSAGES")
                .mergeCommitTitle("MERGE_MESSAGE")
                .mergeCommitMessage("PR_TITLE")
                .build();
    }

    public static CreateRepoRequest getRequestJsonMinimal() {
        return CreateRepoRequest.builder()
                .name("test-repo")
                .build();
    }

    public static CreateRepoRequest getRequestJsonInvalid() {
        return CreateRepoRequest.builder()
                .name("test-repo")
                .description("test repo from API")
                .isPrivate(false)
                .build();
    }

    public static CreateRepoRequest changeRepoJson() {
        return CreateRepoRequest.builder()
                .name("test-repo-updated")
                .description("Updated description via API")
                .homepage("https://updated.example.com")
                .isPrivate(false)
                .hasIssues(true)
                .hasProjects(true)
                .hasWiki(true)
                .hasDiscussions(false)
                .isTemplate(false)
                .allowSquashMerge(true)
                .allowMergeCommit(true)
                .allowRebaseMerge(true)
                .allowAutoMerge(false)
                .deleteBranchOnMerge(false)
                .useSquashPrTitleAsDefault(false)
                .squashMergeCommitTitle("PR_TITLE")
                .squashMergeCommitMessage("COMMIT_MESSAGES")
                .mergeCommitTitle("MERGE_MESSAGE")
                .mergeCommitMessage("PR_TITLE")
                .build();
    }

    public static CreateRepoRequest changeInvalidRepoJson() {
        return CreateRepoRequest.builder()
                .name("")
                .homepage("123")
                .build();
    }
    public static CreateRepoRequest getUpdateRequest(CreateRepoRequest requestJson, String uniqueRepoName) {
        CreateRepoRequest request = requestJson.toBuilder()
                .name(uniqueRepoName)
                .build();
        return request;
    }
}
