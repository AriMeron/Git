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

    private StringBuilder index;

    public Git() throws IOException {
        init();
    }


    public Git(boolean bypassInit) throws IOException {
        if(!bypassInit)
            init();
    }


    public void init() throws IOException {
        File objectsFolder = new File("objects");
        if (!objectsFolder.exists())
            objectsFolder.mkdirs();

        Path indexFile = Paths.get("index");
        if (!Files.exists(indexFile)) {
            Files.createFile(indexFile);
        }

        index = new StringBuilder("");
    }

    public void blob(String filename) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        StringBuilder fileContent = new StringBuilder();

        while (br.ready()) {
            fileContent.append((char) br.read());
        }
        String sha1 = generateSha1(fileContent.toString());

        PrintWriter pw = new PrintWriter("objects/" + sha1);
        pw.print(fileContent);

        index.append(filename + " : " + sha1 + "\n");
        addToIndex(filename, sha1);

        pw.close();
        br.close();
    }

    public String blob(String filename, boolean bypassAddToIndex) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        StringBuilder fileContent = new StringBuilder();

        while (br.ready()) {
            fileContent.append((char) br.read());
        }
        String sha1 = generateSha1(fileContent.toString());

        PrintWriter pw = new PrintWriter("objects/" + sha1);
        pw.print(fileContent);
        if(!bypassAddToIndex) {
            index.append(filename + " : " + sha1 + "\n");
            addToIndex(filename, sha1);
        }

        pw.close();
        br.close();

        return sha1;
    }

    public String generateSha1(String fileContent) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] ret = (md.digest(fileContent.getBytes()));
        return byteArrayToHexString(ret);
    }

    public void addToIndex(String filename, String sha1String) throws Exception {
        File f = new File(filename);
        String s = Util.readFile("index");
        FileWriter fw = new FileWriter("index", true);
        if(f.isDirectory()) {
            if(s.length() != 0)
                fw.write("\ntree : " + sha1String + " : " + filename );
            else
                fw.write("tree : " + sha1String + " : " + filename);
        }
        else {
            if(s.length() != 0)
                fw.write("\nblob : " + sha1String + " : " + filename);
            else
            fw.write("blob : " + sha1String + " : " + filename); 
        }
        fw.close();
    }

    public String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public void remove(String filename) throws IOException {
        BufferedReader brIndex = new BufferedReader(new FileReader("index"));
        StringBuilder index2 = new StringBuilder();
        boolean removedItem = false;
        String hash = "";

        // Loops through all the files, adds to index2 if not equal to the filename
        // which is being removed
        while (brIndex.ready()) {
            String str = brIndex.readLine();
            String[] splits = str.split(" : ");

            // This is terrible but should work in most cases. Needs to check for filename
            // followed by " : " in case another file contains the filename within it's
            // name.
            if (splits[2].equals(filename)) {
                removedItem = true;
                hash = splits[1];
            } else {
                index2.append(str + "\n");
            }
        }

        // Update the index if any files have been removed
        if (removedItem) {
            PrintWriter pw = new PrintWriter("index");
            pw.print(index2.toString().substring(0, Math.max(index2.length() - 1, 0)));
            pw.close();
            Path p = Paths.get("objects/" + hash);
            Files.delete(p);
        }

        brIndex.close();
    }

    public String addDirectory(String folderName) throws Exception {
        File directory = new File(folderName);
        File[] fileList = directory.listFiles();
        StringBuilder fileContents = new StringBuilder();

        for(File file : fileList) {
            if(!file.isDirectory()) {
                String name = file.getName();
                Git git = new Git(false);
                String path = folderName + "/" + name;
                String blobSha1 = git.blob(path, true);
                String lineToAdd = "blob : " + blobSha1 + " : " + name + '\n';
                fileContents.append(lineToAdd);
            }
            else {
                String name = file.getName();
                String subFolderHash = addDirectory(folderName + "/" + name);
                String lineToAdd = "tree : " + subFolderHash + " : " + name + '\n';
                fileContents.append(lineToAdd);
            }
        }
        String ret = fileContents.toString();
        ret = ret.substring(0, ret.length()-1);
        String hash = Util.hashString(ret);
        Util.writeFile("objects/" + hash, ret);
        addToIndex(folderName, hash);
        return hash;
    }
    

    public String generateHash(File f) throws IOException, NoSuchAlgorithmException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        StringBuilder sb = new StringBuilder();
        while(br.ready()) {
            sb.append((char) br.read());
        }
        String contents = sb.toString();
        return generateSha1(contents);
    }

    public void removeNewLine(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filename));

        while(br.ready()) {
            char c = (char) br.read();
            if(br.ready())
                sb.append(c);
            else
                break;
        }

        FileWriter fw = new FileWriter(filename);
        fw.write(sb.toString());
        fw.close();
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