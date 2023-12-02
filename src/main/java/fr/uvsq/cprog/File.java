package fr.uvsq.cprog;


public class File extends FileSystem {

    public File(String name, String path, long size) {
        super(name, path, size);
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
