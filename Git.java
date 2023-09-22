import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Git {

    private static StringBuilder index;

    public static void init() throws IOException {
        File objectsFolder = new File("objects");
        if (!objectsFolder.exists())
            objectsFolder.mkdirs();

        Path indexFile = Paths.get("index");
        if (!Files.exists(indexFile)) {
            Files.createFile(indexFile);
        }

        index = new StringBuilder("");
    }

    public static void blob(String filename) throws IOException, NoSuchAlgorithmException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        StringBuilder fileContent = new StringBuilder();

        while (br.ready()) {
            fileContent.append((char) br.read());
        }
        String sha1 = generateSha1(fileContent.toString());

        PrintWriter pw = new PrintWriter("objects/" + sha1);
        pw.print(fileContent);

        index.append(filename + " : " + sha1 + "\n");
        addToIndex();

        pw.close();
        br.close();
    }

    public static String generateSha1(String fileContent) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] ret = (md.digest(fileContent.getBytes()));
        return byteArrayToHexString(ret);
    }

    public static void addToIndex() throws IOException {
        PrintWriter pw = new PrintWriter("index");
        pw.print(index.toString().substring(0, index.length() - 1));
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
        boolean removedItem = false;

        // Loops through all the files, adds to index2 if not equal to the filename
        // which is being removed
        while (brIndex.ready()) {
            String str = brIndex.readLine();

            // This is terrible but should work in most cases. Needs to check for filename
            // followed by " : " in case another file contains the filename within it's
            // name.
            if (str.contains(filename + " : ")) {
                removedItem = true;
            } else {
                index2.append(str + "\n");
            }
        }

        // Update the index if any files have been removed
        if (removedItem) {
            PrintWriter pw = new PrintWriter("index");
            pw.print(index2.toString().substring(0, Math.max(index2.length() - 1, 0)));
            pw.close();
        }

        brIndex.close();
    }

    // Commented out the following methods because they aren't necessary for Index

    // public static void stringToFile(String string, String fileName) throws
    // IOException {
    // File file = new File("objects/" + string);
    // file.createNewFile();
    // FileWriter fw = new FileWriter(file);
    // FileReader fr = new FileReader(fileName);

    // char ch;
    // String endResult = "";
    // while (fr.ready()) {
    // ch = (char) fr.read();
    // endResult += ch;
    // }

    // fw.write(endResult);

    // // close the file
    // fw.close();
    // fr.close();
    // }

    // public static void deleteFile(String filePath) {
    // File file = new File(filePath);
    // file.delete();
    // }

    // public static void deleteDir(File file) {
    // File[] contents = file.listFiles();
    // if (contents != null) {
    // for (File f : contents) {
    // deleteDir(f);
    // }
    // }
    // file.delete();
    // }
}