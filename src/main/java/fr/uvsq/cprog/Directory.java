package fr.uvsq.cprog;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class Directory extends ElementDirectory {
    private List<ElementDirectory> children; 

    public Directory(String name, String path, long size, List<ElementDirectory> children, int ner) {
        super(name, path, size, ner);
        this.children = children;
    }

    @Override
    public boolean isFile() {
        return false;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }
    public List<ElementDirectory> getChildren() {
        return children;
    }

    public void ReturnAllFiles(String path) {
        ReturnFiles(new File(path));
    }

    private void ReturnFiles(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                ElementDirectory element;

                if (file.isDirectory()) {
                    element = new Directory(file.getName(), file.getPath(), file.length(), new ArrayList<>(), 0 );
                    ReturnFiles(file);
                } else {
                    element = new file(file.getName(), file.getPath(), file.length(), 0);
                }
                children.add(element);
            }
        }
    }
}