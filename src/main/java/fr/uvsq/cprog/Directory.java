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
     * Construit une instance de dossier.
     * @param nameTmp Le nom du dossier.
     * @param nerTmp Le ner du dossier.
     * @param pathTmp Le chemin du dossier.
     */
    public Directory(final String nameTmp, final int nerTmp,
                        final String pathTmp) {
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
     * Pas utilisé.
     */
    @Override
    public void delete() { }

    /**
     * Affiche tous les éléments de currentRepertoryElements dans l'interface.
     *
     * @param currentRepertoryElements Les éléments actuels du répertoire.
     */
    public void displayElementsRepertory(
                final Map<String, ElementRepertory> currentRepertoryElements) {

        for (Map.Entry<String, ElementRepertory>
                entry : currentRepertoryElements.entrySet()) {
            ElementRepertory element = entry.getValue();
            if (element.isDirectory()) {
                System.out.print("[" + element.getNer() + "] ");
                AnsiConsole.out()
                    .println(Ansi.ansi()
                    .fg(Ansi.Color.BLUE)
                    .a(element.getName())
                    .reset());
            } else {
                System.out.println("[" + element.getNer()
                                       + "] " + element.getName());
            }
        }
    }

    /**
     * Crée un nouveau dossier à partir du path.
     * @param path Chemin d'accès où créer le nouveau dossier.
     */
    public void createDirectory(final String path) {
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
     * Trouve un fichier spécifique récursivement à partir du dossier courant.
     * @param name Le nom du fichier à trouver.
     * @param path Le chemin d'accès pour la recherche du fichier.
     */
    public void findRecursive(final String name, final String path) {
        File directory = new File(this.getPath().replaceFirst("^\\./", ""));
        searchFile(directory, name, path);
    }

    /**
     * Recherche un fichier à partir de son nom, récursivement
     * dans un dossier ainsi que ses sous dossier.
     * @param directory Le dossier racine de la recherche.
     * @param name Le nom du fichier à trouver.
     * @param path Le chemin d'accès qui sera
     * afficher si le fichier est trouvé.
     */
    private void searchFile(final File directory,
                                final String name, final String path) {
        File[] directoryChildrens = directory.listFiles();

        if (directoryChildrens != null) {
            for (File file : directoryChildrens) {
                Boolean isDirectory = file.isDirectory();
                if (isDirectory) {
                    String newPath = path + "/" + file.getName();
                    searchFile(file, name, newPath);
                }
                if (file.getName().equals(name)) {
                    System.out.println("Le fichier "
                        + file.getName() + " a été trouvé dans le dossier "
                        + path);
                }

            }
        }
    }
}
