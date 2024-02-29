// Eduardo Rubli Castañeira - 2ºDAM Semi
// Actividad 1-b (Hilo usando clase anónima).
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce n1: ");
        int n1 = scanner.nextInt();

        System.out.print("Introduce n2: ");
        int n2 = scanner.nextInt();

        // Declaramos la clase abstracta que implementa Runnable.
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (int i = n1; i <= n2; i++) {
                    System.out.println(i);
                    try {
                        Thread.sleep((int) (Math.random() * 1000) + 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        // Le pasamos a Thread un objeto Runnable.
        Thread hilo = new Thread(r);
        // Importante, lanzamos el hilo primero.
        hilo.start();
        System.out.println("El hilo se ha lanzado");
    }
}
