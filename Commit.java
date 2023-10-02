import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
    }

    public Commit(String summary, String author) throws Exception {
        this("", summary, author);
    }

    public static String getDate() {
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate);
    }

    public String createTree() throws Exception {
        Tree tree = new Tree();
        BufferedReader br = new BufferedReader(new FileReader("index"));
        while(br.ready()) {
            String line = (String) br.readLine();
            tree.add(line);
        }
        if (!previousCommitHash.equals(""))
            tree.add("tree : " + getPrevTree());
        clearIndex();
        return tree.writeToObjects();
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
        BufferedReader br = new BufferedReader(new FileReader(previousCommitHash));
        return (String) br.readLine();
    }

    public void editPrevCommit() throws IOException, NoSuchAlgorithmException {
        if(!previousCommitHash.equals("")) {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(previousCommitHash));
            PrintWriter pw = new PrintWriter(previousCommitHash);
            int i = 0;
            while(br.ready()) {
                if(i == 2) {
                    StringBuilder builder = new StringBuilder(
                        treeHash + "\n" + previousCommitHash + "\n" + author + "\n" + date + "\n" + summary);
        
                    String commitHash = Util.hashString(builder.toString());
                    sb.append(commitHash);
                }
                else
                    sb.append((String) br.readLine());
                i++;
            }
            pw.print(sb.toString());
            pw.close();
            br.close();
        }
    }
}
