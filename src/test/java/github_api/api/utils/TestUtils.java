package github_api.api.utils;

public class TestUtils {

    public static void waiting(long mls) {
        try {
            Thread.sleep(mls);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted.");
        }
    }

    public static String urlFormatRepo(String a, String s, String d) {
        return String.format("/%s/%s/%s", a, s, d);
    }

    public static String urlFormatIssue(String a, String b, String c, String d) {
        return  String.format("/%s/%s/%s/%s", a, b, c, d);
    }

}
