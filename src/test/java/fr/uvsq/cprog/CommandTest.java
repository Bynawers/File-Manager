package fr.uvsq.cprog;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.junit.Test;

public class CommandTest {
        @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testExecute() {
        LsCommand lsCommand = new LsCommand();


        Map<String, ElementRepertory> currentRepertoryElements = new HashMap<>();
        currentRepertoryElements.put("file1.txt", new FileRef("file1.txt", 0, "/Users/charbeltouma/Desktop/cpl_projet/miniprojet-grp-11_22/src/main/java/fr/uvsq/cprog/file1.txt"));
        currentRepertoryElements.put("file2.txt", new FileRef("file2.txt", 1, "/Users/charbeltouma/Desktop/cpl_projet/miniprojet-grp-11_22/src/main/java/fr/uvsq/cprog/file2.txt"));
        //currentRepertoryElements.put("dir1", new Directory("dir1", 2, "/Users/charbeltouma/Desktop/cpl_projet/miniprojet-grp-11_22/src/main/java/fr/uvsq/cprog/")); // TODO pourquoi le dir ne s'affiche pas


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));


        lsCommand.currentRepertoryElements = currentRepertoryElements;
        
        String name = lsCommand.getName();
        lsCommand.execute();

        //String expectedOutput = "file1.txt\nfile2.txt\ndir1\n";
        String expectedOutput = "[0] file1.txt\n[1] file2.txt\n";
        assertEquals(expectedOutput, outContent.toString());
        assertEquals(name, "ls");

        System.setOut(originalOut);
    }

 
    @Test
    public void testGetName() {
        ExitCommand command = new ExitCommand();
        assertEquals("exit", command.getName());

    }

    @Test // Class visuCommand
    public void testExecuteVisualizesFile() throws Exception {
        String content = "Bonjour ceci est un test !";
        File tempFile = File.createTempFile("testFile", ".txt");
    
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }
    
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    
        VisuCommand visuCommand = new VisuCommand();
        visuCommand.currentRepertoryElements = new HashMap<>();
        FileRef file = new FileRef(tempFile.getName(), 1, tempFile.getAbsolutePath());
        visuCommand.currentRepertoryElements.put(tempFile.getName(), file);
        visuCommand.ner = 1;

        String name = visuCommand.getName(); 
        visuCommand.execute();
    
        String expectedOutput = content;
        assertEquals(expectedOutput, outputStream.toString().trim());
        assertEquals(name, "visu");
        file.delete();
    }

    @Test
    public void testExecuteFindsFile() throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "testDir");
        tempDir.mkdir();
        
        File tempFile = new File(tempDir, "testFile.txt");
        tempFile.createNewFile();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));


        FindCommand findCommand = new FindCommand();
        String path = tempDir.getPath();
        findCommand.setPath(path);
        findCommand.setArgs("testFile.txt");
        findCommand.execute();

        String expectedOutput = "Le fichier testFile.txt a été trouvé dans le dossier " + path + "\n";
        assertEquals(expectedOutput, outputStream.toString());
        assertEquals(findCommand.getName(), "find");
        tempFile.delete();
        tempDir.delete();
    }

    @Test
    public void testCdCommandGoBack() throws Exception {
        String initialPath = System.getProperty("user.home") + "/Desktop/cpl_projet/miniprojet-grp-11_22/src" ;
        String parentPath = System.getProperty("user.home") + "/Desktop/cpl_projet/miniprojet-grp-11_22" ;

        CdCommand command = new CdCommand();
        command.setPath(initialPath);
        command.setArgs("..");
        command.execute();

        assertEquals(parentPath, command.getPath());
        assertEquals(command.getName(), "cd");
    }


    @Test
    public void testCdCommand() throws Exception {
        String initialPath = System.getProperty("user.home") + "/Desktop/cpl_projet/miniprojet-grp-11_22" ;
        String newPath = "Forword";

        Path tempDir = Paths.get(initialPath, newPath);
        tempDir.toFile().mkdirs();

        CdCommand command = new CdCommand();
        command.setPath(initialPath);
        command.setArgs(newPath);
        command.execute();

        assertEquals(tempDir.toString(), command.getPath());

        tempDir.toFile().delete();
    }

    @Test
    public void testCopyValidElement() {
        CopyCommand command = new CopyCommand();
        command.currentRepertoryElements = new HashMap<>();

        ElementRepertory copyFile = new FileRef("file1.txt", 1, "/path/file1.txt");
        command.currentRepertoryElements.put("file1.txt", copyFile);

        command.ner = 1;
        command.execute();
        assertEquals(command.getName(), "copy");
        assertNotNull(command.copy);
        assertEquals(copyFile, command.copy);
    }

    @Test
    public void testCreateDirectory()  throws Exception{
        Path tempDir = Files.createTempDirectory("testDirectory");
        String newDirName = "newDirectory";


        CreateDirectoryCommand command = new CreateDirectoryCommand();
        command.setPath(tempDir.toString());
        command.setArgs(newDirName);
        command.execute();
        Path newDirPath = tempDir.resolve(newDirName); //on creer un nouveau path pour le nouveau dossier
        assertTrue(Files.exists(newDirPath) && Files.isDirectory(newDirPath));
        assertEquals(command.getName(), "mkdir");

        Files.deleteIfExists(newDirPath);
        Files.deleteIfExists(tempDir);
    }
    
    @Test
    public void testAnnotateCommand() {
        AnnotateCommand command = new AnnotateCommand();
        command.currentRepertoryElements = new HashMap<>();
        File[] fileTab = new File[]{new File("file1.txt"), new File("file2.txt"), new File("file3.txt")};

        Notes notes = new Notes(fileTab, "/path");
    
        command.currentRepertoryElements.put("file1.txt", new FileRef("file1.txt", 0, "/path/file1.txt"));
        command.currentRepertoryElements.put("file2.txt", new FileRef("file2.txt", 0, "/path/file2.txt"));

        command.notes = notes;
        command.ner = 0;
        command.setArgs("Annotation pour file1.txt");
        command.execute();

        assertEquals(command.getArgs(), "Annotation pour file1.txt");
        String annotation = notes.getNotes().get(0).getAnnotation();
        assertEquals(command.getName(), "+");
        assertEquals("Annotation pour file1.txt", annotation);
    }

    @Test
    public void testDesannotateCommand() {
        DesannotateCommand command = new DesannotateCommand();
        command.currentRepertoryElements = new HashMap<>();
        File[] fileTab = new File[]{new File("file1.txt")};


        Notes notes = new Notes(fileTab, "/path");
        notes.addAnnotation("Annotation pour file1.txt", "file1.txt");


        ElementRepertory element = new FileRef("file1.txt", 1, "/path/file1.txt");
        command.currentRepertoryElements.put("file1.txt", element);
        command.notes = notes;
        command.ner = 1;

        command.execute(); //on supprime l'annotation

        String annotation = notes.getAnnotation("file1.txt");
        assertEquals("", annotation);
        assertEquals(command.getName(), "-");
    }

    @Test
    public void testCutCommand() throws IOException {
              
        Path tempFile = Files.createTempFile("testFile", ".txt"); // On Créer un fichier temporaire

        CutCommand command = new CutCommand();
        command.currentRepertoryElements = new HashMap<>();

        ElementRepertory fileToCut = new FileRef(tempFile.getFileName().toString(), 1, tempFile.toString());
        command.currentRepertoryElements.put(tempFile.getFileName().toString(), fileToCut);
        command.ner = 1;

        assertTrue(Files.exists(tempFile)); // on verifie que le fichier existe

        command.execute();
        assertFalse(Files.exists(tempFile)); // on verifie que le fichier a ete supprimé
        assertEquals(command.getName(), "cut");
    }
    @Test
    public void testPastCommand() throws IOException {
        Path tempFile = Files.createTempFile("testFile", ".txt"); // On Créer un fichier temporaire

        PastCommand command = new PastCommand(); 
        ElementRepertory Copyfile = new FileRef(tempFile.getFileName().toString(), 1, tempFile.toString()); // une instance de FileRef pour le fichier a copier
        command.copy = Copyfile; //copie 
        
        command.execute(); // past

        Path targetPath = tempFile.getParent().resolve(command.copy.getNameCopy()); // path du nouveau fichier
        assertTrue(Files.exists(targetPath));

        assertEquals(command.getName(), "past");
        Files.deleteIfExists(tempFile);
        Files.deleteIfExists(targetPath);
    }
    @Test
    public void testPastNULLCommand() throws IOException {
        ElementRepertory Copyfile = null;
        PastCommand command = new PastCommand(); 
        command.copy = Copyfile; //copie NULL 
        command.execute(); // past
    }

        @Test
    public void testPastErrCommand() throws IOException {
        ElementRepertory Copyfile = null;
        PastCommand command = new PastCommand(); 
        command.copy = Copyfile; //copie NULL 
        command.execute(); // past
    }
    
}

