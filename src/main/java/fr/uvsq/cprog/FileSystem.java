package fr.uvsq.cprog;

public abstract class FileSystem {
    private String name;
    private String path;
    private long size;

    public FileSystem(String name, String path, long size) {
        this.name = name;
        this.path = path;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public abstract boolean isFile();

    public abstract boolean isDirectory();
}
