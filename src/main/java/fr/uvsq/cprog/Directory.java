package fr.uvsq.cprog;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;
import java.util.List;

public class Directory extends ElementRepertory {

    /**
    * List of child elements in the directory.
    */
    private List<ElementRepertory> children;
    /**
    * Parent of child elements in the directory.
    */
    private Directory parent;

    /**
     * Constructs a new Directory with the specified attributes.
     *
     * @param name The name of the directory.
     * @param path The path of the directory.
     * @param size The size of the directory.
     * @param children The list of child elements in the directory.
     * @param NER The number of the directory.
     * @param annotation The annotation of the directory.
     * @param parent The parent of the directory.
     */
    public Directory(
        final String name,
        final String path,
        final long size,
        final List<ElementRepertory> children,
        final int NER,
        final String annotation,
        final Directory parent
    ) {
        super(name, path, size, NER, annotation, parent);
        this.children = children;
        this.parent = parent;
    }
    public Directory(
        final String name,
        final int NER,
        final Directory parent
    ) {
        super(name, NER, parent);
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

    /**
     * Returns the children elements of the directory.
     *
     * @return List of ElementDirectory representing
     *     the children of the directory.
     */
    public List<ElementRepertory> getChildren() {
        return this.children;
    }

    public void setChildren(final List<ElementRepertory> newChildrens) {
        this.children = newChildrens;
    }

    /**
     * Browse all folders in the current folder and list all files.
     */
    @Override
    public void listContent() {
        if (this.children == null) {
            return;
        }
        System.out.println("(Dossier) " + this.getName() + " :");
        for (ElementRepertory element : children) {
            element.listContent();
        }
    }

    /**
     * Find a specific file from his name.
     */
    public void find(final String name) {
        if (this.children == null) {
            return;
        }
        for (ElementRepertory element : children) {
            if (element.getName() == name) {
                element.listContent();
            }
            if (element.isDirectory() == true) {
                element.listContent();
            }
        }
    }

    /**
     * Delete a children of the current file from the NER.
     */
    public void deleteChildren(final long NER) {
        if (this.children == null) {
            return;
        }
        Iterator<ElementRepertory> iterator = children.iterator();
        while (iterator.hasNext()) {
            ElementRepertory element = iterator.next();
            if (element.getNER() == NER) {
                iterator.remove();
            }
        }
    }
}