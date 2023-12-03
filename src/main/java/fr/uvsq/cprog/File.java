package fr.uvsq.cprog;


public class file extends ElementRepertory {
    /**
     * Constructor for the File class.
     *
     * @param name The name of the file.
     * @param path The path of the file.
     * @param size The size of the file.
     * @param ner  The number of the file.
     */
    public file(final String name, final String path,
                    final long size, final int ner) {
        super(name, path, size, ner);
    }

    /**
     * Indicates if the element is a file.
     *
     * @return true if the element is a file, false otherwise.
     */
    @Override
    public boolean isFile() {
        return true;
    }
    /**
     * Indicates if the element is a directory.
     *
     * @return false since this is a file.
     */
    @Override
    public boolean isDirectory() {
        return false;
    }
}
