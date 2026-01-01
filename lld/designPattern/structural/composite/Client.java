package structural.composite;

public class Client {

    public static void main(String[] args) {       

        Folder root = new Folder("root");
        root.addFile(new File("README.md", 4));
        Folder sub = new Folder("src");
        sub.addFile(new File("Main.java", 6));
        root.addFile(sub);

        root.OpenAll();
        System.out.println("total size: " +root.size());
    }
}