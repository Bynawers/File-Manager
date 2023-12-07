package fr.uvsq.cprog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class Notes {
    private List<Note> notes = new ArrayList<>();

    public Notes(File[] fileList) {
        this.notes = setNotes(fileList);
    }

    private List<Note> setNotes(File[] fileList) {
        List<Note> notes = new ArrayList<>();
        int ner = 0;

        for(File file: fileList) {
            notes.add(new Note(file.getName(), ner, ""));
            ner = ner + 1;
        }
        return notes;
    }

    public void createFile(String path) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(notes);

        try (FileWriter fileWriter = new FileWriter(path + "/notes.json")) {
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Note {
    private String name;
    private int ner;
    private String annotation;

    public Note(String nameTmp, int nerTmp, String annotationTmp) {
        this.name = nameTmp;
        this.ner = nerTmp;
        this.annotation = annotationTmp;
    }

    public String getAnnotation() {
        return annotation;
    }
    public String getName() {
        return name;
    }
    public int getNer() {
        return ner;
    }
    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setNer(int ner) {
        this.ner = ner;
    }
}