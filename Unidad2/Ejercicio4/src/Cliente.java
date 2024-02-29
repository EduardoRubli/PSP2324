import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Cliente implements Runnable {

    @Override
    public void run() {
        try {
            System.out.println("---CLIENTE---");
            Socket cliente = new Socket("localhost", 1234); // Conectamos al servidor.
            // Obtenemos el flujo de salida.
            DataOutputStream flujoSalida = new DataOutputStream(cliente.getOutputStream());

            String mensaje = "";
            Scanner scanner = new Scanner(System.in);
            while (!mensaje.equals("fin")) {
                System.out.print("Escribe el mensaje a enviar: ");
                mensaje = scanner.nextLine();
                flujoSalida.writeUTF(mensaje);
            }

            // Cerramos conexiones.
            System.out.println("Finalizando cliente");
            cliente.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

class Servidor implements Runnable {

    @Override
    public void run() {
        System.out.println("---SERVIDOR---");
        try {
            ServerSocket server = new ServerSocket(1234);

            while (true) {
                // Esperamos de manera no bloqueante la conexión de un nuevo cliente.
                Socket conexionCliente = server.accept();

                // Creamos un nuevo hilo para manejar la comunicación con el cliente.
                Thread hiloCliente = new Thread(new ClienteHandler(conexionCliente));
                hiloCliente.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClienteHandler implements Runnable {
        private final Socket clienteSocket;

        public ClienteHandler(Socket socket) {
            this.clienteSocket = socket;
        }

        @Override
        public void run() {
            try {
                System.out.println("¡Cliente conectado!");

                DataInputStream flujoEntrada = new DataInputStream(clienteSocket.getInputStream());

                String lineaRecibida;
                // Leemos datos del cliente hasta recibir "fin".
                while (!(lineaRecibida = flujoEntrada.readUTF()).equalsIgnoreCase("fin")) {
                    System.out.println("El mensaje recibido es: " + lineaRecibida);
                }

                // Cerramos conexión con el cliente.
                System.out.println("Finalizando conexión con cliente.");
                clienteSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}