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

    /**
     * Obtiens le nom du fichier copié à partir du nom de l'instance,
     * exemple test.txt -> test_copie.txt.
     * @return Le nom de la copie.
     */
    public String getNameCopy() {
        String[] splitName = this.getName().split("\\.");
        if (splitName.length == 1) {
            String nameElement = splitName[0];
            return nameElement + "_copie";
        }
        if (splitName.length > 2) {
            return name;
        }
        String nameElement = splitName[0];
        String extensionElement = splitName[1];
        return nameElement + "_copie." + extensionElement;
    }

    /**
     * Obtiens le nom du dernier dossier dans le path courant.
     * @return Le nom du dossier parent.
     */
    public String lastName(String path) {
        if (path == null) {
            return "";
        }
        String[] splitPath = path.split("/");
        return splitPath[splitPath.length - 1];
    }

    /**
     * Obtiens le path du dernier dossier dans le path courant.
     * @return Le path du dossier parent.
     */
    public String parentPath(String paths) {
        if (paths.equals("")) {
            return "";
        }
        String[] splitPath = paths.split("/");
        String newPath = "";

        int i = 0;
        for(String folder: splitPath) {
            if (i >= splitPath.length - 1) {
                return "/" + newPath;
            }
            newPath = newPath == "" ? folder : newPath + "/" + folder;
            i++;
        }
        return "";
    }

    /**
     * Obtiens l'instance de la classe.
     * @return L'instance de la classe.
     */
    public ElementRepertory getSelf() {
        return this;
    }

    abstract public boolean isDirectory();
    abstract public boolean isFile();
    abstract public void delete();
}
