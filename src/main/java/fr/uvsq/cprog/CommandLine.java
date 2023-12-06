package fr.uvsq.cprog;
import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandLine {

    private final Map<String, Command> commands = new HashMap<>();

    /**
     * List of all commands.
     */
    public void initializeCommands() {
        addCommand(new CreateDirectoryCommand());
        addCommand(new CdCommand());
        addCommand(new ExitCommand());
        addCommand(new LsCommand());
    }

    private void addCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public void start() throws IOException {

        AnsiConsole.systemInstall();

        Command command = null;

        String currentPath = System.getProperty("user.dir");
        //String[] listFolders = currentPath.split("/");
        //String currentFolder = listFolders[listFolders.length - 1];

        long previousNer = -1;

        Terminal terminal = TerminalBuilder.builder().build();
        LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();

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
            String cmdPath = command != null ? command.path : currentPath ;

            command = commands.get(cmdName);
            
            if (command != null) {
                command.ner = cmdNer;
                command.name = cmdName;
                command.args = cmdArgs;
                command.path = cmdPath;
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
