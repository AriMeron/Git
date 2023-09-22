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
    }

    public Commit(String summary, String author) throws Exception {
        this("", summary, author);
    }

    public static String getDate() {
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate);
    }

    public static String createTree() throws Exception {
        Tree tree = new Tree();
        return tree.writeToObjects();
    }

    public void writeToObjects() throws Exception {
        StringBuilder builder = new StringBuilder(
                treeHash + "\n" + previousCommitHash + "\n" + author + "\n" + date + "\n" + summary);

        String commitHash = Util.hashString(builder.toString());

        // Inserting the nextCommitHash after the second newline
        builder.insert(builder.indexOf("\n", builder.indexOf("\n") + 1), nextCommitHash + "\n");

        Util.writeFile("objects/" + commitHash, builder.toString());
    }
}
