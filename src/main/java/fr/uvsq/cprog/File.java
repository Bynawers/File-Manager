package fr.uvsq.cprog;


public class File extends ElementRepertory {

    /** Types of a file. */
    public enum FileType {
        TEXT,
        IMAGE
    }
    /** The content of the element. */
    private String content;
    /** The type of the element. */
    private FileType type;

    /**
     * Constructor for the File class.
     *
     * @param name The name of the file.
     * @param path The path of the file.
     * @param size The size of the file.
     * @param NER  The number of the file.
     * @param content  The content of the file.
     * @param type  The type of the file.
     */
    public File(
        final String name,
        final String path,
        final long size,
        final int NER,
        final String annotation,
        final String content,
        final FileType type,
        final Directory parent
    ) {
        super(name, path, size, NER, annotation, parent);
        this.content = content;
        this.type = type;
    }
    public File(
        final String name,
        final int NER,
        final Directory parent
    ) {
        super(name, NER, parent);
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
     * Display the file metadata.
     */
    @Override
    public void listContent() {
        System.out.println(
            "   (Fichier) " + 
            this.getNER() + " " + 
            this.getName() + " " + 
            this.getSize() + " " +
            this.getType() + " " +
            this.getAnnotation()
        );
    }

    /**
     * Delete the current file.
     */
    public void cut() {
        if (this.getParent() != null) {
            this.getParent().deleteChildren(getNER());
            this.setParent(null);
        }
    }

    /**
     * Display the content of a file, or the size
     * if it's not a text file
     */
    public void visualization() {
        if (this.type == FileType.TEXT) {
            System.out.println(this.content);
        }
        else {
            System.out.println(this.getSize());
        }
    }
}