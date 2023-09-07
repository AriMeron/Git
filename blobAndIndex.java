import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class blobAndIndex {

    private StringBuilder index;
    
    public void init() {
        File f = new File("/Honors Topics/Git/objects");
        if(!f.exists())
            f.mkdirs();
        index = new StringBuilder("");
    }
    
    public void blob (String filename) throws IOException, NoSuchAlgorithmException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        StringBuilder fileContent = new StringBuilder();

        while(br.ready()) {
            fileContent.append((char) br.read());
        }
        String sha1 = generateSha1(fileContent);

        PrintWriter pw = new PrintWriter("/Honors Topics/Git/objects/" + sha1);
        pw.print(fileContent);

        addToIndex(filename, sha1);

        pw.close();
        br.close();
    }

    public static String generateSha1(StringBuilder fileContent) throws NoSuchAlgorithmException {
        String str = fileContent.toString();
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] ret = (md.digest(str.getBytes()));
        return byteArrayToHexString(ret);
    }

    public void addToIndex(String filename, String sha1) throws IOException {
        PrintWriter pw = new PrintWriter("index.txt");
        pw.print(index.toString());
        pw.print(filename + ", " + sha1);
        pw.close();
    }

    public static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i=0; i < b.length; i++) {
          result +=
                Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
      }
}