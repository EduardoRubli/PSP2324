import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente implements Runnable {

    @Override
    public void run () {
        try {
            System.out.println("---CLIENTE---");
            Socket cliente = new Socket("localhost", 1234); // Conectamos al servidor.
            // Obtenemos el flujo de salida.
            DataOutputStream flujoSalida = new DataOutputStream(cliente.getOutputStream());
            Scanner scanner = new Scanner(System.in);

            int lineaEnviada;
            do {
                System.out.println("Introduzca un entero positivo: ");
                lineaEnviada = scanner.nextInt();
                // Enviamos datos al servidor.
                flujoSalida.writeInt(lineaEnviada);
            } while (!(lineaEnviada <= 0));

            // Cerramos conexiones
            System.out.println("Finalizando cliente");
            cliente.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Usamos hilos para ejecutar el servidor y el cliente.
    public static void main(String[] args) {

        Servidor servidor = new Servidor();
        Thread hiloServidor = new Thread(servidor);
        hiloServidor.start();

        Cliente cliente = new Cliente();
        Thread hiloCliente = new Thread(cliente);
        hiloCliente.start();

        try {
            // Sincronizamos el hilo servidor con el principal.
            hiloServidor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}