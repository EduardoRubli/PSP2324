import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Servidor implements Runnable {

    private final Semaphore semCliente;
    private final Semaphore semServidor;

    public Servidor(Semaphore semCliente, Semaphore semServidor) {
        this.semCliente = semCliente;
        this.semServidor = semServidor;
    }

    @Override
    public void run() {
        try {
            System.out.println("---SERVIDOR---");
            ServerSocket server = new ServerSocket(1234);
            System.out.println("Esperando conexión de un cliente...");
            Socket conexionCliente = server.accept(); // Esperamos que se conecte un cliente.
            System.out.println("¡Cliente conectado!"); // Se ha conectado un cliente.

            // Obtenemos el flujo de entrada y salida.
            DataInputStream flujoEntrada = new DataInputStream(conexionCliente.getInputStream());
            DataOutputStream flujoSalida = new DataOutputStream(conexionCliente.getOutputStream());
            Scanner scanner = new Scanner(System.in);

            String lineaRecibida;
            do {
                semCliente.release(); // Indicamos al cliente que está listo para recibir
                lineaRecibida = flujoEntrada.readUTF();
                System.out.println("El mensaje recibido es: " + lineaRecibida);
                semServidor.acquire(); // Servidor espera a que el cliente esté listo
                if (!lineaRecibida.equalsIgnoreCase("fin")) {
                    System.out.println("Escribe un mensaje para el cliente: ");
                    String lineaEnviar = scanner.nextLine();
                    flujoSalida.writeUTF(lineaEnviar);
                    System.out.println("Envío información al cliente...");
                }
            } while (!lineaRecibida.equalsIgnoreCase("fin"));

            // Cerramos conexiones
            System.out.println("Finalizando servidor");
            conexionCliente.close();
            server.close();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}