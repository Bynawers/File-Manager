package fr.uvsq.cprog;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class CommandLine {

    private final Map<String, Command> commands = new HashMap<>();

    /**
     * List of all commands.
     */
    public void initializeCommands() {
        addCommand(new CreateDirectoryCommand());
        addCommand(new CdCommand());
        addCommand(new ExitCommand());
    }

    private void addCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public void start() throws IOException {

        AnsiConsole.systemInstall();

  
        Path currRelativePath = Paths.get("");
        String currAbsolutePathString = currRelativePath.toAbsolutePath().toString();
        Directory root = new Directory("miniprojet-grp-11_22", 1, null);
        Directory current = root;

        long previousNer = -1;

        Terminal terminal = TerminalBuilder.builder().build();
        LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();

        while (true) {
            AnsiConsole.out().println(Ansi.ansi().fg(Ansi.Color.GREEN).a(current.getPath()).reset());

            String userInput = lineReader.readLine("prompt> ");
    
            String[] parsedLine = userInput.split("\\s+");

            long cmdNer = parsedLine.length > 0 
                ? isInteger(parsedLine[0]) 
                ? Integer.parseInt(parsedLine[0]) 
                : previousNer 
                : -1;
            String cmdName = parsedLine.length > 0 
                ? isInteger(parsedLine[0]) 
                ? parsedLine[1] 
                : parsedLine[0]
                : null;
            String cmdArgs = parsedLine.length > 1 
                ? isInteger(parsedLine[0]) 
                ? parsedLine[2] 
                : parsedLine[1] 
                : null;
            Directory cmdParent = current;

            Command command = commands.get(cmdName);
            
            if (command != null) {
                command.ner = cmdNer;
                command.name = cmdName;
                command.args = cmdArgs;
                command.parent = cmdParent;
                command.execute();
            } else {
                System.out.println(cmdName + " command not found");
            }
        }
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}


abstract class Command {
    long ner;
    String name;
    String args;
    Directory parent;

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
        String dirName = args;
        int ner = 0;// TODO
        
        
        String newDirectoryPath = currAbsolutePathString+ "/" + dirName;
        Directory newDirectory = new Directory(dirName, ner, parent);
        List<ElementRepertory> sons = parent.getChildren();
        sons.add(newDirectory);
        parent.setChildren(sons);

        try {
            System.out.println("dscvdsljnjs  "+newDirectoryPath);
            Path newPath = Paths.get(newDirectoryPath); 
            Files.createDirectory(newPath);
        } catch (FileAlreadyExistsException e) {
            System.out.println("Erreur : Le dossier '" + dirName + "' existe déjà dans " + parent.getPath() + ".");
        } catch (IOException e) {
            System.out.println("Erreur : Impossible de creer le dossier.");
            e.printStackTrace();
        }
        System.out.println("Création du dossier : " + newDirectory.getName());
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

class CdCommand extends Command { // TO fIX
    @Override
    public String getName() {
        return "cd";
    }

    @Override
    public void execute() {

        if (args != null) {
            String targetPath = args;
            Path newPath = Paths.get(targetPath);
            //String chemin = parent.getPath()+"/"+args;

            if (Files.exists(newPath) && Files.isDirectory(newPath)) {
                parent = new Directory(targetPath, 0, parent);
                System.out.println("Changement du répertoire courant : " + parent.getPath());
            }else{
                System.out.println("Ce chemin: '"+ newPath +"'' est introuvable");
            }
        }else{
            System.out.println("Aide : cd <chemin_du_dossier>");
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
        System.out.println("visu");
    }
}

class FindCommand extends Command {
    @Override
    public String getName() {
        return "find";
    }

    @Override
    public void execute() {
        System.out.println("find");
    }
}
