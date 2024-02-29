import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor implements Runnable {

    @Override
    public void run() {
        try {
            System.out.println("---SERVIDOR---");
            ServerSocket server = new ServerSocket(1234);
            System.out.println("Esperando conexión de un cliente...");
            Socket conexionCliente = server.accept(); // Esperamos que se conecte un cliente.
            System.out.println("¡Cliente conectado!"); // Se ha conectado un cliente.

            // Obtenemos el flujo de entrada.
            DataInputStream flujoEntrada = new DataInputStream(conexionCliente.getInputStream());

            // Leemos datos del cliente hasta recibir la palabra "fin".
            String lineaRecibida;
            while (!(lineaRecibida = flujoEntrada.readUTF()).equalsIgnoreCase("fin")) {
                System.out.println("El mensaje recibido es: " + lineaRecibida);
            }

            // Cerramos conexiones
            System.out.println("Finalizando servidor");
            conexionCliente.close();
            server.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}