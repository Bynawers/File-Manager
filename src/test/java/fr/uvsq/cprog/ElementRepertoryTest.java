package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ElementRepertoryTest {
    @Test
    public void testgetNameCopy() {
        // fichier avec extension
        ElementRepertory fileWithExtension = new FileRef("file.txt", 1, "/path/file.txt");
        assertEquals("file_copie.txt", fileWithExtension.getNameCopy());

        // fichier sans extension
        ElementRepertory fileWithoutExtension = new FileRef("file2", 2, "/path/file2");
        assertEquals("file2_copie", fileWithoutExtension.getNameCopy());

        // fichier avec plusieurs extensions 
        ElementRepertory fileError = new FileRef("file2.text.txt", 3, "file2.text.txt");
        assertEquals("file2.text.txt", fileError.getNameCopy());
    }

    @Test
    public void testparentPath() {
        ElementRepertory element = new FileRef("test", 0, "");
        //chemin basique
        String fullPath = "/path/to/folder/file.txt";
        assertEquals("/path/to/folder", element.parentPath(fullPath));

        // chemin es vide
        String emptyPath = "";
        assertEquals("", element.parentPath(emptyPath));
    }

    @Test
    public void testlastName() {
        ElementRepertory element = new FileRef("", 0, "");
        element.setName("file.txt");
        String filePath = "/path/to/folder/file.txt";
        element.setPath(filePath);

        assertEquals("file.txt", element.lastName(filePath));
        element.setNer();
        assertEquals(element.getNer(), 0);
        assertEquals(element.getName(), "file.txt");
        assertEquals(element.getPath(), filePath);
    }

}
