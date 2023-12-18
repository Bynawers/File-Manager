package fr.uvsq.cprog;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DirectoryTest {
    
    @Test
    public void testCreateDirectory() {
        Directory directory = new Directory("test", 0, "");
        String path = System.getProperty("user.dir") + "/src/main/java/fr/uvsq/cprog";
        String expectedPath = path + "/" + directory.getName();

        try {
            directory.createDirectory(path);

            assertTrue(Files.isDirectory(Paths.get(expectedPath))); // on verifie que le dossier a bien été  crée
            assertFalse(directory.isFile());
            assertEquals(directory.getName(), Paths.get(expectedPath).getFileName().toString()); // on verifie que le dossier a le bon nom

            Files.deleteIfExists(Paths.get(expectedPath)); // on supprime le dossier
        } catch (IOException e) {
            fail("Erreur lors de la creation du dossier");
        }
    }

    @Test
    public void testdelete() {
        Directory directory = new Directory("test", 0, "");
        String path = System.getProperty("user.dir") + "/src/main/java/fr/uvsq/cprog";
        String expectedPath = path + "/" + directory.getName();
        try {
            directory.createDirectory(path);

            assertTrue(Files.isDirectory(Paths.get(expectedPath))); // on verifie que le dossier a bien été  crée
            assertFalse(directory.isFile());
            assertEquals(directory.getName(), Paths.get(expectedPath).getFileName().toString()); // on verifie que le dossier a le bon nom
            directory.delete();
            //assertFalse(Files.exists(Paths.get(expectedPath))); // TODO
            Files.deleteIfExists(Paths.get(expectedPath)); // on supprime le dossier
        } catch (IOException e) {
            fail("Erreur lors de la creation du dossier");
        }
    }

    @Test
    public void testGetElementByNer() {
        Map<String, ElementRepertory> map = new HashMap<>();
        ExitCommand command = new ExitCommand();
        command.setNer(1);
        command.setCurrentRepertoryElements(map);

        assertNull(command.getElementByNer());

        ElementRepertory element1 = new FileRef( "file1.txt", 1, "");
        ElementRepertory element2 = new FileRef( "file2.txt", 2, "");
        map.put("file1.txt", element1);
        map.put("file2.txt", element2);
        command.setCurrentRepertoryElements(map);

        command.setNer(1);
        assertEquals(element1, command.getElementByNer());
        command.setNer(2);
        assertEquals(element2, command.getElementByNer());
    }
    
}
