import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
        Util.deleteFile("index");
        Git git = new Git();
        git.addToIndex("test1", "2f3c6b82e94acbefbdcc4ac1d00fcfb416892355");
        git.addToIndex("test2", "ccf587c77d3c946812e21674ed3b95cb47ab0d6d");

        Commit commit = new Commit("new commit", "Ari Meron");
        String treeHash = commit.createTree();

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

    @Test
    void testCommitAdvanced1() throws Exception {
        Git git = new Git();
        git.addToIndex("test1", "2f3c6b82e94acbefbdcc4ac1d00fcfb416892355");
        git.addToIndex("test2", "ccf587c77d3c946812e21674ed3b95cb47ab0d6d");

        Commit commit1 = new Commit("Commit", "Ari Meron");
        Commit commit2 = new Commit(commit1.getHash(), "Commit2", "Ari Meron");

        Tree tree1 = new Tree();
        tree1.add("blob : 2f3c6b82e94acbefbdcc4ac1d00fcfb416892355 : test1");
        tree1.add("blob : ccf587c77d3c946812e21674ed3b95cb47ab0d6d : test2");
        String treeHash1 = tree1.getHash();

        String contents1 = Util.readFile("objects/17134b84db732a9cfdebab9e3955ce453a8e4bab");
        String splits[] = contents1.split("\n");
        String commitTree = splits[0];
        String prevCommit = splits[1];
        String nextCommit = splits[2];
        String c2 = commit2.getHash();

        assertEquals(commitTree, treeHash1);
        assertEquals(prevCommit, "");
        assertEquals(nextCommit, c2);
    }

    @Test
    void testCommitAdvanced2() throws Exception {
        Util.deleteDirectory("objects");
        Util.deleteFile("index");
        
        //creating git and first commit
        Git git = new Git();
        git.addToIndex("test1", "2f3c6b82e94acbefbdcc4ac1d00fcfb416892355");
        git.addToIndex("test2", "ccf587c77d3c946812e21674ed3b95cb47ab0d6d");
        Commit commit = new Commit("Commit", "Ari Meron");
        String hash1 = commit.getHash();

        //getting sha1 of first commit's tree
        Tree tree1 = new Tree();
        tree1.add("blob : 2f3c6b82e94acbefbdcc4ac1d00fcfb416892355 : test1");
        tree1.add("blob : ccf587c77d3c946812e21674ed3b95cb47ab0d6d : test2");
        String treeHash1 = tree1.getHash();

        //creating second commit with a directory
        File folder = new File("testFolder");
        folder.mkdirs();
        File hello = new File("testFolder/hello");
        Util.writeFile("testFolder/hello", "hello");
        File hello2 = new File("testFolder/hello2");
        Util.writeFile("objects/hello2", "hello2");

        String treeHash2 = git.addDirectory("testFolder");
        Commit commit2 = new Commit(hash1, "commit2", "Ari Meron");
        String hash2 = commit2.getHash();

        //splits
        String c1 = Util.readFile("objects/" + hash1);
        String c2 = Util.readFile("objects/" + hash2);
        String[] split1 = c1.split("\n");
        String[] split2 = c2.split("\n");

        String tree = split1[0];
        String prevCommit = split1[1];
        String nextCommit = split1[2];

        assertEquals(tree, treeHash1);
        assertEquals(prevCommit, "");
        assertEquals(nextCommit, hash2);

        String tree2 = split2[0];
        String prevCommit2 = split2[1];
        String nextCommit2 = split2[2];

        assertEquals(tree2, treeHash2);
        assertEquals(prevCommit2, hash1);
        assertEquals(nextCommit2, "");
    }

    @Test
    void testDelete() throws Exception {
        Util.deleteDirectory("objects");
        Util.deleteDirectory("index");
        //creating 5 commits
        Git git = new Git();

        File test1 = new File("test1");
        Util.writeFile("test1", "test1");
        File test2 = new File("test2");
        Util.writeFile("test2", "test2");
        git.blob("test1");
        git.blob("test2");
        String currIndex = Util.readFile("index");
        System.out.println(currIndex);
        Commit commit1 = new Commit("first commit", "Ari Meron");

        File test3 = new File("test3");
        Util.writeFile("test3", "test3");
        File test4 = new File("test4");
        Util.writeFile("test4", "test4");
        git.blob("test3");
        git.blob("test4");
        Commit commit2 = new Commit(commit1.getHash(), "second commit", "Ari Meron");

        File test5 = new File("test5");
        Util.writeFile("test5", "test5");
        git.blob("test5");
        Commit commit3 = new Commit(commit2.getHash(), "third commit", "Ari Meron");

        File test6 = new File("test6");
        Util.writeFile("test6", "test6");
        git.blob("test6");
        Commit commit4 = new Commit(commit3.getHash(), "fourth commit", "Ari Meron");

        String test1Hash = Util.hashString("test1");
        String test2Hash = Util.hashString("test2");
        String test3Hash = Util.hashString("test3");
        String test4Hash = Util.hashString("test4");
        String test5Hash = Util.hashString("test5");
        String test6Hash = Util.hashString("test6");

        //deletes 3 files
        commit4.delete("test5");

        String indexContents = Util.readFile("index");
        assertTrue(indexContents.indexOf("*deleted*test5") != -1);
        assertFalse(Util.exists("objects/" + test5Hash));
        String expectedIndex = "*deleted*test5\nblob : " + test6Hash + " : test6\ntree : " + commit2.getTree();
        assertEquals(expectedIndex, indexContents);

        Commit commit5 = new Commit(commit4.getHash(), "first post-delete commit", "Ari Meron");

        commit5.delete("test1");

        indexContents = Util.readFile("index");
        assertTrue(indexContents.indexOf("*deleted*test1") != -1);
        assertFalse(Util.exists("objects/" + test1Hash));
        expectedIndex = "*deleted*test1\nblob : " + test6Hash + " : test6\nblob : " + test3Hash + " : test3\nblob : " + test4Hash + " : test4\nblob : " + test2Hash + " : test2";
        assertEquals(expectedIndex, indexContents);
    }

    @Test
    void testDeleteMultipleFiles() throws Exception {
        Util.deleteDirectory("objects");
        Util.deleteDirectory("index");
        //creating 5 commits
        Git git = new Git();

        File test1 = new File("test1");
        Util.writeFile("test1", "test1");
        File test2 = new File("test2");
        Util.writeFile("test2", "test2");
        git.blob("test1");
        git.blob("test2");
        Commit commit1 = new Commit("first commit", "Ari Meron");

        File test3 = new File("test3");
        Util.writeFile("test3", "test3");
        File test4 = new File("test4");
        Util.writeFile("test4", "test4");
        git.blob("test3");
        git.blob("test4");
        Commit commit2 = new Commit(commit1.getHash(), "second commit", "Ari Meron");

        File test5 = new File("test5");
        Util.writeFile("test5", "test5");
        git.blob("test5");
        Commit commit3 = new Commit(commit2.getHash(), "third commit", "Ari Meron");

        File test6 = new File("test6");
        Util.writeFile("test6", "test6");
        git.blob("test6");
        Commit commit4 = new Commit(commit3.getHash(), "fourth commit", "Ari Meron");

        String test1Hash = Util.hashString("test1");
        String test2Hash = Util.hashString("test2");
        String test3Hash = Util.hashString("test3");
        String test4Hash = Util.hashString("test4");
        String test5Hash = Util.hashString("test5");
        String test6Hash = Util.hashString("test6");

        ArrayList<String> filesToDelete = new ArrayList<String>();
        filesToDelete.add("test3");
        filesToDelete.add("test5");

        commit4.delete(filesToDelete);

        assertFalse(Util.exists("objects/" + test3Hash));
        assertFalse(Util.exists("objects/" + test5Hash));
        String index = Util.readFile("index");
        assertTrue(index.indexOf("*deleted*test3") != -1);
        assertTrue(index.indexOf("*deleted*test5") != -1);

        Commit commit5 = new Commit(commit4.getHash(), "final commit", "Ari Meron");
        String expectedTree = "blob : " + test6Hash + " : test6\nblob : " + test4Hash + " : test4\ntree : " + commit1.getTree();
        String realTree = Util.readFile("objects/" + commit5.getTree());

        assertEquals(realTree, expectedTree);

    }
}
