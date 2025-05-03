package github_api.api.models.request;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CreateRepoRequest {
    private String name;
    private String description;
    private String homepage;
    private Boolean isPrivate;
    private Boolean hasIssues;
    private Boolean hasProjects;
    private Boolean hasWiki;
    private Boolean hasDiscussions;
    private Boolean isTemplate;
    private Boolean autoInit;
    private String gitignoreTemplate;
    private String licenseTemplate;
    private Boolean allowSquashMerge;
    private Boolean allowMergeCommit;
    private Boolean allowRebaseMerge;
    private Boolean allowAutoMerge;
    private Boolean deleteBranchOnMerge;
    private Boolean useSquashPrTitleAsDefault;
    private String squashMergeCommitTitle;
    private String squashMergeCommitMessage;
    private String mergeCommitTitle;
    private String mergeCommitMessage;
    private int _name;
}
