// Eduardo Rubli Castañeira - 2ºDAM Semi
import java.util.Scanner;

public class Main {
    static volatile boolean exitFlag = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numeroAleatorio = (int) (Math.random() * 11) + 10;

        // Hilo para esperar la entrada del usuario.
        Thread inputThread = new Thread(() -> {
            System.out.println("Pulsa enter cuando creas que el contador ha llegado a "
            + numeroAleatorio + "...");
            String value = scanner.nextLine();
            if (value.isEmpty()) {
                exitFlag = true;
            } else if (!value.isEmpty()){
                System.out.println("Entrada incorrecta.");
                scanner.nextLine(); // Consume la línea incorrecta.
            }

        });

        inputThread.start();
        int i = 1;

        while (!exitFlag) {
            System.out.println("Contador: " + i);
            i++;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        if (i-1 != numeroAleatorio) {
            System.out.println("Vuelve a intentarlo, " +
                    "has detenido el contador en " + (i-1));
        } else {
            System.out.println("¡Lo has conseguido!");
        }

    }
}
