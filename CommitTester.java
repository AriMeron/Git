import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommitTester {
    @Test
    @DisplayName("Verify getDate creates a valid date")
    void testGetDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");

        // Will throw an error if the date format is not valid
        dtf.parse(Commit.getDate());
    }

    @Test
    @DisplayName("Verify createTree creates a Tree in the correct location")
    void testCreateTree() throws Exception {
        String treeHash = Commit.createTree();

        // Confirm the empty tree has the correct hash
        assertEquals(treeHash, "da39a3ee5e6b4b0d3255bfef95601890afd80709");

        // Confirm the tree object file was created
        assertTrue(Util.exists("objects/da39a3ee5e6b4b0d3255bfef95601890afd80709"));
    }

    @Test
    @DisplayName("Verify writeToFile writes the commit contents and hashes correctly")
    void testWriteToFile() throws Exception {
        Commit commit = new Commit("2b98fbd4f414b26b612fa50b17879f62733254e6", "Did incredible things.",
                "Buddy the Wolverine");

        commit.writeToObjects();

        // Confirm sure the hash of the file created is correct
        assertTrue(Util.exists("objects/b0ced1739869a9ef72749ea17f0beaa5c75a128e"));

        // Confirm the object file contents match what is expected
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709\n" +
                "2b98fbd4f414b26b612fa50b17879f62733254e6\n" +
                "\n" +
                "Buddy the Wolverine\n" +
                "2023/09/21\n" +
                "Did incredible things.", Util.readFile("objects/b0ced1739869a9ef72749ea17f0beaa5c75a128e"));
    }

    @Test
    @DisplayName("Verify writeToFile works with the alternate constructor")
    void testWriteToFileAlt() throws Exception {
        Commit commit = new Commit("Did incredible things.",
                "Buddy the Wolverine");

        commit.writeToObjects();

        // Confirm sure the hash of the file created is correct
        assertTrue(Util.exists("objects/9d96fd1cc654fad2ee8951db7bb356091a63c4d1"));

        // Confirm the object file contents match what is expected
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709\n" +
                "\n" +
                "\n" +
                "Buddy the Wolverine\n" +
                "2023/09/21\n" +
                "Did incredible things.", Util.readFile("objects/9d96fd1cc654fad2ee8951db7bb356091a63c4d1"));
    }
}
