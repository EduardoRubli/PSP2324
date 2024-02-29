// Eduardo Rubli Castañeira - 2ºDAM Semi
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.Runnable;

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

        // Creamos una lista de hilos donde vamos a almacenarlos.
        List<Thread> listaDeHilos = new ArrayList<>();

        for (int i = 0; i < n1; i++) {
            int n2 = i + 1; // Crear una variable local para almacenar el número del hilo

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
            hilo.setName("Hilo-" + (i+1));
            listaDeHilos.add(hilo);
            hilo.start();

        }

        while(true) {

            boolean hilosFinalizados = true;

            for (Thread hiloActual : listaDeHilos) {
                System.out.println("Para el hilo '" + hiloActual.getName() + "' el ID es: " + hiloActual.getId() +
                        "El estado de '" + hiloActual.getName() + "' es: " + hiloActual.getState());
                if (hiloActual.isAlive()) {
                    hilosFinalizados = false;
                }
            }

            if (hilosFinalizados) { break; }

            try {
                // Se pausa la ejecución del hilo principal (Main).
                //
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
