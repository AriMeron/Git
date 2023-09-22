import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class GitTester {

    @Test
    void testGenerateSha1() throws Exception {
        assertEquals(Git.generateSha1("Hello world."), "e44f3364019d18a151cab7072b5a40bb5b3e274f");
    }

    @Test
    void testInit() throws IOException {
        Git.init();

        assertTrue(Util.exists("objects"));
        assertTrue(Util.exists("index"));

        Util.deleteDirectory("objects");
        Util.deleteFile("index");
    }

    @Test
    void testBlob() throws Exception {
        // Because blob is designed to create the blob and update the index, this test
        // tests both of those functionalities

        Git.init();

        Util.writeFile("testFile.txt", "This is a test file.");
        Git.blob("testFile.txt");

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
    void testRemove() throws Exception {
        Git.init();

        Util.writeFile("testFile.txt", "This is a test file.");
        Util.writeFile("testFile2.txt", "This is another test file.");

        Git.blob("testFile.txt");
        Git.blob("testFile2.txt");

        // Confirm index has both files
        assertEquals(Util.readFile("index"),
                "testFile.txt : 26d82f1931cbdbd83c2a6871b2cecd5cbcc8c26b\ntestFile2.txt : dd6fdaba4cff3db9692d2a86b39a331ad92c0667");

        Git.remove("testFile.txt");

        // Confirm testFile.txt has been removed from index
        assertEquals(Util.readFile("index"), "testFile2.txt : dd6fdaba4cff3db9692d2a86b39a331ad92c0667");

        Git.remove("testFile2.txt");

        // Confirm testFile2.txt has been removed from index
        assertEquals(Util.readFile("index"), "");

        Util.deleteDirectory("objects");
        Util.deleteFile("index");
    }
}
