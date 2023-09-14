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

        Tree tree = new Tree ("tree.txt");
        tree.add ("blob", "adccece39a0795801972604c8cf21a22bf45b262", "hi.txt");
        tree.add ("blob", "42eaa19898ffb1fffc600c0012a6d80ca540c659", "h.txt");
        tree.add ("tree", "aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d", "");

        
    }
}
