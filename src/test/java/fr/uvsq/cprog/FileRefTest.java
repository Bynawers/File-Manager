package fr.uvsq.cprog;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileRefTest {
    @Test
    public void testVisualization() throws IOException {
        Path tempFile = Files.createTempFile("file", ".txt");
        File file = tempFile.toFile();
        file.createNewFile();
    
        try {
            // on creer un fichier . avec du contenu
            FileWriter writer = new FileWriter(file);
            writer.write("Contenu du fichier");
            writer.close();
    
            //sortie console
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));
    
            FileRef fileRef = new FileRef("", 0, "");
            fileRef.visualization(file.getAbsolutePath());
            
            // on verifie que la sortie console est egale à "Contenu du fichier\n"
            String expectedOutput = "Contenu du fichier\n" + System.lineSeparator();
            assertEquals(expectedOutput, outputStream.toString());
    
            file.delete();
        } catch (IOException e) {
            fail("Erreur avec le paths ou la fonction visualization");
        }
    }

    @Test
    public void testVisualizationPng() throws IOException {
        // Créer un fichier temporaire pour simuler un fichier PNG
        Path tempFile = Files.createTempFile("photo", ".png");
        File file = tempFile.toFile();
        file.createNewFile();

        // on change la taille du fichier
        Files.write(tempFile, new byte[1024]);

        // Sortie console
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        FileRef fileRef = new FileRef(file.getName(), 0, file.getAbsolutePath());
        fileRef.visualization(file.getAbsolutePath());

        String expectedOutputPng = "";
        assertEquals(expectedOutputPng, outputStream.toString().trim());

        System.setOut(System.out);
        Files.deleteIfExists(tempFile);
    }
}
