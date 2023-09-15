import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExampleTester {

   private static File newFile;
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        File file = new File ("hello.txt");
        Git.stringToFile ("hello.txt", "test file contents");
        Git.deleteFile ("index");
        newFile = new File ("objects");
        Git.deleteDir (newFile);
        
        
        /*
         * Utils.writeStringToFile("junit_example_file_data.txt", "test file contents");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        Git.deleteFile ("hello.txt");
        Git.deleteFile ("index");
        Git.deleteDir (newFile);
        /*
         * Utils.deleteFile("junit_example_file_data.txt");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */
    }

    @Test
    @DisplayName("[8] Test if initialize and objects are created correctly")
    void testInitialize() throws Exception {

        Git.init();
        // Run the person's code
        // TestHelper.runTestSuiteMethods("testInitialize");

        // check if the file exists
        File file = new File("index");
        Path path = Paths.get("objects");

        assertTrue(file.exists());
        assertTrue(Files.exists(path));
    }

    @Test
    @DisplayName ("Test Tree Object")
    void testTree () throws Throwable
    {
        File reference = new File ("reference.txt");
        FileWriter fw = new FileWriter (reference);
        fw.write ("tree: bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        String sha = fileToString ("reference.txt");
        File file = new File ("treeTester.txt");
        Tree tree = new Tree ("treeTester.txt");
        tree.add ("tree", "bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b", "");
        tree.remove("bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        tree.writeToObjects();
        assertTrue (tree.getName().equals (sha));


    }

     public String fileToString (String fileName) throws Throwable
        {
            String endResult = "";
            //File file = new File ("output.txt");
            char ch;
 
             // check if File exists or not
            FileReader fr;
            try
            {
                fr = new FileReader(fileName);
                while(fr.ready())
                {
                    ch = (char) fr.read();
                    endResult += ch;
                }
 
                fr.close();
            } catch (Error | IOException e)
            {
                
                throw e;
            }
            return endResult;
        }



    @Test
    @DisplayName("[15] Test if adding a blob works.  5 for sha, 5 for file contents, 5 for correct location")
    void testCreateBlob() throws Exception {
        File file;
        try {

            // Manually create the files and folders before the 'testAddFile'
            file = new File ("tester.txt");
            // MyGitProject myGitClassInstance = new MyGitProject();
            // myGitClassInstance.init();
            Git.init();
            
            //how do i test that the blob is created????

            // TestHelper.runTestSuiteMethods("testCreateBlob", file1.getName());
            

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        // Check blob exists in the objects folder
        // String fileName = Git.
        // File testFile = new File("objects/" + Git.methodToGetSha1());
        // assertTrue("Blob file to add not found", file_junit1.exists());

        // // Read file contents
        // String indexFileContents = Git.fileToString("objects/" + file.methodToGetSha1());
        // assertEquals("File contents of Blob don't match file contents pre-blob creation", indexFileContents,
        //         file.getContents());
    }
}
