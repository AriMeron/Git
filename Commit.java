import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Commit {
    protected String treeHash, previousCommitHash, nextCommitHash, summary, author, date;
    protected static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");

    public Commit(String previousCommitHash, String summary, String author) throws Exception {
        this.previousCommitHash = previousCommitHash;
        this.summary = summary;
        this.author = author;

        this.treeHash = createTree();

        this.nextCommitHash = "";

        this.date = getDate();

        editPrevCommit();
        writeToObjects();
    }

    public Commit(String summary, String author) throws Exception {
        this("", summary, author);
    }

    public static String getDate() {
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate);
    }

    public String createTree() throws Exception {
        String tree = "";
        BufferedReader br = new BufferedReader(new FileReader("index"));
        while(br.ready()) {
            String line = (String) br.readLine();
            tree += line + '\n';
        }
        if (!previousCommitHash.equals(""))
            tree += "tree : " + getPrevTree();
        clearIndex();
        tree = tree.substring(0, tree.length()-1);
        String hash = Util.hashString(tree);
        Util.writeFile("objects/" + hash, tree);
        return hash;
    }

    public String writeToObjects() throws Exception {
        StringBuilder builder = new StringBuilder(
                treeHash + "\n" + previousCommitHash + "\n" + author + "\n" + date + "\n" + summary);

        String commitHash = Util.hashString(builder.toString());

        // Inserting the nextCommitHash after the second newline
        builder.insert(builder.indexOf("\n", builder.indexOf("\n") + 1), nextCommitHash + "\n");

        Util.writeFile("objects/" + commitHash, builder.toString());
        return commitHash;
    }

    public String getHash() throws NoSuchAlgorithmException {
        StringBuilder builder = new StringBuilder(
        treeHash + "\n" + previousCommitHash + "\n" + author + "\n" + date + "\n" + summary);

        String commitHash = Util.hashString(builder.toString());

        // Inserting the nextCommitHash after the second newline
        builder.insert(builder.indexOf("\n", builder.indexOf("\n") + 1), nextCommitHash + "\n");

        return commitHash;
    }

    public static void clearIndex() throws IOException {
        Path p = Paths.get("index");
        Files.delete(p);
        PrintWriter pw = new PrintWriter("index");
        pw.print("");
        pw.close();
    }

    public String getPrevTree() throws IOException {
        if(previousCommitHash.equals(""))
            return "";
        BufferedReader br = new BufferedReader(new FileReader("objects/" + previousCommitHash));
        return (String) br.readLine();
    }

    public void editPrevCommit() throws Exception {
        if(!previousCommitHash.equals("")) {
            
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader("objects/" + previousCommitHash));
            int i = 0;
            Boolean ready = br.ready();
            while(br.ready()) {
                if(i == 2) {
                    br.readLine();
                    String w = getHash();
                    sb.append(w + '\n');
                }
                else
                    sb.append((String) br.readLine() + '\n');
                i++;
            }
            String builder = sb.toString();
            System.out.println(builder);
            Util.deleteFile("objects/" + previousCommitHash);
            PrintWriter pw = new PrintWriter("objects/" + previousCommitHash);
            pw.print(sb.toString());
            pw.close();
            br.close();
        }
    }

    public String delete(String fileName) throws NoSuchAlgorithmException, IOException {
        String newTree = "";
        String currTree = treeHash;
        File f = new File(fileName);
        if(!f.exists())
            return Util.hashString(newTree);
        while(currTree != null) {
            Boolean contains = false;
            BufferedReader br = new BufferedReader(new FileReader("objects/" + currTree));
            while(br.ready()) {
                String line = br.readLine();
                String[] splits = line.split(" : ");
                if(splits.length == 3) {
                    if(!splits[2].equals(fileName))
                        newTree += line + '\n';
                    else
                        contains = true;
                }
            }
            currTree = getPrevTree(currTree);
            if(contains)
                break;
        }
        newTree += "tree : " + currTree;

        String contents = Util.readFile(fileName);
        String hash = Util.hashString(contents);
        Util.deleteFile("objects/" + hash);

        FileWriter fw = new FileWriter("index", true);
        fw.write("*deleted*" + fileName);
        fw.close();
        return Util.hashString(newTree);
    }


    public String getPrevTree(String currTree) throws IOException {

        String contents = Util.readFile("objects/" + currTree);
        String[] splits = contents.split("\n");
        String line = splits[splits.length-1];
        String[] splitLine = line.split(" : ");
        if(!splitLine[0].equals("tree"))
            return null;
        else
            return splitLine[1];
    }    
}
