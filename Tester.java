import java.io.IOException;

public class Tester {
    public static void main(String[] args) throws Exception {
        Util.deleteFile("index");
        Util.deleteDirectory("objects");
        Git git = new Git();
        git.addToIndex("test1", "2f3c6b82e94acbefbdcc4ac1d00fcfb416892355");
        git.addToIndex("test2", "ccf587c77d3c946812e21674ed3b95cb47ab0d6d");

        Commit commit1 = new Commit("Commit", "Ari Meron");
        Commit commit2 = new Commit(commit1.writeToObjects(), "Commit2", "Ari Meron");

        Tree tree1 = new Tree();
        tree1.add("blob : 2f3c6b82e94acbefbdcc4ac1d00fcfb416892355 : test1");
        tree1.add("blob : ccf587c77d3c946812e21674ed3b95cb47ab0d6d : test2");
        String treeHash1 = tree1.writeToObjects();
    }
}
