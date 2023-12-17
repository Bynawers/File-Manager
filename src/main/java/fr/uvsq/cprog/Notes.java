package fr.uvsq.cprog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Iterator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileReader;

public class Notes {
    private List<Note> notes = new ArrayList<>();
    private String path;

    public Notes(File[] fileList, String pathTmp) {
        this.notes = setNotes(fileList);
        path = pathTmp;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    private List<Note> setNotes(File[] fileList) {
        List<Note> notes = new ArrayList<>();
        if (fileList == null) {
            return notes;
        }

        for(File file: fileList) {
            notes.add(new Note(file.getName(), ""));
        }
        return notes;
    }

    public String getAnnotation(String name) {
        for (Note note : notes) {
            if (note.getName().equals(name)) {
                return note.getAnnotation();
            }
        }
        return "";
    }

    public void checkNotes(File[] fileList) {
        List<Note> notesNotFind = new ArrayList<>(notes);
        Iterator<Note> iterator = notesNotFind.iterator();
        Boolean find = false;

        for (File file: fileList) {
            find = false;
            while (iterator.hasNext()) {
                Note note = iterator.next();
                //System.out.println("note : "+note.getName()+" / filename : "+ file.getName());
                if (note.getName().equals(file.getName())) {
                    find = true;
                    iterator.remove();
                    break;
                }
            }
            if (find == false) {
                notes.add(new Note(file.getName(), ""));
            }
        }
        for (Note noteNoteFind : notesNotFind) {
            notes.remove(noteNoteFind);
        }
        writeFile();
    }

    public void readNote() {
        try (FileReader reader = new FileReader(path)) {

            Type listType = new TypeToken<List<Note>>() {}.getType();
            this.notes = new Gson().fromJson(reader, listType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAnnotation(String annotation, String name) {
        for (Note note: notes) {
            if (name.equals(note.getName())) {
                note.setAnnotation(note.getAnnotation() + annotation);
                writeFile();
                return;
            }
        }
    }

    public void deleteAnnotation(String name) {
        for (Note note: notes) {
            if (name.equals(note.getName())) {
                note.setAnnotation("");
                writeFile();
                return;
            }
        }
    }

    public void addNote(String fileName) {
        for (Note note: notes) {
            if (fileName.equals(note.getName())) {
                return;
            }
        }
        notes.add(new Note(fileName, ""));
        writeFile();
    }

    public void deleteNote(String fileName) {
        for (Note note: notes) {
            if (fileName.equals(note.getName())) {
                notes.remove(note);
                writeFile();
                return;
            }
        }
    }

    public void writeFile() {
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
            System.out.println("nom : " + note.getName() + " annotation : " + note.getAnnotation());
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
