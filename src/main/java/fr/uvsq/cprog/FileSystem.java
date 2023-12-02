package fr.uvsq.cprog;

public abstract class FileSystem {
    private String name;
    private String path;
    private long size;
    private int ner;

    public FileSystem(String name, String path, long size, int ner) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.ner = ner;
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

    public long getNer() {
        return ner;
    }

    public abstract boolean isFile();

    public abstract boolean isDirectory();
}
