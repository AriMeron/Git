import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Tree {

    private HashSet<String> trees;
    private HashMap<String, String> blobs;

    public Tree() {
        blobs = new HashMap<String, String>();
        trees = new HashSet<String>();
    }

    public void add(String input) throws Exception {
        String[] splits = input.split(" : ");

        if (splits.length == 2) {
            // Adding a tree
            if (splits[0].equals("tree")) {
                if (trees.contains(splits[1])) {
                    throw new Exception("Cannot add a duplicate tree");
                }
                trees.add(splits[1]);
                return;
            }
        } else if (splits.length == 3) {
            // Adding a blob
            if (splits[0].equals("blob")) {
                if (blobs.containsKey(splits[2])) {
                    throw new Exception("Cannot add a blob with a duplicate filename");
                }
                blobs.put(splits[2], splits[1]);
                return;
            }
        }

        throw new Exception("Invalid add format");
    }

    public boolean remove(String key) {
        if (blobs.containsKey(key)) {
            blobs.remove(key);
            return true;
        } else if (trees.contains(key)) {
            trees.remove(key);
            return true;
        }
        return false;
    }

    public void writeToObjects() throws Exception {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, String> entry : blobs.entrySet()) {
            builder.append("blob : " + entry.getValue() + " : " + entry.getKey() + "\n");
        }

        for (String hash : trees.toArray(new String[trees.size()])) {
            builder.append("tree : " + hash + "\n");
        }

        builder.deleteCharAt(builder.length() - 1);

        String result = builder.toString();
        System.out.println(result);
        Util.writeFile("objects/" + Util.hashString(result), result);
    }

}
