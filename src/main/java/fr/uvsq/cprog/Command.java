package fr.uvsq.cprog;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

abstract class Command {
    Map<String, ElementRepertory> currentRepertoryElements = new HashMap<>();
    /** The ner in the command. */
    int ner;
    /** The name of the command. */
    String name;
    /** The arguments of the command. */
    String args;
    /** The path of the command. */
    private String path;

    abstract String getName();
    abstract void execute();

    public void setPath(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }
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
        Directory newDirectory = new Directory(args, 0, "");
        newDirectory.createDirectory(getPath());
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
            return;
        }

        String newPath;
        if (!args.equals("../") && !args.equals("..")) {
            newPath = getPath() + "/" + args;
        }
        else {
            Directory currentDirectory = new Directory(getPath(), 0, "");
            newPath = currentDirectory.goBack(getPath());
        }

        Path pathRef = Paths.get(getPath() + "/" + args);

        if (Files.exists(pathRef) && Files.isDirectory(pathRef)) {
            setPath(newPath);
        } else {
            System.out.println("Ce chemin: " + newPath + " est introuvable");
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
        Directory directory = new Directory("root", 0, "");

        if (!directory.isDirectory()) {
            System.out.println("Le chemin spécifié n'est pas un dossier.");
            return;
        }
        directory.displayElementsRepertory(currentRepertoryElements);
    }
}


class CutCommand extends Command {
    @Override
    public String getName() {
        return "cut";
    }

    @Override
    public void execute() {
        if (this.ner == -1) {
            System.err.println("Error : entrer un ner");
            return;
        }

        fr.uvsq.cprog.FileRef file = new fr.uvsq.cprog.FileRef("delete", 0, "");
        file.delete(currentRepertoryElements, ner);
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
            return;
        }
        String filePath = getPath() + "/" + args;
        FileRef file = new FileRef("visu", 0, "");
        file.visualization(filePath);
    }
}

class FindCommand extends Command {
    @Override
    public String getName() {
        return "find";
    }

    @Override
    public void execute() {
        if (args == null) {
            return;
        }   
    }
}
