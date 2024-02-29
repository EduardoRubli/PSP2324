// Eduardo Rubli Castañeira - 2ºDAM Semi
// Actividad1-c (Hilo con expresiones lambda).
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce n1: ");
        int n1 = scanner.nextInt();

        System.out.print("Introduce n2: ");
        int n2 = scanner.nextInt();

        Thread hilo = new Thread(() -> {
            for (int i = n1; i <= n2; i++) {
                System.out.println(i);
                try {
                    Thread.sleep((int) (Math.random() * 1000) + 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        hilo.start();
        System.out.println("El hilo se ha lanzado");
    }

}
