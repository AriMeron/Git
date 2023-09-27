import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GitTester {

    @Test
    @DisplayName("Verify sha1 hashing works")
    void testGenerateSha1() throws Exception {
        Git git = new Git();
        assertEquals(git.generateSha1("Hello world."), "e44f3364019d18a151cab7072b5a40bb5b3e274f");
    }

    @Test
    @DisplayName("Verify init creates index file and objects folder")
    void testInit() throws IOException {
        Git git = new Git();
        git.init();

        assertTrue(Util.exists("objects"));
        assertTrue(Util.exists("index"));

        Util.deleteDirectory("objects");
        Util.deleteFile("index");
    }

    @Test
    @DisplayName("Verify blob creates blobs and adds them to index")
    void testBlob() throws Exception {
        // Because blob is designed to create the blob and update the index, this test
        // tests both of those functionalities
        Git git = new Git();

        git.init();

        Util.writeFile("testFile.txt", "This is a test file.");
        git.blob("testFile.txt");

        // Confirm blob file has been created in objects with the correct hash
        assertTrue(Util.exists("objects/26d82f1931cbdbd83c2a6871b2cecd5cbcc8c26b"));

        // Confirm blob file contents match original file contents
        assertEquals(Util.readFile("objects/26d82f1931cbdbd83c2a6871b2cecd5cbcc8c26b"), "This is a test file.");

        // Confirm index has been updated
        assertEquals(Util.readFile("index"), "testFile.txt : 26d82f1931cbdbd83c2a6871b2cecd5cbcc8c26b");

        Util.deleteDirectory("objects");
        Util.deleteFile("index");
    }

    @Test
    @DisplayName("Verify remove correctly updates index")
    void testRemove() throws Exception {
        Git git = new Git();

        git.init();

        Util.writeFile("testFile.txt", "This is a test file.");
        Util.writeFile("testFile2.txt", "This is another test file.");

        git.blob("testFile.txt");
        git.blob("testFile2.txt");

        // Confirm index has both files
        assertEquals(Util.readFile("index"),
                "testFile.txt : 26d82f1931cbdbd83c2a6871b2cecd5cbcc8c26b\ntestFile2.txt : dd6fdaba4cff3db9692d2a86b39a331ad92c0667");

        git.remove("testFile.txt");

        // Confirm testFile.txt has been removed from index
        assertEquals(Util.readFile("index"), "testFile2.txt : dd6fdaba4cff3db9692d2a86b39a331ad92c0667");

        // Confirm the blob file still exists
        assertTrue(Util.exists("objects/26d82f1931cbdbd83c2a6871b2cecd5cbcc8c26b"));

        git.remove("testFile2.txt");

        // Confirm testFile2.txt has been removed from index
        assertEquals(Util.readFile("index"), "");

        // Confirm the blob file still exists
        assertTrue(Util.exists("objects/dd6fdaba4cff3db9692d2a86b39a331ad92c0667"));

        Util.deleteDirectory("objects");
        Util.deleteFile("index");
    }
}
