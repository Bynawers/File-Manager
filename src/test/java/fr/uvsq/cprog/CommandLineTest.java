package fr.uvsq.cprog;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CommandLineTest {
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
    @Test
    public void testmodifyNotes() throws IOException {
        // test apres avoir fait mkdir et cut
        File tempDir = Files.createTempDirectory("testDir").toFile();
        File tempFile = new File(tempDir, "testFile.txt");
        tempFile.createNewFile();

        CommandLine commandLine = new CommandLine();
        Notes currentNotes = new Notes(new File[]{tempFile}, tempDir.getAbsolutePath());

        currentNotes.addNote(tempFile.getName());
        // avec mkdir
        CreateDirectoryCommand mkdirCommand = new CreateDirectoryCommand();
        mkdirCommand.setArgs("newDirectory");
        currentNotes = commandLine.modifyNotes(currentNotes, mkdirCommand);
        boolean foundNewDirectory = false;
        for (Note note : currentNotes.getNotes()) {
            if ("newDirectory".equals(note.getName())) {
                foundNewDirectory = true;
                break;
            }
        }
        assertTrue(foundNewDirectory);
        
        // avec cut
        CutCommand cutCommand = new CutCommand();
        cutCommand.setCopy(new FileRef("testFile.txt", 0, tempFile.getAbsolutePath()));
        currentNotes = commandLine.modifyNotes(currentNotes, cutCommand);
        boolean cutDir = false;
        for (Note note : currentNotes.getNotes()) {
            if ("newDirectory".equals(note.getName())) {
                cutDir = true;
                break;
            }
        }
        assertTrue(cutDir);
        tempFile.delete();
        tempDir.delete();
    }
    @Test
    public void testModifyNotesWithCdAndPastCommands() throws IOException {
        // test apres avoir fait cd et past
        File tempDir = Files.createTempDirectory("testDir").toFile();
        Notes currentNotes = new Notes(tempDir.listFiles(), tempDir.getAbsolutePath());
    
        // pour cd
        CommandLine commandLine = new CommandLine();
        Command cdCommand = new CdCommand();
        cdCommand.setPath(tempDir.getAbsolutePath());
        currentNotes = commandLine.modifyNotes(currentNotes, cdCommand);
    
        assertNotNull(currentNotes); //on verfie que le le fichier a bien été généré
    
        // pour past
        Command pastCommand = new PastCommand();
        ElementRepertory copiedElement = new FileRef("newFile.txt", 0, tempDir.getAbsolutePath());
        pastCommand.setCopy(copiedElement);
        currentNotes = commandLine.modifyNotes(currentNotes, pastCommand);
    
        assertNotNull(currentNotes);
        
        boolean noteFound = false;
        for (Note note : currentNotes.getNotes()) {
            if ("newFile.txt".equals(note.getName())) {
                noteFound = true;
                break;
            }
        }
        assertFalse(noteFound);
        tempDir.delete();
    }

    @Test
    public void testparseUser() throws IOException {
        CommandLine commandLine = new CommandLine();
        // avec mkdir
        String[] input1 = {"mkdir", "newDir"};
        commandLine.parseUser(input1);
        assertEquals("mkdir", commandLine.getCurrentName());
        assertEquals("newDir", commandLine.getCurrentArgs());

        // avec ls
        String[] input2 = {"ls"};
        commandLine.parseUser(input2);
        assertEquals("ls", commandLine.getCurrentName());
        assertNull(commandLine.getCurrentArgs());

        // avec cd
        String[] input3 = {"cd", "difFolder"};
        commandLine.parseUser(input3);
        assertEquals("cd", commandLine.getCurrentName());
        assertEquals("difFolder", commandLine.getCurrentArgs());
    }

    @Test
    public void testStart() {
        
    }
    
}
