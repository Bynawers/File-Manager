package fr.uvsq.cprog;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class NotesTest {
 
    @Test
    public void testSetPath() {
        Notes notes = new Notes(new File[]{}, "");
    
        String expectedPath = "/Dir/file.txt";
        notes.setPath(expectedPath);
    
        assertEquals(expectedPath, notes.getPath());
    }

    @Test
    public void testGetPath() {
        Note note = new Note("", "");
        note.setName("file.txt");
        note.setAnnotation("test");

        String resultName = note.getName();
        String resultAnnotation = note.getAnnotation();
        
        assertEquals("file.txt", resultName);
        assertEquals("test", resultAnnotation);
    }

    @Test
    public void testConstructor() {
        File[] filetab = new File[]{new File("file1.txt"), new File("file2.txt"), new File("file3.txt")};
        Notes notes = new Notes(filetab, "");
        List<Note> res = notes.getNotes();
        assertEquals(3, res.size());
        assertEquals("", notes.getPath());
    }

    @Test
    public void testSetNotes() {

        File[] filetab = new File[]{new File("file1.txt"), new File("file2.txt"), new File("file3.txt")};
        Notes notes = new Notes(new File[]{}, "path");

        List<Note> result = notes.setNotes(filetab);

        assertEquals(3, result.size());
        assertEquals("file1.txt", result.get(0).getName());
    }

    @Test
    public void testReadNote() throws IOException {
        String jsonContent = "[{\"name\":\"file1.txt\",\"annotation\":\"Annotation1\"}," +
                                "{\"name\":\"file2.txt\",\"annotation\":\"Annotation2\"}]";

        Path jsonFile = Files.createTempFile("notes", ".json");
        Files.write(jsonFile, jsonContent.getBytes());

        Notes notes = new Notes(new File[]{}, jsonFile.toString());

        notes.readNote();

        List<Note> loadedNotes = notes.getNotes();
        assertEquals(2, loadedNotes.size());
        assertEquals("file1.txt", loadedNotes.get(0).getName());
        assertEquals("Annotation2", loadedNotes.get(1).getAnnotation());

        Files.deleteIfExists(jsonFile);
    } 

    @Test
    public void testDeleteAnnotation() {
        File[] filetab = new File[] {new File("file1.txt"), new File("file2.txt"), new File("file3.txt")};
        Notes testNotes = new Notes(filetab, "notes.json");

        testNotes.addAnnotation("Anno1", "file1.txt");
        testNotes.addAnnotation("Anno2", "file2.txt");

        assertEquals("Anno1", testNotes.getAnnotation("file1.txt"));
        testNotes.deleteAnnotation("file1.txt");
        assertEquals("", testNotes.getAnnotation("file1.txt"));
        assertEquals("Anno2", testNotes.getAnnotation("file2.txt"));
    }

    @Test
    public void testDisplayNotes() {
        File[] filetab = new File[]{new File("file1.txt"), new File("file2.txt")};
        Notes testNotes = new Notes(filetab, "notes.json");

        testNotes.addAnnotation("Anno1", "file1.txt");
        testNotes.addAnnotation("Anno2", "file2.txt");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        testNotes.displayNotes();

        String expectedOutput = "nom : file1.txt annotation : Anno1\nnom : file2.txt annotation : Anno2\n";
        assertEquals(expectedOutput, outputStream.toString());
        System.setOut(System.out);
    }

    @Test
    public void testAddNote() {
        File[] filetab = new File[]{};
        Notes notes = new Notes(filetab, "notes.json");
        String FileName = "file1.txt";
        String newFileName = "file2.txt";
        notes.addNote(FileName);
        notes.addNote(newFileName);
        // Vérifier que la note a été ajoutée
        boolean noteFound = false;
        for (Note note : notes.getNotes()) {
            if (note.getName().equals(FileName) && note.getAnnotation().isEmpty()) {
                noteFound = true;
                break;
            }
        }
        assertTrue(noteFound);
    }

    @Test
    public void testdeleteNote() {
        File[] filetab = new File[]{};
        Notes notes = new Notes(filetab, "notes.json");

        notes.addNote("file1.txt");
        notes.addNote("file2.txt");

        // on supprime le fichier "file2.txt"
        notes.deleteNote("file2.txt");

        // Vérifier que le fichier "file2.txt" n'existe plus
        List<Note> currentNotes = notes.getNotes();
        boolean noteFound = false;
        for (Note note : currentNotes) {
            if (note.getName().equals("file2.txt")) {
                noteFound = true;
                break;
            }
        }
        assertFalse(noteFound);
    }

    @Test
    public void testCheckNotes() throws IOException {
        Path tempDir = Files.createTempDirectory("testDir");
        File tempFile1 = new File(tempDir.toFile(), "file1.txt");
        File tempFile2 = new File(tempDir.toFile(), "file2.txt");
        File tempFile3 = new File(tempDir.toFile(), "file3.txt");

        tempFile1.createNewFile();
        tempFile2.createNewFile();

        File[] filetab = new File[]{tempFile1, tempFile2};
        Notes notes = new Notes(filetab, "notes.json");

        tempFile2.delete();

        tempFile3.createNewFile();


        File[] updatedFiles = new File[]{tempFile1, tempFile3};
        notes.checkNotes(updatedFiles);


        boolean foundFile1 = false;
        for (Note note : notes.getNotes()) {
            if ("file1.txt".equals(note.getName())) {
                foundFile1 = true;
                break;
            }
        }
        assertTrue(foundFile1);

        boolean foundFile2 = false;
        for (Note note : notes.getNotes()) {
            if ("file2.txt".equals(note.getName())) {
                foundFile2 = true;
                break;
            }
        }
        assertFalse(foundFile2);
        
        boolean foundFile3 = false;
        for (Note note : notes.getNotes()) {
            if ("file3.txt".equals(note.getName())) {
                foundFile3 = true;
                break;
            }
        }
        assertTrue(foundFile3);

        tempFile1.delete();
        tempFile3.delete();
        Files.delete(tempDir);
    }
}
