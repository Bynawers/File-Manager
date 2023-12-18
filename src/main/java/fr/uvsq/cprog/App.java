package fr.uvsq.cprog;

import java.io.IOException;

/**
 * Basic App for test.
 */
public final class App {
    /**
     * Main method to start the application.
     *
     * @param args Command-line arguments.
    */
    public static void main(final String[] args) {

        //System.out.println(System.getProperty("user.dir"));

        CommandLine app = new CommandLine();
        app.initializeCommands();

        try {
            app.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private App() {
        throw new AssertionError("This class should not be instantiated.");
    }
}
