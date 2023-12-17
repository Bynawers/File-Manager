package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
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
    String path;

    ElementRepertory copy;
    Notes notes;

    abstract String getName();
    abstract void execute();

    public void setPath(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }

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


class CreateDirectoryCommand extends Command {
    @Override
    public String getName() {
        return "mkdir";
    }

    @Override
    public void execute() {
        if (args == null) {
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
            newPath = currentDirectory.parentPath(getPath());
        }

        Path pathRef = Paths.get(getPath() + "/" + args);

        if (Files.exists(pathRef) && Files.isDirectory(pathRef)) {
            setPath(newPath);
        } else {
            // chemin introuvable
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
            // pas un dossier
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
            return;
        }

        ElementRepertory element = getElementByNer();
        copy = element;
        element.delete();
    }
}
// TODO ajouter copy folder
class CopyCommand extends Command {
    @Override
    public String getName() {
        return "copy";
    }

    @Override
    public void execute() {
        if (this.ner == -1) {
            // entrer un ner
            return;
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

class PastCommand extends Command {
    @Override
    public String getName() {
        return "past";
    }

    @Override
    public void execute() {
        if (this.copy == null) {
            return;
        }
        String copyPath = copy.parentPath(copy.getPath()) + "/" + copy.getNameCopy();
        Path sourcePath = Paths.get(copy.getPath());
        Path targetPath = Paths.get(copyPath);

        boolean existingFile = new File(copyPath).exists();

        if (existingFile) {
            return;
        }

        try {
            Files.copy(sourcePath, targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class VisuCommand extends Command {
    @Override
    public String getName() {
        return "visu";
    }

    @Override
    public void execute() {
        if (ner == -1) {
            return;
        }

        ElementRepertory element = getElementByNer();

        FileRef file = new FileRef("visu", 0, "");
        file.visualization(element.getPath());
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
        Directory currentDirectory = new Directory("root", 0, path);
        currentDirectory.findRecursive(args, path);
    }
}

class AnnotateCommand extends Command {
    @Override
    public String getName() {
        return "+";
    }

    @Override
    public void execute() {
        if (ner == -1 || args == null || notes == null) {
            return;
        }

        ElementRepertory element = getElementByNer();
        String name = element.getName();
        notes.addAnnotation(args, name);
    }
}

class DesannotateCommand extends Command {
    @Override
    public String getName() {
        return "-";
    }

    @Override
    public void execute() {
        if (ner == -1 || notes == null) {
            return;
        }
        ElementRepertory element = getElementByNer();
        String name = element.getName();
        notes.deleteAnnotation(name);
    }
}
