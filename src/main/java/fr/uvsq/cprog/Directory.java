package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class Directory extends ElementRepertory {

    /**
     * Constructs a new Directory with the specified attributes.
     *
     * @param nameTmp The name of the directory.
     * @param nerTmp The number of the directory.
     */
    public Directory(final String nameTmp, final int nerTmp, final String pathTmp) {
        super(nameTmp, nerTmp, pathTmp);
    }

    public boolean isDirectory() {
        return true;
    }
    public boolean isFile() {
        return false;
    }

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

    public void createDirectory(String path) {
        try {
            String newPath = path + "/" + this.getName();
            Path pathRef = Paths.get(newPath);
            System.out.println(pathRef);
            Files.createDirectory(pathRef);
        } catch (FileAlreadyExistsException e) {
            System.out.println("Erreur : Le dossier '"
                + this.getName() + "' existe déjà");
            return;
        } catch (IOException e) {
            System.out.println("Erreur : Impossible de creer le dossier.");
            e.printStackTrace();
            return;
        }
    }
    public String lastDirectory(String path) {
        String[] splitPath = path.split("/");
        return splitPath[splitPath.length - 1];
    }

    public String goBack(String paths) {
        String[] splitPath = paths.split("/");
        System.out.println("go back");
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
}
