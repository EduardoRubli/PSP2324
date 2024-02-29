import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Cliente implements Runnable {

    private final Semaphore semCliente;
    private final Semaphore semServidor;
    private boolean primerMensajeRecibido;

    public Cliente(Semaphore semCliente, Semaphore semServidor) {
        this.semCliente = semCliente;
        this.semServidor = semServidor;
        this.primerMensajeRecibido = false;
    }

    @Override
    public void run() {
        try {
            System.out.println("---CLIENTE---");
            Socket cliente = new Socket("localhost", 1234); // Conectamos al servidor.
            // Obtenemos el flujo de salida.
            DataOutputStream flujoSalida = new DataOutputStream(cliente.getOutputStream());
            DataInputStream flujoEntrada = new DataInputStream(cliente.getInputStream());
            Scanner scanner = new Scanner(System.in);

            String lineaEnviada;
            String lineaRecibida;
            do {
                semCliente.acquire(); // Cliente espera a que el servidor esté listo.
                if (primerMensajeRecibido) {
                    // Si ya se ha recibido un mensaje del servidor, leemos el siguiente mensaje.
                    lineaRecibida = flujoEntrada.readUTF();
                    System.out.println("Respuesta del servidor: " + lineaRecibida);
                } else {
                    // Si es la primera vez, no esperamos un mensaje del servidor
                    primerMensajeRecibido = true;
                }
                System.out.println("Escribe un mensaje para el servidor: ");
                lineaEnviada = scanner.nextLine();
                // Enviamos datos al servidor.
                flujoSalida.writeUTF(lineaEnviada);
                System.out.println("Envío información al servidor...");
                semServidor.release(); // Indicamos al servidor que está listo para recibir
            } while (!lineaEnviada.equalsIgnoreCase("fin"));

            // Cerramos conexiones
            System.out.println("Finalizando cliente");
            cliente.close();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Usamos hilos para ejecutar el servidor y el cliente.
    public static void main(String[] args) {
        Semaphore semCliente = new Semaphore(0);
        Semaphore semServidor = new Semaphore(1);

        Cliente cliente = new Cliente(semCliente, semServidor);
        Servidor servidor = new Servidor(semCliente, semServidor);

        Thread hiloCliente = new Thread(cliente);
        Thread hiloServidor = new Thread(servidor);

        hiloCliente.start();
        hiloServidor.start();
    }
}