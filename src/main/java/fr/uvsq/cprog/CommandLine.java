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

    private static Command command;
    private static String currentPath;
    private static String currentName;
    private static String currentArgs;
    private static String currentAnnotation;
    private static ElementRepertory currentCopy;
    private static Notes currentNotes;
    private static int currentNer;


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

        Terminal terminal = TerminalBuilder.builder().build();
        LineReader lineReader = LineReaderBuilder
            .builder().terminal(terminal).build();

        command = null;
        currentPath = System.getProperty("user.dir");
        currentNer = -1;
        currentNotes = null;
        currentCopy = null;
        currentName = null;
        currentAnnotation = "";
        currentArgs = "";

        currentNotes = generateNotesFile(currentPath);
        generateInstancesRepertory(currentPath);
        currentAnnotation = getCurrentPathAnnotation();

        while (true) {
            if (command != null) {
                currentPath = command.getPath();
            }

            displayInterface(currentPath);
            String userInput = lineReader.readLine("> ");

            String[] parsedLine = userInput.split("\\s+");
            parseUser(parsedLine);
            
            if (command != null) {
                currentCopy = command.copy;
            }

            command = currentName != null ? commands.get(currentName) : null;

            if (command != null) {
                command.ner = currentNer;
                command.name = currentName;
                command.args = currentArgs;
                command.copy = currentCopy;
                command.notes = currentNotes;

                if (currentPath != null) {
                    command.setPath(currentPath);
                }
                command.currentRepertoryElements = currentRepertoryElements;

                command.execute();

                currentNotes = modifyNotes(currentNotes, command);

                generateInstancesRepertory(command.getPath());
                currentPath = command.getPath();
                currentAnnotation = getCurrentPathAnnotation();
            } else {
                // command not found
            }
        }
    }

    private void parseUser(String[] parsedLine) {
        currentNer = parsedLine.length > 0
            ? isInteger(parsedLine[0])
            ? Integer.parseInt(parsedLine[0])
            : currentNer
            : currentNer;
        currentName = parsedLine.length > 0
            ? isInteger(parsedLine[0]) && parsedLine.length > 1
            ? parsedLine[1]
            : parsedLine[0]
            : null;
        currentArgs = parsedLine.length > 1
            ? isInteger(parsedLine[0])
            ? parsedLine.length > 2
            ? parsedLine[2]
            : parsedLine[1]
            : parsedLine[1]
            : null;
    }

    private Notes modifyNotes(Notes currentNotes, Command command) {
        String cmdName = command.name;
        String nameFile = "";

        if (cmdName.equals("mkdir")) {
            nameFile = command.args;
            currentNotes.addNote(nameFile);

        } else if (cmdName.equals("past")) {
            nameFile = command.copy.getNameCopy();
            currentNotes.addNote(nameFile);

        } else if (cmdName.equals("cut")) {
            nameFile = command.copy.getName();
            currentNotes.deleteNote(nameFile);
        }
        else if (cmdName.equals("cd")) {
            currentNotes = generateNotesFile(command.getPath());
        }
        return currentNotes;
    }

    private void displayInterface(String path) {

        Directory currentDirectory = new Directory(path, 0, "");

        AnsiConsole.out()
            .print(Ansi.ansi()
            .fg(Ansi.Color.GREEN)
            .a(path + " ")
            .reset());

        AnsiConsole.out()
            .println(Ansi.ansi()
            .fg(Ansi.Color.YELLOW)
            .a(currentAnnotation)
            .reset());

        currentDirectory.displayElementsRepertory(currentRepertoryElements);

        AnsiConsole.out()
            .print(Ansi.ansi()
            .fg(Ansi.Color.GREEN)
            .a("[" + currentDirectory.lastName(path) + "]$ ")
            .reset());

        // TODO annotation du répertoire courant
    }

    private Notes generateNotesFile(String path) {
        File directory = new File(path);
        File[] directoryChildrens = directory.listFiles();

        Notes notes = new Notes(null, path + "/notes.json");

        if (directoryChildrens == null) {
            // Exception pas d'enfants
            return null;
        }

        for (File file : directoryChildrens) {
            if (file.getName().equals("notes.json")) {
                notes.readNote();
                //notes.writeFile();
                notes.checkNotes(directoryChildrens);
                return notes;
            }
        }
        notes = new Notes(directoryChildrens, path + "/notes.json");
        notes.writeFile();
        return notes;
    }

    private void generateInstancesRepertory(String path) {
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

    private String getCurrentPathAnnotation() {

        Directory utility = new Directory("utility", 0, "");
        String parentPath = utility.parentPath(currentPath);
        String ParentDirectory = utility.lastName(currentPath);

        File directory = new File(parentPath);
        File[] directoryChildrens = directory.listFiles();

        Notes parentNotes = new Notes(directoryChildrens, parentPath + "/notes.json");
        
        for (File file : directoryChildrens) {
            
            if (file.getName().equals("notes.json")) {
                parentNotes.readNote();
                parentNotes.displayNotes();
                return parentNotes.getAnnotation(ParentDirectory);
            }
        }
        return "";
    }
    
    /**
     * Checks if the given string can be parsed as an integer.
     *
     * @param str the string to check
     * @return true if the string can be parsed as an integer, false otherwise
    */
    private static boolean isInteger(final String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
