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

/**
 * La classe Notes représente l'ensemble des instances "Note" du dossier courant,
 * et permet d'écrire/supprimer/modifier de nouvelle Note dans un fichier Notes.json.
 */
public class Notes {
    /**
     * Liste de Note du fichier courant.
     */
    private List<Note> notes = new ArrayList<>();
    /**
     * Path du fichier Notes.json.
     */
    private String path;

    /**
     * Contructeur de la classe Notes.
     * @param fileList Une liste de fichier/dossier.
     * @param pathTmp Chemin d'accès du fichier Notes.json.
     */
    public Notes(File[] fileList, String pathTmp) {
        this.notes = setNotes(fileList);
        path = pathTmp;
    }

    /**
     * Obtiens le path de l'instance.
     * @return chaine de charatère du path.
     */
    public String getPath() {
        return path;
    }
    /**
     * Modifie le path de l'instance.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Crée une Liste de Note à partir d'un tableau de fichier/dossier.
     * @return une liste de notes.
     */
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

    /**
     * Obtiens toutes la liste de notes du dossier courant.
     * @return une liste de notes.
     */
    public List<Note> getNotes() {
        return notes;
    }

    /**
     * Obtiens l'annotation d'un fichier/dossier,
     * à partir de son nom.
     * @param name Une liste de fichier/dossier.
     * @return chaîne de charactère de l'annotation.
     */
    public String getAnnotation(String name) {
        for (Note note : notes) {
            if (note.getName().equals(name)) {
                return note.getAnnotation();
            }
        }
        return "";
    }

    /**
     * Vérifie l'état du fichier notes.json pour s'assurer qu'il
     * correspond bien au fichier du dossier courant.
     * @param fileList Une liste de fichier/dossier.
     */
    public void checkNotes(File[] fileList) {
        List<Note> notesNotFind = new ArrayList<>(notes);
        Iterator<Note> iterator = notesNotFind.iterator();
        Boolean find = false;

        for (File file: fileList) {
            find = false;
            while (iterator.hasNext()) {
                Note note = iterator.next();
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

    /**
     * Lit le fichier notes.json du path afin de récupérer toutes
     * ses données et les stocker dans l'instance Notes.
     */
    public void readNote() {
        try (FileReader reader = new FileReader(path)) {

            Type listType = new TypeToken<List<Note>>() {}.getType();
            this.notes = new Gson().fromJson(reader, listType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ajout d'une annotation à un fichier/dossier donné.
     * @param annotation annotation du fichier.
     * @param name nom du fichier/dossier à annoter.
     */
    public void addAnnotation(String annotation, String name) {
        for (Note note: notes) {
            if (name.equals(note.getName())) {
                note.setAnnotation(note.getAnnotation() + annotation);
                writeFile();
                return;
            }
        }
    }

    /**
     * Surpprime l'annotation d'un fichier/dossier.
     * @param name nom du fichier/dossier à désannoter.
     */
    public void deleteAnnotation(String name) {
        for (Note note: notes) {
            if (name.equals(note.getName())) {
                note.setAnnotation("");
                writeFile();
                return;
            }
        }
    }

    /**
     * Ajoute une note d'un fichier/dossier aux fichier notes.json.
     * @param fileName nom du fichier/dossier à ajouter.
     */
    public void addNote(String fileName) {
        for (Note note: notes) {
            if (fileName.equals(note.getName())) {
                return;
            }
        }
        notes.add(new Note(fileName, ""));
        writeFile();
    }

    /**
     * Supprime une note d'un fichier/dossier aux fichier notes.json.
     * @param fileName nom du fichier/dossier à supprimer.
     */
    public void deleteNote(String fileName) {
        for (Note note: notes) {
            if (fileName.equals(note.getName())) {
                notes.remove(note);
                writeFile();
                return;
            }
        }
    }

    /**
     * Ecrit dans le fichier notes.json le contenue de la liste notes.
     */
    public void writeFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(notes);

        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche l'ensemble du nom et annotation des fichier/dossier de l'instance.
     */
    public void displayNotes() {
        if (notes == null) {
            return;
        }
        for (Note note: notes) {
            System.out.println("nom : " + note.getName() + " annotation : " + note.getAnnotation());
        }
    }
}

/**
 * La classe Note représente un fichier/dossier à partir de son nom,
 * et stock l'annotation de l'utilisateur.
 */
class Note {
    /**
     * Nom de fichier/dossier.
     */
    private String name;
    /**
     * Annotation du fichier/dossier.
     */
    private String annotation;

    /**
     * Contructeur de la classe Note.
     * @param nameTmp Nom du fichier/dossier.
     * @param annotationTmp Annotation du fichier/dossier.
     */
    public Note(String nameTmp, String annotationTmp) {
        this.name = nameTmp;
        this.annotation = annotationTmp;
    }

    /**
     * Obtiens l'annotation.
     * @return Retourne l'annotation.
     */
    public String getAnnotation() {
        return annotation;
    }
    /**
     * Obtiens le nom.
     * @return Retourne le nom.
     */
    public String getName() {
        return name;
    }
    /**
     * Modifie l'annotation.
     */
    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
    /**
     * Modifie le nom.
     */
    public void setName(String name) {
        this.name = name;
    }
}
