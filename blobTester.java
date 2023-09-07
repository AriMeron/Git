import java.security.NoSuchAlgorithmException;

public class blobTester {
    public static void main(String[] args) {
        StringBuilder ex = new StringBuilder("tottenham");
        try {
            System.out.println(blobAndIndex.generateSha1(ex));
        } catch (NoSuchAlgorithmException e){
            System.out.println(e);
        }
    }
}
