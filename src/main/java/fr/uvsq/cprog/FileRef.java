package fr.uvsq.cprog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FileRef extends ElementRepertory {

    /**
     * Constructeur de la classe FileRef.
     * @param nameTmp Le nom du fichier.
     * @param nerTmp  Le ner du fichier.
     * @param pathTmp Le chemin du fichier.
     */
    public FileRef(final String nameTmp,
                   final int nerTmp,
                   final String pathTmp) {
        super(nameTmp, nerTmp, pathTmp);
    }

    /**
     * Indique que l'élément n'est pas un dossier.
     * @return Booléen faux.
     */
    @Override
    public boolean isDirectory() {
        return false;
    }
    /**
     * Indique que l'élément est un fichier.
     * @return Booléen vrai.
     */
    @Override
    public boolean isFile() {
        return true;
    }

    /**
     * Supprime le fichier correspondant à l'instance.
     */
    @Override
    public void delete() {
        Path path = Paths.get(getPath());
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Visualise le fichier, si cest un fichier .txt
     * cela affiche le contenu du texte,
     * sinon cela affiche sa taille.
     * @param path The path of the file.
     */
    public void visualization(final String path) {
        File file = new File(path);

        if (file.exists()) {
            String fileName = file.getName();

            if (fileName.endsWith(".txt")) {
                try {
                    BufferedReader reader = new BufferedReader(
                                            new FileReader(path)
                                            );
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

            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                JFrame frame = new JFrame("Image Viewer");
                ImageIcon imageIcon = new ImageIcon(path);
                JLabel label = new JLabel(imageIcon);
                frame.getContentPane().add(label);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
            else {
                long fileSize = file.length();
                System.out.println("La taille du fichier est de : "
                                    + fileSize
                                    + " Ko");
            }
        } else {
            // fichier n'existe pas
        }
    }
}
