package fr.uvsq.cprog;


public class File extends ElementRepertory {

    /**
     * Types of a file.
     */
    public enum FileType {
        /** Type text in format txt. */
        TEXT,
        /** Type image in binary. */
        IMAGE
    }
    /** The content of the element. */
    private String content;
    /** The type of the element. */
    private FileType type;

    /**
     * Constructor for the File class.
     * @param nameTmp The name of the file.
     * @param nerTmp  The number of the file.
     * @param annotationTmp  The annotation of the file.
     * @param contentTmp The content of the file.
     * @param parentTmp  The parent of the file.
     * @param typeTmp  The type of the file.
     */
    public File(
        final String nameTmp,
        final int nerTmp,
        final String annotationTmp,
        final String contentTmp,
        final Directory parentTmp,
        final FileType typeTmp
    ) {
        super(nameTmp, nerTmp, annotationTmp, parentTmp);
        this.content = contentTmp;
        this.type = typeTmp;
    }

    /**
     * Minimal constructor for the File class.
     * @param nameTmp The name of the file.
     * @param nerTmp  The number of the file.
     * @param parentTmp  The parent of the file.
     */
    public File(
        final String nameTmp,
        final int nerTmp,
        final Directory parentTmp
    ) {
        super(nameTmp, nerTmp, parentTmp);
    }

    /**
     * Gets the content of the element.
     * @return The content of the element.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Gets the type of the element.
     * @return The type of the element.
     */
    public FileType getType() {
        return this.type;
    }


    /**
     * Indicates if the element is a file.
     * @return true if the element is a file, false otherwise.
     */
    @Override
    public boolean isFile() {
        return true;
    }
    /**
     * Indicates if the element is a directory.
     * @return false since this is a file.
     */
    @Override
    public boolean isDirectory() {
        return false;
    }
    /**
     * calculate file size from his content.
     * @return the size of the file.
     */
    @Override
    public long getSize() {
        if (content == null) {
            return 0;
        }
        return this.getContent().length();
    }

    /**
     * Display the file metadata.
     */
    @Override
    public void listContent() {
        System.out.println(
            "   (Fichier) "
            + this.getNer() + " "
            + this.getName() + " "
            + this.getSize() + " "
            + this.getType() + " "
            + this.getAnnotation()
        );
    }

    /**
     * Delete the current file.
     */
    public void cut() {
        if (this.getParent() != null) {
            this.getParent().deleteChildren(getNer());
            this.setParent(null);
        }
    }

    /**
     * Display the content of a file, or the size
     * if it's not a text file.
     */
    public void visualization() {
        if (this.type == FileType.TEXT) {
            System.out.println(this.content);
        } else {
            System.out.println(this.getSize());
        }
    }
}
