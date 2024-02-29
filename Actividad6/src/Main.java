// Eduardo Rubli Castañeira - 2ºDAM Semi
import java.text.SimpleDateFormat;
import java.util.Date;

class Log {
    public synchronized void escribir(int idHilo, String mensaje) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss yyyy/MM/dd");
        String fechaYHora = formatter.format(new Date());

        System.out.println("ID: " + idHilo + " - " + fechaYHora);
        System.out.println(mensaje);
        System.out.println();
    }
}

class HiloLog extends Thread {
    private Log log;
    private int idHilo;

    public HiloLog(Log log, int idHilo) {
        this.log = log;
        this.idHilo = idHilo;
    }

    public void run() {
        for (int i = 1; i <= 100; i++) {
            log.escribir(idHilo, "Este es mi mensaje número " + i);
            try {
                Thread.sleep(100); // Simula cierta carga de trabajo.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Log log = new Log();

        // Crear y ejecutar 5 hilos.
        for (int i = 1; i <= 5; i++) {
            Thread hilo = new HiloLog(log, i);
            hilo.start();
        }
    }
}
