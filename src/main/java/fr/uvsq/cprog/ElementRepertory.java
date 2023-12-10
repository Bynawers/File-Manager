package fr.uvsq.cprog;

/**
 * Abstract class representing elements in the directory structure.
 */
public abstract class ElementRepertory {
    /** The name of the element. */
    private String name;
    /** The number of the element. */
    private int ner;

    private String path;

    /**
     * Constructor for the ElementRepertory class.
     * @param nameTmp The name of the element.
     * @param nerTmp  The number of each directory (or file).
     */
    public ElementRepertory(final String nameTmp, final int nerTmp, final String pathTmp) {
        this.name = nameTmp;
        this.ner = nerTmp;
        this.path = pathTmp;
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

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gets the number of the element.
     *
     * @return The number of the element.
     */
    public int getNer() {
        return ner;
    }

    /**
     * Gets the number of the element.
     *
     * @return The number of the element.
     */
    public int setNer() {
        return ner;
    }

    abstract public boolean isDirectory();
    abstract public boolean isFile();
}
