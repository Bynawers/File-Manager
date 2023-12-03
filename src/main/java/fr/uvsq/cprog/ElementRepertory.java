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
    private int NER;
    /** The annotation of the element*/
    private String annotation;
    /** The parent instance of the element */
    private Directory parent;
    /**
     * Constructor for the ElementRepertory class.
     *
     * @param name The name of the element.
     * @param path The path of the element.
     * @param size The size of the element.
     * @param NER  The number of each directory (or file).
     * @param annotation .
     */

    public ElementRepertory(
        final String name,
        final String path,
        final long size,
        final int NER,
        final String annotation,
        final Directory parent
    ) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.NER = NER;
        this.annotation = annotation;
        this.parent = parent;
    }
    public ElementRepertory(
        final String name,
        final int NER,
        final Directory parent
    ) {
        this.name = name;
        this.NER = NER;
        this.parent = parent;
    }
    
    /**
     * Gets the name of the element.
     *
     * @return The name of the element.
     */
    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the path of the element.
     *
     * @return The path of the element.
     */
    public String getPath() {
        return path;
    }
    public void setPath(final String path) {
        this.path = path;
    }

    /**
     * Gets the size of the element.
     *
     * @return The size of the element.
     */
    public long getSize() {
        return size;
    }
    public void setSize(final long size) {
        this.size = size;
    }

    /**
     * Gets the number of the element.
     *
     * @return The number of the element.
     */
    public long getNER() {
        return NER;
    }

    /**
     * Gets the number of the element.
     *
     * @return The number of the element.
     */
    public Directory getParent() {
        return parent;
    }
    public void setParent(final Directory parent) {
        this.parent = parent;
    }

    /**
     * Gets the annotation of the element.
     *
     * @return The annotation of the element.
     */
    public String getAnnotation() {
        return annotation;
    }
    public void setAnnotation(final String annotation) {
        this.annotation = annotation;
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

    public abstract void listContent();
}