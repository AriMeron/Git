import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;

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
        BufferedWriter bw = new BufferedWriter (new FileWriter (file, true));
        if  (!names.contains (optionalFileName))
        {
            if (optionalFileName.length() > 0)
            {
                names.add (optionalFileName);
                bw.write ("" + typeOfFile + " : " + shaOfFile + " : " + optionalFileName + "\n");
            }
            else
            {
                if (!trees.contains (shaOfFile))
                {
                    trees.add (shaOfFile);
                }
                bw.write ("" + typeOfFile + " : " + shaOfFile + "\n");
            }
        }
        bw.close();
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


     public void stringToFile (String fileName, String string) throws IOException
        {
            // attach a file to FileWriter
            File file = new File ("objects/" + fileName);
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter (new FileWriter (file, true));

            bw.write (string);

     
            //close the file
            bw.close();
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


        public void writeToObjects () throws Throwable
        {
            //read file contents to string
            String beforeSha = fileToString (treeFileName);

            //take sha of string
            
            String afterSha = encryptPassword (beforeSha);
            
            //make fileName the sha string

            //File newFile = new File ("objects", afterSha);
            this.stringToFile (afterSha, beforeSha);
            //write contents of the file
            
        }


        public static String encryptPassword(String password)
    {
        String sha1 = "";
        try
        { 
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }


}
