package fr.uvsq.cprog;
import java.util.List;

public class Directory extends FileSystem {
    private List<FileSystem> children; 

    public Directory(String name, String path, long size, List<FileSystem> children, int ner) {
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

}