package structural.composite;

public class File implements FileSystemItem {

    private final String name;
    private final int sizeKb;

    public File(String name, int sizeKb) {
        this.name = name;
        this.sizeKb = sizeKb;
    }

    @Override
    public String size() {
        return sizeKb + "KB";
    }

    @Override
    public void OpenAll() {
        System.out.println("file name: " + name);
    }

    @Override
    public boolean isFolder() {
        return false;
    }
}