package fr.uvsq.cprog;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

public class AppTest 
{
 @Test
    public void testMain() throws InterruptedException {
        final PrintStream originalOut = System.out; // Sauvegarde de la sortie standard actuelle
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream(); //capture de la sortie standard
        System.setOut(new PrintStream(outContent)); //redirection de la sortie standard

        // Création d'un thread pour exécuter la méthode main
        // Cela permet de l'exécuter sans bloquer le test
        Thread thread = new Thread(() -> { // liste d'instruction 
            String[] args = {};
            App.main(args);
        });

        thread.start();

        // Attendez un peu pour laisser la méthode `main` démarrer
        Thread.sleep(1000);

        // Interrompre le thread pour terminer `main`
        thread.interrupt();

        // Attendez que le thread se termine
        thread.join(1000);

        // Vérifier que le thread est terminé
        assertFalse(thread.isAlive());

        System.setOut(originalOut);
    }
    

}





