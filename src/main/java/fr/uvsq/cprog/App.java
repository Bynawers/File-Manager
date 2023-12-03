package fr.uvsq.cprog;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic App for test.
 */
public class App {
    public static void main(final String[] args) {

        Directory root = new Directory("root", 1, null);

        List<ElementRepertory> children = new ArrayList<>();

        File f1 = new File("fichier1", 2, root);
        File f2 = new File("fichier2", 3, root);
        File f3 = new File("fichier3", 4, root);
        children.add(f1);
        children.add(f2);
        children.add(f3);

        root.setChildren(children);
        f1.cut();
        root.listContent();
    }
}
