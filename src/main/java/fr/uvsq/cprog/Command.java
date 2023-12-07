package fr.uvsq.cprog;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.FileReader;

abstract class Command {
    /** The ner in the command. */
    long ner;
    /** The name of the command. */
    String name;
    /** The arguments of the command. */
    String args;
    /** The path of the command. */
    String path;

    abstract String getName();
    abstract void execute();
}


class CreateDirectoryCommand extends Command {

    @Override
    public String getName() {
        return "mkdir";
    }

    @Override
    public void execute() {

        if (args == null) {
            System.out.println("Veuillez entrer le nom du dossier");
            return;
        }

        try {
            String newPath = path + "/" + args;
            Path pathRef = Paths.get(newPath);
            System.out.println(pathRef);
            Files.createDirectory(pathRef);
        } catch (FileAlreadyExistsException e) {
            System.out.println("Erreur : Le dossier '"
                                + args + "' existe déjà");
            return;
        } catch (IOException e) {
            System.out.println("Erreur : Impossible de creer le dossier.");
            e.printStackTrace();
            return;
        }
    }
}

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

class CdCommand extends Command {
    @Override
    public String getName() {
        return "cd";
    }

    @Override
    public void execute() {

        if (args == null) {
            System.out.println("Aide : cd <chemin_du_dossier>");
        }

        String newPath = path + "/" + args;
        Path pathRef = Paths.get(newPath);

        if (Files.exists(pathRef) && Files.isDirectory(pathRef)) {
            this.path = newPath;
        } else {
            System.out.println("Ce chemin: '" + newPath + "'' est introuvable");
        }
    }
}

class LsCommand extends Command {
    @Override
    public String getName() {
        return "ls";
    }

    @Override
    public void execute() {

        File directory = new File(path);

        if (!directory.isDirectory()) {
            System.out.println("Le chemin spécifié n'est pas un dossier.");
            return;
        }

        File[] directoryChildrens = directory.listFiles();

        if (directoryChildrens != null) {
            for (File file : directoryChildrens) {
                Boolean isDirectory = file.isDirectory();

                if (isDirectory) {
                    AnsiConsole.out()
                    .print(Ansi.ansi()
                    .fg(Ansi.Color.BLUE)
                    .a(file.getName() + " ")
                    .reset());

                } else {
                    System.out.print(file.getName() + " ");

                }
            }
            System.out.println();
        }
    }
}


class CutCommand extends Command {
    @Override
    public String getName() {
        return "cut";
    }

    @Override
    public void execute() {
        System.out.println("cut");
    }
}

class PastCommand extends Command {
    @Override
    public String getName() {
        return "past";
    }

    @Override
    public void execute() {
        System.out.println("past");
    }
}

class VisuCommand extends Command {
    @Override
    public String getName() {
        return "visu";
    }

    @Override
    public void execute() {
        if (args == null) {
            System.out.println("Aide : <NER> visu");
        }
        String newP = path + "/" + args;
        File file = new File(newP);

        if (file.exists()) {
            String fileName = file.getName();

            if (fileName.endsWith(".txt")) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(newP));
                    StringBuilder content = new StringBuilder();
                    String line;
                    System.out.println("Le contenu du fichier " + fileName + " est :");
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    reader.close();
                    System.out.println(content.toString());
                }catch (IOException e) {
                    e.printStackTrace();
                }

            }else{
                System.out.println("Le fichier n'est pas un texte");
                long fileSize = file.length();
                System.out.println("La taille du fichier est de : " + fileSize);
            }
        }else{
            System.out.println("Le fichier n'existe pas");
        }
    }
}

class FindCommand extends Command {
    @Override
    public String getName() {
        return "find";
    }

    @Override
    public void execute() {
        if (args != null) {
            Directory currentDirectory = new Directory(path, 0, null);
            currentDirectory.findRecursive(args,path);
        } else {
            System.out.println("Aide : find <nom_fichier>");
        }
    }
    
}
