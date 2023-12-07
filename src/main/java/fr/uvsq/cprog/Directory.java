package fr.uvsq.cprog;
//import java.io.IOException;
//import java.nio.file.DirectoryStream;
//import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Directory extends ElementRepertory {

    /**
    * List of child elements in the directory.
    */
    private List<ElementRepertory> children;

    /**
     * Constructs a new Directory with the specified attributes.
     *
     * @param nameTmp The name of the directory.
     * @param childrenTmp The list of child elements in the directory.
     * @param nerTmp The number of the directory.
     * @param annotationTmp The annotation of the directory.
     * @param parentTmp The parent of the directory.
     */
    public Directory(
        final String nameTmp,
        final List<ElementRepertory> childrenTmp,
        final int nerTmp,
        final String annotationTmp,
        final Directory parentTmp
    ) {
        super(nameTmp, nerTmp, annotationTmp, parentTmp);
        this.children = childrenTmp;
    }
    /**
     * Minimal constructor for a new Directory with the specified attributes.
     * @param nameTmp The name of the directory.
     * @param nerTmp The number of the directory.
     * @param parentTmp The parent of the directory.
     */
    public Directory(
        final String nameTmp,
        final int nerTmp,
        final Directory parentTmp
    ) {
        super(nameTmp, nerTmp, parentTmp);
        List<ElementRepertory> list = new ArrayList<>();
        this.children = list;
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
    /**
     * Sets the children elements of the directory.
     *
     * @param newChildrens The new list of children elements.
     */
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
     * Find a specific file from its name.
     *
     * @param name The name of the file to find.
     */
    public void find(final String name) {
        if (this.children == null) {
            return;
        }
        for (ElementRepertory element : children) {
            if (element.getName() == name) {
                element.listContent();
            }
            if (element.isDirectory()) {
                element.listContent();
            }
        }
    }
    /**
     * Find a specific file from its name recursively.
     *
     * @param name The name of the file to find.
     */
    public void findRecursive(final String name, String path) {
        File directory = new File(this.getPath().replaceFirst("^\\./", ""));
        searchFile(directory, name, path);
    }
    
    private void searchFile(File directory, String name, String path) {
        File[] directoryChildrens = directory.listFiles();
        
        if (directoryChildrens != null) {
            for (File file : directoryChildrens) {
                Boolean isDirectory = file.isDirectory();
                if (isDirectory) {
                    String newPath = path + "/" + file.getName();
                    searchFile(file, name, newPath);
                }
                if (file.getName().equals(name)) {
                    System.out.println("Le fichier " + file.getName() + " a été trouvé dans le dossier " + path);
                }

            }
        }
    }



    /**
     * Calculate directory size from his content.
     * @return the size of the directory.
     */
    @Override
    public long getSize() {
        int directorySize = 0;

        if (this.children == null) {
            return 0;
        }

        for (ElementRepertory element : children) {
            directorySize += element.getSize();
        }
        return directorySize;
    }

    /**
     * Delete a child of the current directory
     * based on the NER (Number of Element Repertory).
     * @param ner The NER (Number of Element Repertory)
     * of the child to be deleted.
     */
    public void deleteChildren(final long ner) {
        if (this.children == null) {
            return;
        }
        Iterator<ElementRepertory> iterator = children.iterator();
        while (iterator.hasNext()) {
            ElementRepertory element = iterator.next();
            if (element.getNer() == ner) {
                iterator.remove();
            }
        }
    }
}
