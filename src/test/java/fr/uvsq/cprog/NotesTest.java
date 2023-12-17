package fr.uvsq.cprog;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NotesTest {
    
    @Test
    public void testSetPath() {
        List<Note> expectedNotes = new ArrayList<>();
        
        expectedNotes.add(new Note("file1", "Annotation 1"));
        expectedNotes.add(new Note("file2", "Annotation 2"));
        Notes notes = new Notes(expectedNotes , "");
        String addAnnotation = " suite annotation";
        String expectedAnnotation = "Annotation 1"+" suite annotation";
        
        notes.setPath("/folder/file.txt");
        assertEquals("/folder/file.txt", notes.getPath());

        notes.addAnnotation(addAnnotation, "file1");
        assertEquals(expectedAnnotation, expectedNotes.get(0).getAnnotation());
        
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
        List<Note> expectedNotes = new ArrayList<>();
        Notes notes = new Notes(expectedNotes, "path");
        List<Note> result3 = notes.setNotes(filetab);

        assertEquals("file1.txt", result3.get(0).getName());
        assertEquals("file3.txt", result3.get(2).getName());
        assertEquals("", result3.get(2).getAnnotation());
    }
    @Test
    public void testCreateFile() throws IOException {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note("Note 1", "Annotation 1"));
        notes.add(new Note("Note 2", "Annotation 2"));
        String path = "test.json";
        Notes testNotes = new Notes(notes, path);
        testNotes.createFile();
        File file = new File(path);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            String actualJson = content.toString().replaceAll("\\s", "");

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String expectedJson = gson.toJson(notes).replaceAll("\\s", "");

            assertEquals(expectedJson, actualJson);
        } finally {
            file.delete();
        }
    }

    @Test
    public void testReadNote() throws IOException {
        String jsonContent = "[{\"name\":\"file1.txt\",\"annotation\":\"Annotation1\"}," +
                                "{\"name\":\"file2.txt\",\"annotation\":\"Annotation2\"}]";

        Path jsonFile = Files.createTempFile("notes", ".json");
        Files.write(jsonFile, jsonContent.getBytes());

        List<Note> emptyNotesList = new ArrayList<>();
        Notes notes = new Notes(emptyNotesList, jsonFile.toString());

        notes.readNote();

        List<Note> loadedNotes = notes.getNotes();
        assertEquals(2, loadedNotes.size());
        assertEquals("file1.txt", loadedNotes.get(0).getName());
        assertEquals("Annotation2", loadedNotes.get(1).getAnnotation());

        Files.deleteIfExists(jsonFile);
    }
    

    @Test
    public void testDeleteAnnotation() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note("file 1", "Annotation 1"));
        notes.add(new Note("file 2", "Annotation 2"));
        Notes testNotes = new Notes(notes,"");

        assertEquals("Annotation 1", notes.get(0).getAnnotation());
        testNotes.deleteAnnotation("file 1");
        assertEquals("", notes.get(0).getAnnotation());
        assertEquals("Annotation 2", notes.get(1).getAnnotation());

    
    }

    @Test
    public void testDisplayNotes() {
        List<Note> notes = new ArrayList<>();

        notes.add(new Note("file 1", "Annotation 1"));
        notes.add(new Note("file 2", "Annotation 2"));
        Notes testNotes = new Notes(notes,"");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        testNotes.displayNotes();

        String expectedOutput = "nom : file 1 annotation :\nnom : file 2 annotation :\n";
        assertEquals(expectedOutput, outputStream.toString());
        System.setOut(System.out);

    }
}
