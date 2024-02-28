import java.util.Scanner;

public class Main {

    static private int n1;

    // Función que verifica si un número es primo.
    public static boolean esPrimo(int num) {
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce el número de hilos a crear: ");
        n1 = scanner.nextInt();

        for (int i = 0; i < n1; i++) {
            int n2 = i + 1; // Almacena el número del hilo.

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    int nAleatorio = (int) (Math.random() * 100) + 1;

                    for (int i = 1; i <= nAleatorio; i++) {
                        if (esPrimo(i)) {
                            System.out.println("Hilo " + n2 + ": el numero '" + i + "' es primo.");
                            try {
                                Thread.sleep((int) (Math.random() * 501) + 500);
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            }
                        }
                    }
                }
            };

            Thread hilo = new Thread(r);
            hilo.start();
        }
    }
}