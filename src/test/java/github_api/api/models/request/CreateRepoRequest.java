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
    private Boolean has_issues;
    private Boolean has_projects;
    private Boolean has_wiki;
    private Integer team_id;
    private Boolean auto_init;
    private String gitignore_template;
    private String license_template;
    private Boolean allow_squash_merge;
    private Boolean allow_merge_commit;
    private Boolean allow_rebase_merge;
    private Boolean allow_auto_merge;
    private Boolean delete_branch_on_merge;
    private Boolean has_downloads;
    private Boolean is_template;
}
