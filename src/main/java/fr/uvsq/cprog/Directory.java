package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class Directory extends ElementRepertory {

    /**
     * Constructs a new Directory with the specified attributes.
     *g
     * @param nameTmp The name of the directory.
     * @param nerTmp The number of the directory.
     * @param pathTmp The path of the directory.
     */
    public Directory(final String nameTmp, final int nerTmp, final String pathTmp) {
        super(nameTmp, nerTmp, pathTmp);
    }

    /**
     * Indique que l'élément est un dossier.
     * @return Booléen vrai.
     */
    @Override
    public boolean isDirectory() {
        return true;
    }
    /**
     * Indique que l'élément n'est pas un fichier.
     * @return Booléen faux.
     */
    @Override
    public boolean isFile() {
        return false;
    }

    /**
     * Supprime récursivement le dossier ainsi que tous ses éléments.
     */
    @Override
    public void delete() {
    }

    /**
     * Affiche tous les éléments de currentRepertoryElements dans l'interface.
     */
    public void displayElementsRepertory(Map<String, ElementRepertory> currentRepertoryElements) {
        for (Map.Entry<String, ElementRepertory> entry : currentRepertoryElements.entrySet()) {
            ElementRepertory element = entry.getValue();
            if (element.isDirectory()) {
                System.out.print("[" + element.getNer() + "] ");
                AnsiConsole.out()
                    .println(Ansi.ansi()
                    .fg(Ansi.Color.BLUE)
                    .a(element.getName())
                    .reset());
            } else {
                System.out.println("[" + element.getNer() + "] " + element.getName());
            }
        }
    }

    /**
     * Crée un nouveau dossier à partir du path.
     * @param path Chemin d'accès où créer le nouveau dossier.
     */
    public void createDirectory(String path) {
        try {
            String newPath = path + "/" + this.getName();
            Path pathRef = Paths.get(newPath);
            Files.createDirectory(pathRef);
        } catch (FileAlreadyExistsException e) {
            // fichier existe déjà
            return;
        } catch (IOException e) {
            // impossible créer dossier
            return;
        }
    }

    /**
     * Find a specific file from its name recursively.
     * @param name The name of the file to find.
     */
    public void findRecursive(final String name, String path) {
        File directory = new File(this.getPath().replaceFirst("^\\./", ""));
        searchFile(directory, name, path);
    }
    
    /**
     * Recherche un fichier à partir de son nom, récursivement
     * dans un dossier ainsi que ses sous dossier.
     * @param directory Le dossier racine de la recherche.
     * @param name Le nom du fichier à trouver.
     * @param path Le chemin d'accès qui sera affichier si le fichier est trouvé.
     */
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
}
