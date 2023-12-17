package fr.uvsq.cprog;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

//import org.junit.Test;

public class CommandLineTest {
    @Test
    public void testGenerateNotesFile() throws Exception {

        Path tempDir = Files.createTempDirectory("testDir"); // Creé un répertoire temporaire

        Path notesJson = tempDir.resolve("notes.json"); 
        Files.createFile(notesJson); // Creé un fichier notes.json

        CommandLine commandLine = new CommandLine();
        Notes notes = commandLine.generateNotesFile(tempDir.toString());
        
        assertNotNull(notes);

        Files.deleteIfExists(notesJson);
        Files.deleteIfExists(tempDir);
    }
    @Test
    public void testGenerateNotesFileNotFile() throws IOException {
        // test sans creer le fichier json
        Path tempDir = Files.createTempDirectory("testDir");

        CommandLine commandLine = new CommandLine();
        Notes notes = commandLine.generateNotesFile(tempDir.toString());

        Path notesFile = tempDir.resolve("notes.json");
        assertTrue(Files.exists(notesFile));

        Files.deleteIfExists(notesFile);
        Files.deleteIfExists(tempDir);
    }

    @Test
    public void testGenerateInstancesRepertory() throws IOException {
        Path tempDir = Files.createTempDirectory("testDir");
        Path pathFile = tempDir.resolve("testFile.txt");
        Files.createFile(pathFile);

        CommandLine commandLine = new CommandLine();
        commandLine.generateInstancesRepertory(null);
        commandLine.generateInstancesRepertory(tempDir.toString());

        assertTrue(commandLine.getCurrentRepertoryElements().containsKey("testFile.txt"));

        Files.deleteIfExists(tempDir.resolve("testFile.txt"));
        Files.deleteIfExists(tempDir);
    }

    @Test
    public void testIsInteger() throws IOException {
        assertTrue(CommandLine.isInteger("1"));
        assertFalse(CommandLine.isInteger("a"));
    }


    @Test
    public void testAddCommand() {
        CommandLine commandLine = new CommandLine();

        Command testCommand = new Command() {
            @Override
            public String getName() {
                return "test";
            }

            @Override
            public void execute() {
                System.out.println("test command");
            }
        };

        commandLine.addCommand(testCommand);
        assertTrue(commandLine.containsCommand("test"));
    }
    @Test
    public void testInitializeCommands() {
        CommandLine commandLine = new CommandLine();

        commandLine.initializeCommands();

        // on verifie que toutes les commandes sont ajoutées
        assertTrue(commandLine.containsCommand("mkdir"));
        assertTrue(commandLine.containsCommand("cd"));
        assertTrue(commandLine.containsCommand("exit"));
        assertTrue(commandLine.containsCommand("cut"));
        assertTrue(commandLine.containsCommand("ls"));
        assertTrue(commandLine.containsCommand("find"));
        assertTrue(commandLine.containsCommand("copy"));
        assertTrue(commandLine.containsCommand("past"));
        assertTrue(commandLine.containsCommand("visu"));
        assertTrue(commandLine.containsCommand("+"));
        assertTrue(commandLine.containsCommand("-"));
    }
}
