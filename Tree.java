import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Tree {
   
    private File file;
    private ArrayList trees;
    private ArrayList names;
    public Tree (String fileName)
    {
        file = new File (fileName);
        names = new ArrayList <String> ();
        trees = new ArrayList <String> ();
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

    public void remove (String string)
    {
        Boolean isFile = true;
        if (string.contains (".txt"))
        {
            isFile = false;
            
            
            StringBuilder sb = new StringBuilder ();

        }
        else
        {

        }
    }


     public  void stringToFile (String string, String fileName) throws IOException
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
}
