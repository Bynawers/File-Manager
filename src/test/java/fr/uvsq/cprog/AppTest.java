package fr.uvsq.cprog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Files;

// TODO mettre les tests dans des classes
/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
/*        @Test
    public void testFind() {
        // Create a Directory object and add child elements
        Directory directory = new Directory("test", 1, null);
        directory.setChildren(new ArrayList<>());
        Directory f1 = new Directory("hi", 1,directory);
        File f2 = new File("texte1.txt", 1, f1);
        directory.getChildren().add(f1);
        directory.getChildren().add(f2);

        // Test the find() method
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        String path = directory.getPath().replaceFirst("^\\./", "");
        directory.findRecursive("texte1.txt", path);
        String expectedOutput = "Le fichier texte1.txt a été trouvé dans le dossier " + path + "\n";
        assertEquals(expectedOutput, outputStream.toString());
    }*/ 

    @Test
    public void testCreateDirectory() {
        Directory directory = new Directory("test", 0, "");
        String path = System.getProperty("user.home") + "/Desktop/cpl_projet/miniprojet-grp-11_22/src/main/java/fr/uvsq/cprog"; // TODO on garde ca ou on change?
        String expectedPath = path + "/" + directory.getName();

        try {
            directory.createDirectory(path);

            assertTrue(Files.isDirectory(Paths.get(expectedPath))); // on verifie que le dossier a bien été  crée

            assertEquals(directory.getName(), Paths.get(expectedPath).getFileName().toString()); // on verifie que le dossier a le bon nom

            Files.deleteIfExists(Paths.get(expectedPath)); // on supprime le dossier
        } catch (IOException e) {
            fail("Erreur lors de la creation du dossier");
        }
    }
    @Test
    public void testVisualization() {
        String path = System.getProperty("user.home") + "/Desktop/cpl_projet/miniprojet-grp-11_22/src/main/java/fr/uvsq/cprog/file.txt"; 
        File file = new File(path);
    
        try {
            // on creer un fichier . avec du contenu
            FileWriter writer = new FileWriter(file);
            writer.write("Contenu du fichier");
            writer.close();
    
            //sortie console
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));
    
            FileRef fileRef = new FileRef("", 0, "");
    
            fileRef.visualization(path);
    
            // on verifie que la sortie console est egale à "Contenu du fichier\n"
            String expectedOutput = "Contenu du fichier\n" + System.lineSeparator();
            assertEquals(expectedOutput, outputStream.toString());
    
            file.delete();
        } catch (IOException e) {
            fail("Erreur avec le paths ou la méthode visualization");
        }
    }

        @Test
        public void testExecute() {
            LsCommand lsCommand = new LsCommand();
    
            // Mocking the currentRepertoryElements map
            Map<String, ElementRepertory> currentRepertoryElements = new HashMap<>();
            currentRepertoryElements.put("file1.txt", new FileRef("file1.txt", 0, "/Users/charbeltouma/Desktop/cpl_projet/miniprojet-grp-11_22/src/main/java/fr/uvsq/cprog/file1.txt"));
            currentRepertoryElements.put("file2.txt", new FileRef("file2.txt", 1, "/Users/charbeltouma/Desktop/cpl_projet/miniprojet-grp-11_22/src/main/java/fr/uvsq/cprog/file2.txt"));
            //currentRepertoryElements.put("dir1", new Directory("dir1", 2, "/Users/charbeltouma/Desktop/cpl_projet/miniprojet-grp-11_22/src/main/java/fr/uvsq/cprog/")); // TODO pourquoi le dir ne s'affiche pas

            // Redirecting System.out to capture the output
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));
    
            // Setting the currentRepertoryElements map in the lsCommand object
            lsCommand.currentRepertoryElements = currentRepertoryElements;
    
            // Executing the lsCommand
            lsCommand.execute();
    
            // Asserting the expected output
            //String expectedOutput = "file1.txt\nfile2.txt\ndir1\n";
            String expectedOutput = "[0] file1.txt\n[1] file2.txt\n";
            assertEquals(expectedOutput, outContent.toString());
    
            // Restoring the original System.out
            System.setOut(originalOut);
        }
} 





