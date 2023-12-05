package fr.uvsq.cprog;

import java.io.IOException;

/**
 * Basic App for test.
 */
public class App {
    public static void main(final String[] args) {

        CommandLine app = new CommandLine();
        app.initializeCommands();

        try {
            app.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
