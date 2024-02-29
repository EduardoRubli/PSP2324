// Eduardo Rubli Castañeira - 2ºDAM Semi
// Actividad8c (Productor de café 4 hilos).
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Capsula {
    private String variedadCafe;
    private int intensidad;

    public Capsula(String variedadCafe, int intensidad) {
        this.variedadCafe = variedadCafe;
        this.intensidad = intensidad;
    }

    public String getVariedadCafe() {
        return variedadCafe;
    }

    public int getIntensidad() {
        return intensidad;
    }
}

class Productor implements Runnable {
    private String variedadCafe;
    private int intensidad;
    private List<Capsula> contenedor;
    private static int totalCapsulas = 0;

    public Productor(String variedadCafe, int intensidad, List<Capsula> contenedor) {
        this.variedadCafe = variedadCafe;
        this.intensidad = intensidad;
        this.contenedor = contenedor;
    }

    public int getTotalCapsulas() {
        return totalCapsulas;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {

                // Interrupción de 1000 a 2000 milisegundos.
                Thread.sleep((long) (Math.random() * 501) + 500); // Frecuencia entre 500 y 1000 milisegundos.

                synchronized (contenedor) {
                    // Controla que la lista contenedor no tenga más de 6 cápsulas.
                    if (contenedor.size() < 6) {
                        Capsula capsula = new Capsula(variedadCafe, intensidad);
                        contenedor.add(capsula);
                        // Incrementamos contador total de cápsulas.
                        totalCapsulas++;
                        System.out.println("Hilo Productor: Se ha fabricado una cápsula. \n" +
                                "=> Total de cápsulas en contenedor: " + contenedor.size());
                    } else if (contenedor.size() == 6) {
                        contenedor.notify(); // Notifica al hilo consumidor.
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaura el flag de interrupción.
        }
    }
}

class Consumidor implements Runnable {
    private List<Capsula> contenedor;

    public Consumidor(List<Capsula> contenedor) {
        this.contenedor = contenedor;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (contenedor) {

                    while (contenedor.isEmpty()) {
                        contenedor.wait(); // Espera hasta que haya suficientes cápsulas.
                    }

                    if (!contenedor.isEmpty()) {
                        System.out.println("Hilo Consumidor: Creando caja con 6 cápsulas");

                        for (int i = 0; i < 6; i++) {
                            if (!contenedor.isEmpty()) {
                                contenedor.remove(0);
                            }
                        }

                        System.out.println("Hilo Consumidor: Caja creada");
                    }
                }

                // Espera aleatroria entre 1000 y 3000 milisegundos.
                Thread.sleep((long) (Math.random() * 2001) + 1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaura el flag de interrupción.
        }
    }
}

public class Main {
    public static void main(String[] args) {
        List<Capsula> contenedor = new ArrayList<>();
        List<Thread> productores = new ArrayList<>();
        List<Productor> instanciasProductor = new ArrayList<>();

        // Crear cuatro hilos productores
        for (int i = 0; i < 4; i++) {
            Productor productor = new Productor("Arábica", 5, contenedor);
            instanciasProductor.add(productor); // Guardamos la instancia de Productor.
            Thread hiloProductor = new Thread(productor);
            productores.add(hiloProductor);
            hiloProductor.start();
        }

        Consumidor consumidor = new Consumidor(contenedor);
        Thread hiloConsumidor = new Thread(consumidor);
        hiloConsumidor.start();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Presiona Enter para detener...");
        String value = scanner.nextLine();

        if (value.isEmpty()) {
            // Interrumpir todos los hilos productores
            for (Thread productorThread : productores) {
                productorThread.interrupt();
            }
            hiloConsumidor.interrupt();

            // Esperar a que finalicen los hilos productores y el hilo consumidor
            try {
                for (Thread productorThread : productores) {
                    productorThread.join();
                }
                hiloConsumidor.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int totalCapsulasFabricadas = 0;
            for (Productor productor: instanciasProductor) {
                totalCapsulasFabricadas += productor.getTotalCapsulas();
            }

            System.out.println("Se han fabricado un total de: " + totalCapsulasFabricadas + " cápsulas.");
        }
    }
}