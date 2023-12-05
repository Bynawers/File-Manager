package fr.uvsq.cprog;

/**
 * Abstract class representing elements in the directory structure.
 */
public abstract class ElementRepertory {
    /** The name of the element. */
    private String name;
    /** The path of the element. */
    private String path;
    /** The number of the element. */
    private int ner;
    /** The annotation of the element. */
    private String annotation;
    /** The parent instance of the element. */
    private Directory parent;

    /**
     * Constructor for the ElementRepertory class.
     * @param nameTmp The name of the element.
     * @param nerTmp  The number of each directory (or file).
     * @param annotationTmp The annotation of the element.
     * @param parentTmp The parent of the element.
     */
    public ElementRepertory(
        final String nameTmp,
        final int nerTmp,
        final String annotationTmp,
        final Directory parentTmp
    ) {
        this.name = nameTmp;
        this.ner = nerTmp;
        this.annotation = annotationTmp;
        this.parent = parentTmp;
        this.path = parentTmp == null 
            ? "./" + nameTmp
            : parentTmp.getPath() + "/" + nameTmp;
    }
    /**
     * Minimal constructor for the ElementRepertory class.
     * @param nameTmp The name of the element.
     * @param nerTmp  The number of each directory (or file).
     * @param parentTmp The parent of the element.
     */
    public ElementRepertory(
        final String nameTmp,
        final int nerTmp,
        final Directory parentTmp
    ) {
        this.name = nameTmp;
        this.ner = nerTmp;
        this.parent = parentTmp;
        this.path = parentTmp == null 
            ? "./" + nameTmp
            : parentTmp.getPath() + "/" + nameTmp;
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
     * set the name of the element.
     * @param nameTmp The name of the element.
     */
    public void setName(final String nameTmp) {
        this.name = nameTmp;
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
     * set the name of the path.
     * * @param pathTmp The path of the element.
     */
    public void setPath(final String pathTmp) {
        this.path = pathTmp;
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
     * Gets the number of the element.
     *
     * @return The number of the element.
     */
    public Directory getParent() {
        return parent;
    }
     /**
     * set the name of the parent.
     * @param parentTmp The parent of the element.
     */
    public void setParent(final Directory parentTmp) {
        this.parent = parentTmp;
    }

    /**
     * Gets the annotation of the element.
     *
     * @return The annotation of the element.
     */
    public String getAnnotation() {
        return annotation;
    }
     /**
     * set the name of the annotation.
     * * @param annotationTmp The annotation of the element.
     */
    public void setAnnotation(final String annotationTmp) {
        this.annotation = annotationTmp;
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
    /**
     * Gets the size of the element.
     *
     * @return The size of the element.
     */
    public abstract long getSize();

    public abstract void listContent();
}
