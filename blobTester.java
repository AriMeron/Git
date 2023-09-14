import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class blobTester {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        Git.init();
        File file2 = new File ("hi.txt");
        Git.blob("hi.txt");
        File file3 = new File ("h.txt");
        Git.blob("h.txt");

        Git.remove("hello.txt");
    }
}
