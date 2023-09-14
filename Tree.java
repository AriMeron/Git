import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Tree {
   
    private File file;
    private ArrayList trees;
    private ArrayList names;
    private String treeFileName;

    public Tree (String fileName)
    {
        file = new File (fileName);
        names = new ArrayList <String> ();
        trees = new ArrayList <String> ();
        treeFileName = fileName;
    }

    public void add (String typeOfFile, String shaOfFile, String optionalFileName) throws IOException
    {
        FileWriter fw = new FileWriter (file);
        if  (!names.contains (optionalFileName))
        {
            if (optionalFileName.length() > 0)
            {
                names.add (optionalFileName);
                fw.write ("" + typeOfFile + " : " + shaOfFile + " : " + optionalFileName);
            }
            else
            {
                if (!trees.contains (shaOfFile))
                {
                    trees.add (shaOfFile);
                }
                fw.write ("" + typeOfFile + " : " + shaOfFile);
            }
        }
        fw.close();
    }

    public void remove (String string) throws Throwable
    {
        int index;
        String theFile = fileToString (treeFileName);
        index = theFile.indexOf (string);
        int length;
        String endString;
        
        if (string.contains (".txt"))
        {
            length = 50;
            endString = string.substring (0, index - length) + string.substring (index + string.length() + 1);
            stringToFile (endString, treeFileName);
        }
        else
        {
            length = 7;
            endString = string.substring (0, index - length) + string.substring (index + string.length() + 1);
            stringToFile (endString, treeFileName);

        }
    }


     public void stringToFile (String string, String fileName) throws IOException
        {
            // attach a file to FileWriter
            File file = new File ("objects/" + string);
            file.createNewFile();
            FileWriter fw= new FileWriter(file);
            FileReader fr = new FileReader (fileName);

            char ch;
            String endResult = "";
            while(fr.ready())
                {
                    ch = (char) fr.read();
                    endResult += ch;
                }


                fw.write(endResult);
     
            //close the file
            fw.close();
            fr.close();
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





}
