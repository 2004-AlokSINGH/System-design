package structural.composite;

import java.util.ArrayList;
import java.util.List;

public class Folder implements FileSystemItem {

    private final String name;
    private final List<FileSystemItem> childrenItem;

    public Folder() {
        this("Unnamed");
    }

    public Folder(String name) {
        this.name = name;
        this.childrenItem = new ArrayList<>();
    }

    @Override
    public String size() {
        int totalKb = 0;
        for (FileSystemItem fs : childrenItem) {
            String s = fs.size(); // expect formats like "4KB", "44Kb"
            try {
                String digits = s.replaceAll("[^0-9]", "");
                if (!digits.isEmpty()) {
                    totalKb += Integer.parseInt(digits);
                }
            } catch (NumberFormatException e) {
                // ignore malformed sizes
            }
        }
        return totalKb + "KB";
    }

    public void addFile(FileSystemItem file) {
        this.childrenItem.add(file);
    }

    public void removeFile(FileSystemItem file) {
        this.childrenItem.remove(file);
    }

    @Override
    public void OpenAll() {
        System.out.println("Folder: " + name);
        for (FileSystemItem fs : childrenItem) {
            fs.OpenAll(); 
        }
    }

    @Override
    public boolean isFolder() {
        return true;
    }
}