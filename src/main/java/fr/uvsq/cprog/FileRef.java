package fr.uvsq.cprog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class FileRef extends ElementRepertory {

    /**
     * Constructor for the File class.
     * @param nameTmp The name of the file.
     * @param nerTmp  The number of the file.
     */
    public FileRef(final String nameTmp, final int nerTmp, final String pathTmp) {
        super(nameTmp, nerTmp, pathTmp);
    }

    public boolean isDirectory() {
        return false;
    }
    public boolean isFile() {
        return true;
    }

    public void delete(Map<String, ElementRepertory> currentRepertoryElements, int deleteId) {
        for (Map.Entry<String, ElementRepertory> entry : currentRepertoryElements.entrySet()) {
            ElementRepertory element = entry.getValue();

            if (element.getNer() == deleteId && element.isFile()) {
                Path path = Paths.get(element.getPath());
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }

    public void visualization(String path) {
        File file = new File(path);

        if (file.exists()) {
            String fileName = file.getName();

            if (fileName.endsWith(".txt")) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(path));
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    reader.close();
                    System.out.println(content.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else{
                System.out.println("Le fichier n'est pas un texte");
                long fileSize = file.length();
                System.out.println("La taille du fichier est de : " + fileSize);
            }
        } else {
            System.out.println("Le fichier n'existe pas");
        }
    }
}
