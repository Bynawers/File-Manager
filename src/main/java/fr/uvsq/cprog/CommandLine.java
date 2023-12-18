package fr.uvsq.cprog;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.ParsedLine;
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
    /** HashMap de toutes les commandes associés au nom de la commande. */
    private final Map<String, Command> commands = new HashMap<>();
    /* HashMap de tous les éléments associés à leur nom */
    private static Map<String, ElementRepertory>
        currentRepertoryElements = new HashMap<>();
    /* Instance de la commande */
    private static Command command;
    /** Path courant du programme. */
    private static String currentPath;
    /** Arguement courant du programme. */
    private static String currentArgs;
    /** Nom de la commande. */
    private static String currentName;
    /** Annotation du dossier courant. */
    private static String currentAnnotation;
    /** Element copié courant du programme. */
    private static ElementRepertory currentCopy;
    /** Notes du dossier courant. */
    private static Notes currentNotes;
    /** Ner de la commande. */
    private static int currentNer;
    /* Erreur de la commande */
    private static String currentError;

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
        addCommand(new HelpCommand());
    }

    /**
     * Ajoutes une commande au HashMap Commands permettant
     * ainsi à partir d'un String,
     * d'obtenir l'instance de la commande associé.
     * @param commandTemp La commande
     */
    public void addCommand(final Command commandTemp) {
        commands.put(commandTemp.getName(), commandTemp);
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
            .builder()
            .terminal(terminal)
            .completer(new MultiCompleter())
            .build();

        command = null;
        currentPath = System.getProperty("user.dir");
        currentNer = -1;
        currentNotes = null;
        currentCopy = null;
        currentName = null;
        currentAnnotation = "";
        currentArgs = "";
        currentError = "";

        currentNotes = generateNotesFile(currentPath);
        generateInstancesRepertory(currentPath);
        currentAnnotation = "";

        while (true) {
            if (command != null) {
                currentPath = command.getPath();
            }
            displayInterface(currentPath);
            displayError(currentError);
            currentError = "";
            String userInput = lineReader.readLine("prompt> ");

            String[] parsedLine = userInput.split("\\s+");
            parseUser(parsedLine);

            if (command != null) {
                currentCopy = command.getCopy();
            }

            command = currentName != null ? commands.get(currentName) : null;

            if (command != null) {
                command.setNer(currentNer);
                command.setArgs(currentArgs);
                command.setCopy(currentCopy);
                command.setNotes(currentNotes);

                if (currentPath != null) {
                    command.setPath(currentPath);
                }
                command.setCurrentRepertoryElements(currentRepertoryElements);

                try {
                    command.execute();
                } catch(FileManagerException e) {
                    currentError = e.getMessage();
                }

                currentNotes = modifyNotes(currentNotes, command);

                generateInstancesRepertory(command.getPath());

                currentPath = command.getPath();
            } else {
                currentError = "Command not found";
            }

            ElementRepertory currentFile = getElementByNer(currentNer);
            currentAnnotation = currentFile != null ? currentNotes.getAnnotation(currentFile.getName()) : "";
        }
    }

    /**
     * Analyse l'entrée de l'utilisateur et récupère les informations,
     * en les affectant dans les bonnes variables.
     * @param parsedLine Le tableau d'entrée de l'utilisateur.
     */
    public void parseUser(final String[] parsedLine) {
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
     * @param currentNotesTemp L'instances Note de dossier courant.
     * @param commandTemp L'instance Command de la commande executé.
     * @return La nouvelle instance Notes modifées.
     */
    public Notes modifyNotes(Notes currentNotesTemp,
                             final Command commandTemp) {
        String cmdName = commandTemp.getName();
        String nameFile = "";

        if (cmdName.equals("mkdir") && command.getArgs() != null) {
            nameFile = command.getArgs();
            currentNotesTemp.addNote(nameFile);

        } else if (cmdName.equals("past") && command.getCopy() != null) {
            nameFile = command.getCopy().getNameCopy();
            currentNotesTemp.addNote(nameFile);

        } else if (cmdName.equals("cut") && command.getNer() != -1 && command.getCopy() != null) {
            nameFile = command.getCopy().getName();
            currentNotesTemp.deleteNote(nameFile);
        } else if (cmdName.equals("cd")) {
            currentNotesTemp = generateNotesFile(command.getPath());
        }
        return currentNotesTemp;
    }

    public void displayError(String message) {
        if (message.equals("")) {
            return;
        }
        AnsiConsole.out()
            .println(Ansi.ansi()
            .fg(Ansi.Color.YELLOW)
            .a("[WARNING] "+message)
            .reset());
    }

    /**
     * Affiches les informations neccessaire à l'utilisateurs afin de connaître
     * l'état actuel du dossier courant.
     * @param path Le path à afficher dans le terminal.
     */
    private void displayInterface(final String path) {
        Directory currentDirectory = new Directory(path, 0, "");
        String currentString = currentNer != -1 ? "[" + currentNer + "] "+ currentAnnotation : "";

        AnsiConsole.out()
            .println(Ansi.ansi()
            .fg(Ansi.Color.GREEN)
            .a(path + " ")
            .reset());

        AnsiConsole.out()
            .println(Ansi.ansi()
            .fg(Ansi.Color.BLUE)
            .a("Fichier courant : " + currentString)
            .reset());

        currentDirectory.displayElementsRepertory(currentRepertoryElements);
    }

    /**
     * Génère l'instance Notes ainsi que son fichier Notes.json  dans le
     * dossier courant, si il existe déjà, vérifie la validité du fichier.
     * @param path Le path du dossier parent.
     * @return L'instance Notes
     */
    public Notes generateNotesFile(final String path) {
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
    public void generateInstancesRepertory(final String path) {
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
                currentRepertoryElements.put(file.getName(),
                                new Directory(file.getName(), ner, newPath));
            } else if (file.isFile()) {
                currentRepertoryElements.put(file.getName(),
                                new FileRef(file.getName(), ner, newPath));
            } else {
                //exception
            }
            ner++;
        }
    }
    
    /** 
     * Obtiens l'instance d'un élément à partir de son Ner. 
     * @return L'élément associé au Ner de la commande.
     */
    public static ElementRepertory getElementByNer(int ner) {
        for (Map.Entry<String, ElementRepertory> entry : currentRepertoryElements.entrySet()) {
            ElementRepertory element = entry.getValue();
            if (element.getNer() == ner) {
                return element;
            }
        }
        return null;
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
    /** @return currentRepertoryElements */
    public Map<String, ElementRepertory> getCurrentRepertoryElements() {
        return currentRepertoryElements;
    }
    /** contains command.
     * @param commandName command name
     * @return commands */
    public boolean containsCommand(final String commandName) {
        return commands.containsKey(commandName);
    }
    /** Returns the current name.
     * @return the current name
    */
    public String getCurrentName() {
        return currentName;
    }
    /**
     * Returns the current arguments.
     * @return the current arguments
     */
    public String getCurrentArgs() {
        return currentArgs;
    }


    /**
     * Class permettant l'auto-complétion des mots, utilisant Jline.
    */
    static class MultiCompleter implements Completer {
        /**
         * fonction principale analysant l'entrée de l'utilisateur afin de redirgier vers l'auto-compléteur,
         * le plus pertinent.
         */
        @Override
        public void complete(LineReader reader, ParsedLine line, java.util.List<Candidate> candidates) {
            int wordCount = line.wordIndex() + 1;

            String[] parsedLine = line.line().split("\\s+");
            String tmpCommandName = "";
            int tmpCommandNer = -1;
            Boolean hasNer = false;

            if (wordCount > 0) {
                tmpCommandName = parsedLine[0];
                hasNer = isInteger(parsedLine[0]);
                tmpCommandNer = hasNer ? Integer.parseInt(parsedLine[0]) : -1;
            }

            if (wordCount == 1) {
                completeCommand(line, candidates, currentNer);
            } else if (wordCount == 2 && tmpCommandName.equals("cd")) {
                completeFolderName(line, candidates);
            } else if (wordCount == 2 && hasNer) {
                completeCommand(line, candidates, tmpCommandNer);
            }
        }

        /**
         * Auto-compléteur des commandes, n'affiche seulement les commandes possible pour l'utilisateur,
         * en fonction de ce qu'il a entrée et de l'élément courant
         */
        private void completeCommand(ParsedLine line, java.util.List<Candidate> candidates, int ner) {
            int wordCount = line.wordIndex() + 1;
            Boolean isDirectory = false;

            ElementRepertory element = getElementByNer(ner);
            if (element != null) {
                isDirectory = element.isDirectory();
            }

            if (command != null ? command.getCopy() != null : false) {
                candidates.add(new Candidate("past"));
            }
            if (ner == -1) {
                candidates.add(new Candidate("exit"));
                candidates.add(new Candidate("help"));
                candidates.add(new Candidate("cd"));
                candidates.add(new Candidate("ls"));
            }
            else {
                if (wordCount < 2) {
                    candidates.add(new Candidate("exit"));
                    candidates.add(new Candidate("help"));
                    candidates.add(new Candidate("cd"));
                    candidates.add(new Candidate("ls"));
                }
                if (!isDirectory) {
                    candidates.add(new Candidate("visu"));
                    candidates.add(new Candidate("copy"));
                }
                candidates.add(new Candidate("-"));
                candidates.add(new Candidate("+"));
                
            }
        }

        /**
         * Auto-compléteur des dossiers, permettant de se déplacer plus rapidement avec cd.
         */
        private void completeFolderName(ParsedLine line, java.util.List<Candidate> candidates) {
            for (Map.Entry<String, ElementRepertory> entry : currentRepertoryElements.entrySet()) {
                String fileName = entry.getKey();
                ElementRepertory element = entry.getValue();
            
                if (element.isDirectory()) {
                    candidates.add(new Candidate(fileName));
                }
            }
            candidates.add(new Candidate("../"));
        }
    }
    
}
