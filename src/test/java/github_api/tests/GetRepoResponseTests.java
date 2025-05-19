package github_api.tests;

import github_api.tests.repo.CreateRepoTest;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Test;

public class GetRepoResponseTests {

    @Story("Манипуляции с репозиторием")
    @Test
    public void repoTest() {
        CreateRepoTest create = new CreateRepoTest();

    }


}
