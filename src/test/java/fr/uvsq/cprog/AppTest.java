package fr.uvsq.cprog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;

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
        @Test
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
    }
} 


