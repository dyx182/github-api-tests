package github_api.api.config.repo;

import github_api.api.models.request.CreateRepoRequest;

public class RepoTestData {

    public static CreateRepoRequest getRequestJsonFull() {
        return CreateRepoRequest.builder()
                .name("test-repo-api")
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
                .name("test-repo-api-2")
                .build();
    }

    public static CreateRepoRequest getRequestJsonInvalid() {
        return CreateRepoRequest.builder()
                .description("test repo from API")
                .isPrivate(false)
                .build();
    }

    public static CreateRepoRequest changeRepoJson() {
        return CreateRepoRequest.builder()
                .name("test-repo-updated-")
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

    public static CreateRepoRequest changeNonExistRepoJson() {
        return CreateRepoRequest.builder()
                .name("non-exist-repo-")
                .build();
    }
}

