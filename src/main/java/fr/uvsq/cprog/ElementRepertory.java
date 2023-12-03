package fr.uvsq.cprog;

/**
 * Abstract class representing elements in the directory structure.
 */
public abstract class ElementRepertory {
    /** The name of the element. */
    private String name;
    /** The path of the element. */
    private String path;
    /** The size of the element. */
    private long size;
    /** The number of the element. */
    private int ner;

    /**
     * Constructor for the ElementRepertory class.
     *
     * @param name The name of the element.
     * @param path The path of the element.
     * @param size The size of the element.
     * @param ner  The number of each directory (or file).
     */
    public ElementRepertory(final String name, final String path,
                                final long size, final int ner) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.ner = ner;
    }
    /**
     * Gets the name of the element.
     *
     * @return The name of the element.
     */
    public String getName() {
        return name;
    }
    /**
     * Gets the path of the element.
     *
     * @return The path of the element.
     */
    public String getPath() {
        return path;
    }
    /**
     * Gets the size of the element.
     *
     * @return The size of the element.
     */
    public long getSize() {
        return size;
    }
    /**
     * Gets the number of the element.
     *
     * @return The number of the element.
     */
    public long getNer() {
        return ner;
    }
    /**
     * Indicates if the element is a file.
     *
     * @return true if the element is a file, false otherwise.
     */
    public abstract boolean isFile();
    /**
     * Indicates if the element is a directory.
     *
     * @return true if the element is a directory, false otherwise.
     */
    public abstract boolean isDirectory();
}
