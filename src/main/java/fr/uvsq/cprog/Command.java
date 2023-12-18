package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * La classe abstraite Command impose une structure à chaque
 * classe hérité de Command, cela implique les informations utilitaires
 * pour effectuer les commandes ainsi que des fonctions getName et execute
 * necessaire pour le bon fonctionnement du programme.
 */
abstract class Command {
    /** Map du nom éléments du dossier courant associés à leur instance. */
    private Map<String, ElementRepertory>
            currentRepertoryElements = new HashMap<>();
    /** Le Ner affecté par la commande. */
    private int ner;
    /** L'arguments de la commande. */
    private String args;
    /** Le chemin d'accès. */
    private String path;
    /** L'élément copié. */
    private ElementRepertory copy;
    /** L'instance Notes du dossier courant où est effectué la commande. */
    private Notes notes;

    public Map<String, ElementRepertory> getCurrentRepertoryElements() {
        return currentRepertoryElements;
    }
    public void setCurrentRepertoryElements(
            final Map<String, ElementRepertory> currentRepertoryElementsTemp) {
        this.currentRepertoryElements = currentRepertoryElementsTemp;
    }
    public int getNer() {
        return ner;
    }
    public void setNer(final int nerTemp) {
        this.ner = nerTemp;
    }
    public ElementRepertory getCopy() {
        return copy;
    }

    public void setCopy(final ElementRepertory copyTemp) {
        this.copy = copyTemp;
    }

    public Notes getNotes() {
        return notes;
    }

    public void setNotes(final Notes notesTemp) {
        this.notes = notesTemp;
    }
    /**
     * Obtiens le nom de la commande.
     * @return Chaine de charactère.
     */
    abstract String getName();
    /**
     * Fonction d'execution de la commande.
     */
    abstract void execute() throws FileManagerException;

    /**
     * Modifie le Path de la commande.
     * @param pathTemp Le nouveau chemin.
     */
    public void setPath(final String pathTemp) {
        this.path = pathTemp;
    }
    /**
     * Modifie l'aguement de la commande.
     * @param argsTemp L'argument de la commande.
     */
    public void setArgs(final String argsTemp) {
        this.args = argsTemp;
    }
    /**
     * @return Le Path de la commande.
     */
    public String getPath() {
        return path;
    }
    /**
     * @return L'argument de la commande.
     */
    public String getArgs() {
        return args;
    }

    /**
     * Obtiens l'instance d'un élément à partir de son Ner.
     * @return L'élément associé au Ner de la commande.
     */
    public ElementRepertory getElementByNer() {
        for (Map.Entry<String, ElementRepertory>
                entry : currentRepertoryElements.entrySet()) {
            ElementRepertory element = entry.getValue();
            if (element.getNer() == ner) {
                return element;
            }
        }
        return null;
    }
}


/**
 * Créer un dossier à partir de la commande mkdir, necessite un argument
 * représentant le nom du dossier.
 */
class CreateDirectoryCommand extends Command {
    @Override
    public String getName() {
        return "mkdir";
    }

    @Override
    public void execute() throws FileManagerException {
        if (getArgs() == null) {
            throw new FileManagerException("Argument null");
        }
        Directory newDirectory = new Directory(getArgs(), 0, "");
        newDirectory.createDirectory(getPath());
    }
}

/**
 * Quitte le programme avec la commande exit.
 */
class ExitCommand extends Command {
    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public void execute() {
        System.exit(0);
    }
}

/**
 * Déplacement dans les dossier avec la commande cd, necessite un argument,
 * représentant le chemin à prendre.
 */
class CdCommand extends Command {
    @Override
    public String getName() {
        return "cd";
    }

    @Override
    public void execute() throws FileManagerException {
        if (getArgs() == null) {
            throw new FileManagerException("Argument null");
        }

        String newPath;
        if (!getArgs().equals("../") && !getArgs().equals("..")) {
            newPath = getPath() + "/" + getArgs();
        } else {
            Directory currentDirectory = new Directory(getPath(), 0, "");
            newPath = currentDirectory.parentPath(getPath());
        }

        Path pathRef = Paths.get(getPath() + "/" + getArgs());

        if (Files.exists(pathRef) && Files.isDirectory(pathRef)) {
            setPath(newPath);
        } else {
            throw new FileManagerException("Path not find");
        }
    }
}

/**
 * Permet de retourner dans le dossier parent. (équivalent à cd ../)
 */
class BackCommand extends Command {
    @Override
    public String getName() {
        return "..";
    }

    @Override
    public void execute() throws FileManagerException {

        String newPath;

        Directory currentDirectory = new Directory(getPath(), 0, "");
        newPath = currentDirectory.parentPath(getPath());

        Path pathRef = Paths.get(newPath);

        if (Files.exists(pathRef) && Files.isDirectory(pathRef)) {
            setPath(newPath);
        } else {
            throw new FileManagerException("Path not find");
        }
    }
}

/**
 * Déplacement dans les dossier avec la commande cd, necessite un argument,
 * représentant le chemin à prendre.
 */
class GoToCommand extends Command {
    @Override
    public String getName() {
        return ".";
    }

    @Override
    public void execute() throws FileManagerException {
        if (getNer() == -1) {
            throw new FileManagerException("Please enter a ner");
        }

        ElementRepertory element = getElementByNer();
        if (element == null) {
            throw new FileManagerException("Ner not valid");
        } else if (element.isFile()) {
            throw new FileManagerException("Not a folder");
        }

        String newPath;
        newPath = getPath() + "/" + element.getName();

        Path pathRef = Paths.get(newPath);

        if (Files.exists(pathRef) && Files.isDirectory(pathRef)) {
            setPath(newPath);
        } else {
            throw new FileManagerException("Path not find");
        }
    }
}

/**
 * Affichage des éléments courants ainsi que leur Ner
 * à l'aide de la commande ls (développement seulement).
 */
class LsCommand extends Command {
    @Override
    public String getName() {
        return "ls";
    }

    @Override
    public void execute() throws FileManagerException {
        Directory directory = new Directory("root", 0, "");

        if (!directory.isDirectory()) {
            throw new FileManagerException("Not a directory");
        }
        Map<String, ElementRepertory> elements = getCurrentRepertoryElements();
        directory.displayElementsRepertory(elements);
    }
}

/**
 * Supprime un fichier à partir de son Ner,
 * à l'aide de la commande cut.
 */
class CutCommand extends Command {
    @Override
    public String getName() {
        return "cut";
    }

    @Override
    public void execute() throws FileManagerException {
        if (this.getNer() == -1) {
            throw new FileManagerException("Please enter a ner");
        }

        ElementRepertory element = getElementByNer();
        if (element == null) {
            throw new FileManagerException("File not found");
        }
        setCopy(element);
        element.delete();
    }
}

/**
 * Copie un fichier à partir
 * de son Ner à l'aide de la commande copy
 */
class CopyCommand extends Command {
    @Override
    public String getName() {
        return "copy";
    }

    @Override
    public void execute() throws FileManagerException {
        if (getNer() == -1) {
            throw new FileManagerException("Please enter a ner");
        }

        for (Map.Entry<String, ElementRepertory>
                entry : getCurrentRepertoryElements().entrySet()) {
            ElementRepertory element = entry.getValue();
            if (element.getNer() == getNer() && element.isFile()) {
                setCopy(element.getSelf());
                return;
            }
        }
    }
}

/**
 * Créer un nouveau fichier identique au fichier
 * copié à partir de son Ner, à l'aide de la commande past.
 */
class PastCommand extends Command {
    @Override
    public String getName() {
        return "past";
    }

    @Override
    public void execute() throws FileManagerException{
        if (getCopy() == null) {
            throw new FileManagerException("No copy found");
        }
        String copyPath = getCopy().parentPath(getCopy().getPath())
            + "/" + getCopy().getNameCopy();
        Path sourcePath = Paths.get(getCopy().getPath());
        Path targetPath = Paths.get(copyPath);

        boolean existingFile = new File(copyPath).exists();

        if (existingFile) {
            throw new FileManagerException("Existing file found");
        }

        try {
            Files.copy(sourcePath, targetPath);
        } catch (IOException e) {
        }
    }
}

/**
 * Visualise un fichier à partir de son Ner,
 * si le fichier est un .txt, affiche son contenu,
 * sinon affiche sa taille.
 */
class VisuCommand extends Command {
    @Override
    public String getName() {
        return "visu";
    }

    @Override
    public void execute() throws FileManagerException {
        if (getNer() == -1) {
            throw new FileManagerException("Please enter a ner");
        }

        ElementRepertory element = getElementByNer();
        if (element == null) {
            throw new FileManagerException("File not found");
        }
        else if (element.isDirectory()) {
            throw new FileManagerException("Element is a directory");
        }

        FileRef file = new FileRef("visu", 0, "");
        file.visualization(element.getPath());
    }
}

/**
 * Trouves fichier à partir de son nom à l'aide de la commande find.
 */
class FindCommand extends Command {
    @Override
    public String getName() {
        return "find";
    }

    @Override
    public void execute() throws FileManagerException {
        if (getArgs() == null) {
            throw new FileManagerException("Argument null");
        }
        String currentPath = getPath();
        Directory currentDirectory = new Directory("root", 0, currentPath);
        currentDirectory.findRecursive(getArgs(), currentPath);
    }
}

/**
 * Affecte une annotation à une dossier/fichier à partir de son Ner,
 * à l'aide de la commande +.
 */
class AnnotateCommand extends Command {
    @Override
    public String getName() {
        return "+";
    }

    @Override
    public void execute() throws FileManagerException {
        if (getNer() == -1) {
            throw new FileManagerException("Please enter a ner");
        }
        else if (getArgs() == null) {
            throw new FileManagerException("Argument null");
        }
        else if (getNotes() == null) {
            throw new FileManagerException("Notes not found");
        }

        ElementRepertory element = getElementByNer();
        String name = element.getName();
        Notes notesInstance = getNotes();
        notesInstance.addAnnotation(getArgs(), name);
    }
}

/**
 * Supprime l'annotation d'un fichier/dossier à partir de son Ner,
 * à l'aide de la commande -.
 */
class DesannotateCommand extends Command {
    @Override
    public String getName() {
        return "-";
    }

    @Override
    public void execute() throws FileManagerException {
        if (getNer() == -1) {
            throw new FileManagerException("Please enter a ner");
        }
        else if (getNotes() == null) {
            throw new FileManagerException("Notes not found");
        }
        ElementRepertory element = getElementByNer();
        String name = element.getName();
        getNotes().deleteAnnotation(name);
    }
}

/**
 * Supprime l'annotation d'un fichier/dossier à partir de son Ner,
 * à l'aide de la commande -.
 */
class HelpCommand extends Command {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute() throws FileManagerException {
        String helpOutput = "Listes des commandes :\n" 
        + "mkdir <nom_du_dossier>       créer un dossier\n"
        + "<NER> cut                    effacer un fichier\n"
        + "<NER> visu                   visualise un fichier\n"
        + "find <nom_du_fichier>        trouve le chemin d'un fichier\n"
        + "<NER> + <Annotation>         ajoute une annotation à un fichier\n"
        + "<NER> -                      supprime l'annotation du fichier\n"
        + "<NER> copy                   copie un fichier\n"
        + "past                         créer un fichier à partir de la copie\n"
        + "ls                           affiche les éléments du dossier courant\n"
        + "cd <chemin>                  parcours le système de fichier\n"
        + "<NER> .                      se rendre vers le dossier associé au ner\n"
        + "..                           retourne dans le dossier parent\n"
        + "exit                         quitte l'execution de l'invite de commande.";
        System.out.println(helpOutput);
    }
}