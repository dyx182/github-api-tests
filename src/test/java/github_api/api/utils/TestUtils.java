package github_api.api.utils;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;

public class TestUtils {

    public static void sleep(long mls) {
        try {
            Thread.sleep(mls);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted.");
        }
    }

}
