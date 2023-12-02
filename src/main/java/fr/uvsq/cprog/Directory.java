package fr.uvsq.cprog;
import java.util.List;

public class Directory extends FileSystem {
    private List<FileSystem> children; // Liste des fichiers et sous-répertoires

    public Directory(String name, String path, long size, List<FileSystem> children) {
        super(name, path, size);
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