package fr.uvsq.cprog;

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

    /**
     * List of all commands.
     */
    public void initializeCommands() {
        addCommand(new CreateDirectoryCommand());
        addCommand(new CdCommand());
        addCommand(new ExitCommand());
        addCommand(new LsCommand());
        addCommand(new FindCommand());
        addCommand(new PastCommand());
        addCommand(new VisuCommand());
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

        long previousNer = -1;

        Terminal terminal = TerminalBuilder.builder().build();
        LineReader lineReader = LineReaderBuilder
            .builder().terminal(terminal).build();

        generateNotesFile(currentPath);

        while (true) {
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
            String cmdPath = command != null ? command.getPath() : currentPath;

            command = commands.get(cmdName);

            if (command != null) {
                command.ner = cmdNer;
                command.name = cmdName;
                command.args = cmdArgs;
                if (cmdPath != null) { 
                    command.setPath(cmdPath);
                    generateNotesFile(cmdPath);
                }
                command.execute();
            } else {
                System.out.println(cmdName + " command not found");
            }
        }
    }

    public void generateNotesFile(String path) {
        File directory = new File(path);
        File[] directoryChildrens = directory.listFiles();

        for (File file : directoryChildrens) {
            if (file.getName() == "notes.json") {
                return;
            }
        }
        
        System.out.println("create file notes at "+ path +"...");
        Notes notes = new Notes(directoryChildrens);
        notes.createFile(path);
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
