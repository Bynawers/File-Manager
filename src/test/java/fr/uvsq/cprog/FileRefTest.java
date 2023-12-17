package fr.uvsq.cprog;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

//import org.junit.Test;

public class FileRefTest {
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
            fail("Erreur avec le paths ou la fonction visualization");
        }
    }
}
