package fr.uvsq.cprog;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

//import org.junit.Test;

public class DirectoryTest {
    @Test
    public void testCreateDirectory() {
        Directory directory = new Directory("test", 0, "");
        String path = System.getProperty("user.home") + "/Desktop/cpl_projet/miniprojet-grp-11_22/src/main/java/fr/uvsq/cprog"; // TODO on garde ca ou on change?
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
        String path = System.getProperty("user.home") + "/Desktop/cpl_projet/miniprojet-grp-11_22/src/main/java/fr/uvsq/cprog"; // TODO on garde ca ou on change?
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


    /* TODO getElementByNer dans Command plus dans directory
    @Test
    public void testGetElementByNer() {
        Map<String, ElementRepertory> map = new HashMap<>();
        Directory directory = new Directory("test", 0, "");

        assertNull(directory.getElementByNer(map, 1));

        ElementRepertory element1 = new FileRef( "file1.txt", 1, "");
        ElementRepertory element2 = new FileRef( "file2.txt", 2, "");
        map.put("file1.txt", element1);
        map.put("file2.txt", element2);

        assertEquals(element1, directory.getElementByNer(map, 1));
        assertEquals(element2, directory.getElementByNer(map, 2));
    }
    */
}
