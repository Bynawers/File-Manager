package fr.uvsq.cprog;

/**
 * Classe Abstraite représentant l'élement dans le répertoire.
 */
public abstract class ElementRepertory {
    /** Le nom de l'element. */
    private String name;
    /** Le Ner de l'element. */
    private int ner;
    /** Le path de l'élément. */
    private String path;

    /**
     * Constructeur de la classe ElementRepertory.
     * @param nameTmp Le nom de l'element.
     * @param nerTmp  Le ner de l'élément.
     * @param pathTmp Le path de l'element.
     */
    public ElementRepertory(final String nameTmp,
                            final int nerTmp,
                            final String pathTmp) {
        this.name = nameTmp;
        this.ner = nerTmp;
        this.path = pathTmp;
    }

    /**
     * Retourne le nom de l'élément.
     * @return Le nom de l'élément.
     */
    public String getName() {
        return name;
    }
    /**
     * Modifie le nom de l'élément.
     * @param nameTmp Le nom de l'élément.
     */
    public void setName(final String nameTmp) {
        this.name = nameTmp;
    }
    /** Obtiens le chemin de l"élément.
    * @return Le chemin de l'élément.
    */
    public String getPath() {
        return path;
    }
    /**
     * Modifie le chemin de l'élément.
     * @param  pathTemp le nouveau chemin.
     */
    public void setPath(final String pathTemp) {
        this.path = pathTemp;
    }

    /**
     * Obtiens le ner de l'élément.
     * @return Le ner de l'élément.
     */
    public int getNer() {
        return ner;
    }

    /**
     * Modifie le ner de l'élément.
     * @return ner de l'élément.
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
     * @param pathTemp Le path du dossier.
     * @return Le nom du dossier parent.
     */
    public String lastName(final String pathTemp) {
        if (pathTemp == null) {
            return "";
        }
        String[] splitPath = pathTemp.split("/");
        return splitPath[splitPath.length - 1];
    }

    /**
     * Obtiens le path du dernier dossier dans le path courant.
     * @param paths Le path du dossier.
     * @return Le path du dossier parent.
     */
    public String parentPath(final String paths) {
        if (paths.equals("")) {
            return "";
        }
        String[] splitPath = paths.split("/");
        String newPath = "";

        int i = 0;
        for (String folder: splitPath) {
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
    /**
     * Vérifie si l'object est un dossier.
     * @return vrai si c'est un dossier sinon faux.
    */
    public abstract boolean isDirectory();
    /** Vérifie si l'object est un fichier.
     * @return vrai si c'est un fichier sinon faux.
    */
    public abstract boolean isFile();
    /** Supprime l'élément. */
    public abstract void delete();
}
