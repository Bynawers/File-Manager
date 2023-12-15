package fr.uvsq.cprog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileReader;

public class Notes {
    private List<Note> notes = new ArrayList<>();
    private String path;

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public Notes(File[] fileList, String pathTmp) {
        this.notes = setNotes(fileList);
        path = pathTmp;
    }

    private List<Note> setNotes(File[] fileList) {
        List<Note> notes = new ArrayList<>();
        int ner = 0;

        for(File file: fileList) {
            notes.add(new Note(file.getName(), ""));
            ner = ner + 1;
        }
        return notes;
    }

    public void readNote() {
        try (FileReader reader = new FileReader(path)) {

            Type listType = new TypeToken<List<Note>>() {}.getType();
            this.notes = new Gson().fromJson(reader, listType);
            displayNotes();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAnnotation(String annotation, String name) {
        for (Note note: notes) {
            if (name.equals(note.getName())) {
                note.setAnnotation(note.getAnnotation() + annotation);
                createFile();
                return;
            }
        }
    }

    public void deleteAnnotation(String name) {
        for (Note note: notes) {
            if (name.equals(note.getName())) {
                note.setAnnotation("");
                createFile();
                return;
            }
        }
    }

    public void createFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(notes);

        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayNotes() {
        if (notes == null) {
            return;
        }
        for (Note note: notes) {
            System.out.println("nom : " + note.getName() + " annotation :");
        }
    }
}

class Note {
    private String name;
    private String annotation;

    public Note(String nameTmp, String annotationTmp) {
        this.name = nameTmp;
        this.annotation = annotationTmp;
    }

    public String getAnnotation() {
        return annotation;
    }
    public String getName() {
        return name;
    }
    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
    public void setName(String name) {
        this.name = name;
    }
}
