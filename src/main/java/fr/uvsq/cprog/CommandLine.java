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

/**
 * Classe principale qui s'occupe de la gestion de l'interface, du prompt, 
 * des commandes des utilisateurs, ainsi que l'execution des commandes.
 */
public class CommandLine {
    /* HashMap de toutes les commandes associés au nom de la commande */
    private final Map<String, Command> commands = new HashMap<>();
    /* HashMap de tous les éléments associés à leur nom */
    private Map<String, ElementRepertory> currentRepertoryElements = new HashMap<>();
    /* Instance de la commande */
    private static Command command;
    /* Path courant du programme */
    private static String currentPath;
    /* Arguement courant du programme */
    private static String currentArgs;
    /* Nom de la commande */
    private static String currentName;
    /* Annotation du dossier courant */
    private static String currentAnnotation;
    /* Element copié courant du programme */
    private static ElementRepertory currentCopy;
    /* Notes du dossier courant */
    private static Notes currentNotes;
    /* Ner de la commande */
    private static int currentNer;

    /**
     * Initialisation de la HashMap commands stockant toutes les commandes.
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

    /**
     * Ajoutes une commande au HashMap Commands permettant ainsi à partir d'un String,
     * d'obtenir l'instance de la commande associé.
     */
    public void addCommand(final Command command) {
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

    /**
     * Analyse l'entrée de l'utilisateur et récupère les informations,
     * en les affectant dans les bonnes variables.
     * @param parsedLine Le tableau d'entrée de l'utilisateur.
     */
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

    /**
     * Modifie les notes du fichier Notes.json si une commande
     * mkdir/past/cut/cd a été utilisé auparavant.
     * @param currentNotes L'instances Note de dossier courant.
     * @param command L'instance Command de la commande executé.
     * @return La nouvelle instance Notes modifées.
     */
    private Notes modifyNotes(Notes currentNotes, Command command) {
        String cmdName = command.getName();
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

    /**
     * Affiches les informations neccessaire à l'utilisateurs afin de connaître
     * l'état actuel du dossier courant.
     * @param path Le path à afficher dans le terminal.
     */
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
    }

    /**
     * Génère l'instance Notes ainsi que son fichier Notes.json  dans le
     * dossier courant, si il existe déjà, vérifie la validité du fichier.
     * @param path Le path du dossier parent.
     */
    public Notes generateNotesFile(String path) {
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
                notes.checkNotes(directoryChildrens);
                return notes;
            }
        }
        notes = new Notes(directoryChildrens, path + "/notes.json");
        notes.writeFile();
        return notes;
    }

    /**
     * Génère les instances du dossier courants et affectes un Ner respectif
     * puis stock dans la Map currentRepertoryElements de la classe.
     * @param path Le path du dossier parent.
     */
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
     * Récupère l'annotation du dossier courant à partir du fichier notes.json du dossier parent.
     * @return L'annotation du dossier courant.
     */
    public String getCurrentPathAnnotation() {

        Directory utility = new Directory("utility", 0, "");
        String parentPath = utility.parentPath(currentPath);
        String ParentDirectory = utility.lastName(currentPath);

        File directory = new File(parentPath);
        File[] directoryChildrens = directory.listFiles();

        Notes parentNotes = new Notes(directoryChildrens, parentPath + "/notes.json");
        
        for (File file : directoryChildrens) {
            
            if (file.getName().equals("notes.json")) {
                parentNotes.readNote();
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
    public static boolean isInteger(final String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public Map<String, ElementRepertory> getCurrentRepertoryElements() {
        return currentRepertoryElements;
    }
    public boolean containsCommand(String commandName) {
        return commands.containsKey(commandName);
    }
}
