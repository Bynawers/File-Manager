package fr.uvsq.cprog;
import java.util.ArrayList;
import java.io.File;
import java.util.List;
public class Directory extends ElementRepertory {
    /**
    * List of child elements in the directory.
    */
    private List<ElementRepertory> children;
    /**
     * Constructs a new Directory with the specified attributes.
     *
     * @param name The name of the directory.
     * @param path The path of the directory.
     * @param size The size of the directory.
     * @param children The list of child elements in the directory.
     * @param ner The number of the directory .
     */
    public Directory(final String name, final String path, final long size,
     final List<ElementRepertory> children, final int ner) {
        super(name, path, size, ner);
        this.children = children;
    }

    /**
     * Indicates if the element is a file.
     *
     * @return true if the element is a file, false otherwise.
     */
    @Override
    public boolean isFile() {
        return false;
    }

    /**
     * Indicates if the element is a Directory.
     *
     * @return true if the element is a Directory, false otherwise.
    */
    @Override
    public boolean isDirectory() {
        return true;
    }
    /**
     * Returns the children elements of the directory.
     *
     * @return List of ElementDirectory representing
     *     the children of the directory.
    */

    public List<ElementRepertory> getChildren() {
        return children;
    }

    /**
     * Recursively returns all files from the specified directory path.
     *
     * @param path The path of the directory to retrieve files from.
    */
    public void returnAllFiles(final String path) {
        returnFiles(new File(path));
    }

    private void returnFiles(final File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                ElementRepertory element;

                if (file.isDirectory()) {
                    element = new Directory(file.getName(), file.getPath(),
                                        file.length(), new ArrayList<>(), 0);
                    returnFiles(file);
                } else {
                    element = new file(file.getName(), file.getPath(),
                                        file.length(), 0);
                }
                children.add(element);
            }
        }
    }
}
