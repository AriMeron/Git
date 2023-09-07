import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class blobTester {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        Git.init();
        Git.blob("hello.txt");
        Git.blob("hi.txt");
        Git.blob("h.txt");
    }
}
