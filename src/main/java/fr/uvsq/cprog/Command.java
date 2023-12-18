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
    /* Map du nom éléments du dossier courant associés à leur instance */
    Map<String, ElementRepertory> currentRepertoryElements = new HashMap<>();
    /* Le Ner affecté par la commande */
    int ner;
    /* L'arguments de la commande */
    String args;
    /* Le chemin d'accès */
    String path;
    /* L'élément copié */
    ElementRepertory copy;
    /* L'instance Notes du dossier courant où est effectué la commande */
    Notes notes;

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
     */
    public void setPath(String path) {
        this.path = path;
    }
    /** 
     * Modifie l'aguement de la commande.
     */
    public void setArgs(String args) {
        this.args = args;
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
        for (Map.Entry<String, ElementRepertory> entry : currentRepertoryElements.entrySet()) {
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
        if (args == null) {
            throw new FileManagerException("Argument null");
        }
        Directory newDirectory = new Directory(args, 0, "");
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
        if (args == null) {
            throw new FileManagerException("Argument null");
        }

        String newPath;
        if (!args.equals("../") && !args.equals("..")) {
            newPath = getPath() + "/" + args;
        }
        else {
            Directory currentDirectory = new Directory(getPath(), 0, "");
            newPath = currentDirectory.parentPath(getPath());
        }

        Path pathRef = Paths.get(getPath() + "/" + args);

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
        directory.displayElementsRepertory(currentRepertoryElements);
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
        if (this.ner == -1) {
            throw new FileManagerException("Please enter a ner");
        }

        ElementRepertory element = getElementByNer();
        if (element == null) {
            throw new FileManagerException("File not found");
        }
        copy = element;
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
        if (this.ner == -1) {
            throw new FileManagerException("Please enter a ner");
        }

        for (Map.Entry<String, ElementRepertory> entry : currentRepertoryElements.entrySet()) {
            ElementRepertory element = entry.getValue();
            if (element.getNer() == this.ner && element.isFile()) {
                this.copy = element.getSelf();
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
        if (this.copy == null) {
            throw new FileManagerException("No copy found");
        }
        String copyPath = copy.parentPath(copy.getPath()) + "/" + copy.getNameCopy();
        Path sourcePath = Paths.get(copy.getPath());
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
        if (ner == -1) {
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
        if (args == null) {
            throw new FileManagerException("Argument null");
        }
        Directory currentDirectory = new Directory("root", 0, path);
        currentDirectory.findRecursive(args, path);
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
        if (ner == -1) {
            throw new FileManagerException("Please enter a ner");
        }
        else if (args == null) {
            throw new FileManagerException("Argument null");
        }
        else if (notes == null) {
            throw new FileManagerException("Notes not found");
        }

        ElementRepertory element = getElementByNer();
        String name = element.getName();
        notes.addAnnotation(args, name);
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
        if (ner == -1) {
            throw new FileManagerException("Please enter a ner");
        }
        else if (notes == null) {
            throw new FileManagerException("Notes not found");
        }
        ElementRepertory element = getElementByNer();
        String name = element.getName();
        notes.deleteAnnotation(name);
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
        + "cd <chemin>                  parcours le système de fichier";
        System.out.println(helpOutput);
    }
}