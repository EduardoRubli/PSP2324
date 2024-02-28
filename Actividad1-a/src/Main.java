import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce n1: ");
        int n1 = scanner.nextInt();

        System.out.print("Introduce n2: ");
        int n2 = scanner.nextInt();

        HiloRunnable hiloRunnable = new HiloRunnable(n1, n2);
        Thread hilo = new Thread(hiloRunnable);

        hilo.start();
        System.out.println("El hilo se ha lanzado");
    }

}