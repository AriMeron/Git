import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Git {

    private static StringBuilder index;

    public static void init() {
        File f = new File("objects");
        if (!f.exists())
            f.mkdirs();
        index = new StringBuilder("");
    }

    public static void blob(String filename) throws IOException, NoSuchAlgorithmException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        StringBuilder fileContent = new StringBuilder();

        while (br.ready()) {
            fileContent.append((char) br.read());
        }
        String sha1 = generateSha1(fileContent);

        PrintWriter pw = new PrintWriter("objects/" + sha1);
        pw.print(fileContent);

        index.append(filename + ":" + sha1 + "\n");
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

    public static void addToIndex(String filename, String sha1) throws IOException {
        PrintWriter pw = new PrintWriter("index");
        pw.print(index.toString());
        pw.close();
    }

    public static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static void remove(String filename) throws IOException {
        BufferedReader brIndex = new BufferedReader(new FileReader("index"));
        StringBuilder index2 = new StringBuilder();
        int occurences = 0;
        String shaName = "";

        // records all of the files
        while (brIndex.ready()) {
            String str = brIndex.readLine();
            if (str.substring(0, filename.length()).equals(filename)) {
                occurences++;
                shaName = str.substring(str.indexOf(':') + 1);
            } else {
                index2.append(str + "\n");
            }
        }

        if (occurences == 0) {
        }
        if (occurences == 1) {
            Path path = Paths.get("objects/" + shaName);
            Files.delete(path);
            PrintWriter pw = new PrintWriter("index");
            pw.print(index2.toString());
            pw.close();
        }
        brIndex.close();
    }

    public static void stringToFile(String string, String fileName) throws IOException {
        // attach a file to FileWriter
        File file = new File("objects/" + string);
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        FileReader fr = new FileReader(fileName);

        char ch;
        String endResult = "";
        while (fr.ready()) {
            ch = (char) fr.read();
            endResult += ch;
        }

        fw.write(endResult);

        // close the file
        fw.close();
        fr.close();
    }

    public static void deleteFile(String filePath) {

        File file = new File(filePath);
        file.delete();

    }

    public static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

}