import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

            int lineaRecibida = 0;

            try {
                lineaRecibida = flujoEntrada.readInt();
            } catch (NumberFormatException e) {
                System.out.println("Solo está permitido introducir enteros.");
                System.exit(0);
            }

            List<Integer> listaEnteros = new ArrayList<>();
            while (!(lineaRecibida <= 0)) {
                listaEnteros.add(lineaRecibida);
                lineaRecibida = flujoEntrada.readInt();
                System.out.println("El valor recibido es: " + lineaRecibida);
            }

            int valor = 0;
            for (int num : listaEnteros) {
                valor += num;
            }

            System.out.println("El valor total es: " + valor);
            // Cerramos conexiones
            System.out.println("Finalizando servidor");
            conexionCliente.close();
            server.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}