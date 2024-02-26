package dev.rlni.jlake;

public class Utils {
    private Utils() { }

    public static String readFile(final String path) {
        String content = null;
        try {
            content = new String(Utils.class.getResourceAsStream(path).readAllBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
}
