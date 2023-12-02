package fr.uvsq.cprog;


public class File extends FileSystem {

    public File(String name, String path, long size, int ner) {
        super(name, path, size, ner);
    }

    @Override
    public boolean isFile() {
        return true;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }
}
