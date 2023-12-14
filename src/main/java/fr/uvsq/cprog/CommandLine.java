package fr.uvsq.cprog;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandLine {
     /** Map to store commands. */
    private final Map<String, Command> commands = new HashMap<>();
    private Map<String, ElementRepertory> currentRepertoryElements = new HashMap<>();

    /**
     * List of all commands.
     */
    public void initializeCommands() {
        addCommand(new CreateDirectoryCommand());
        addCommand(new CdCommand());
        addCommand(new ExitCommand());
        addCommand(new CutCommand());
        addCommand(new LsCommand());
        addCommand(new FindCommand());
        addCommand(new CopyCommand());
        addCommand(new PastCommand());
        addCommand(new VisuCommand());
        addCommand(new AnnotateCommand());
        addCommand(new DesannotateCommand());
    }

    private void addCommand(final Command command) {
        commands.put(command.getName(), command);
    }
    /**
     * Starts the command line interface.
     * Override this method in a subclass to customize the behavior.
     *
     * @throws IOException if an I/O error occurs
     */
    public void start() throws IOException {

        AnsiConsole.systemInstall();

        Command command = null;

        String currentPath = System.getProperty("user.dir");

        int previousNer = -1;

        Terminal terminal = TerminalBuilder.builder().build();
        LineReader lineReader = LineReaderBuilder
            .builder().terminal(terminal).build();

        generateNotesFile(currentPath);
        generateInstancesRepertory(currentPath);

        while (true) {

            String cmdPath = command != null ? command.getPath() : currentPath;

            displayInterface(cmdPath);
            String userInput = lineReader.readLine("> ");

            String[] parsedLine = userInput.split("\\s+");

            int cmdNer = parsedLine.length > 0
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
                ? cmdArgs = parsedLine.length > 2
                ? parsedLine[2]
                : parsedLine[1]
                : parsedLine[1]
                : null;
            ElementRepertory copy = command != null ? command.copy : null;
            Notes notes = command != null ? command.notes : null;

            command = commands.get(cmdName);

            if (command != null) {
                //System.out.println("COMMANDES \nner : "+cmdNer+"\nname : "+cmdName+"\nargs : "+cmdArgs+"\npath : "+cmdPath);
                command.ner = cmdNer;
                command.name = cmdName;
                command.args = cmdArgs;
                command.copy = copy;
                command.notes = notes;
                if (cmdPath != null) {
                    command.setPath(cmdPath);
                }
                command.currentRepertoryElements = currentRepertoryElements;
                command.execute();
                if (cmdName.equals("cd") || cmdName.equals("mkdir") || cmdName.equals("cut") || cmdName.equals("past")) {
                    command.notes = generateNotesFile(command.getPath());
                    generateInstancesRepertory(command.getPath());
                }
            } else {
                // command not found
            }
        }
    }

    public void displayInterface(String path) {

        Directory currentDirectory = new Directory(path, 0, "");

        AnsiConsole.out()
            .println(Ansi.ansi()
            .fg(Ansi.Color.GREEN)
            .a(path)
            .reset());
        currentDirectory.displayElementsRepertory(currentRepertoryElements);

        AnsiConsole.out()
            .print(Ansi.ansi()
            .fg(Ansi.Color.GREEN)
            .a("[" + currentDirectory.lastName(path) + "]$ ")
            .reset());

        // TODO annotation du répertoire courant
    }

    // TODO si notes.json existe juste modifier
    public Notes generateNotesFile(String path) {
        File directory = new File(path);
        File[] directoryChildrens = directory.listFiles();

        Notes notes = new Notes(directoryChildrens, path + "/notes.json");

        if (directoryChildrens == null) {
            // Exception pas d'enfants
            return null;
        }

        for (File file : directoryChildrens) {
            if (file.getName().equals("notes.json")) {
                notes.readNote();
                notes.createFile();
                return notes;
            }
        }
        notes = new Notes(directoryChildrens, path + "/notes.json");
        notes.createFile();
        return notes;
    }

    public void generateInstancesRepertory(String path) {
        if (path == null) {
            return;
        }
        currentRepertoryElements = new HashMap<>();
        int ner = 1;
        String newPath = "";

        File currentDirectory = new File(path);
        File[] directoryChildrens = currentDirectory.listFiles();

        if (directoryChildrens == null) {
            // Exception
            return;
        }

        for (File file : directoryChildrens) {
            
            newPath = path + "/" + file.getName();

            if (file.isDirectory()) {
                currentRepertoryElements.put(file.getName(), new Directory(file.getName(), ner, newPath));
            }
            else if (file.isFile()) {
                currentRepertoryElements.put(file.getName(), new FileRef(file.getName(), ner, newPath));
            }
            else {
                //exception
            }
            ner++;
        }
    }
    
    /**
     * Checks if the given string can be parsed as an integer.
     *
     * @param str the string to check
     * @return true if the string can be parsed as an integer, false otherwise
    */
    public static boolean isInteger(final String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
