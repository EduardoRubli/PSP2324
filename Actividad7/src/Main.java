// Eduardo Rubli Castañeira - 2ºDAM Semi
// Actividad7 (Fabricación y montaje de vehículo).
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Motor {
    private int potencia;
    private float peso;

    public Motor(int potencia, float peso) {
        this.potencia = potencia;
        this.peso = peso;
    }
}

class Carroceria {
    private String color;
    private int puertas;
    private boolean acabado;

    public Carroceria(String color, int puertas, boolean acabado) {
        this.color = color;
        this.puertas = puertas;
        this.acabado = acabado;
    }
}

class Bateria {
    private int potencia;
    private float peso;

    public Bateria(int potencia, float peso) {
        this.potencia = potencia;
        this.peso = peso;
    }
}

class Almacen {
    private List<Motor> motores;
    private List<Carroceria> carrocerias;
    private List<Bateria> baterias;

    public Almacen() {
        motores = new ArrayList<>();
        carrocerias = new ArrayList<>();
        baterias = new ArrayList<>();
    }

    public synchronized void almacenarMotor(Motor motor) throws InterruptedException {
        while (motores.size() >= 5) {
            wait(); // Esperar si el almacén de motores está lleno
        }
        motores.add(motor);
        System.out.println("Total de motores en almacén: " + motores.size());
        notifyAll(); // Notificar a los consumidores
    }

    public synchronized void almacenarCarroceria(Carroceria carroceria) throws InterruptedException {
        while (carrocerias.size() >= 5) {
            wait(); // Esperar si el almacén de carrocerías está lleno
        }
        carrocerias.add(carroceria);
        System.out.println("Total de carrocerías en almacén: " + carrocerias.size());
        notifyAll(); // Notificar a los consumidores
    }

    public synchronized void almacenarBateria(Bateria bateria) throws InterruptedException {
        while (baterias.size() >= 5) {
            wait(); // Esperar si el almacén de baterías está lleno
        }
        baterias.add(bateria);
        System.out.println("Total de baterías en almacén: " + baterias.size());
        notifyAll(); // Notificar a los consumidores
    }

    public synchronized void ensamblarVehiculo() {
        try {
            while (carrocerias.isEmpty() || motores.isEmpty() || baterias.isEmpty()) {
                wait(); // Esperar si no hay suficientes componentes.
            }
            Carroceria carroceria = carrocerias.remove(0);
            Motor motor = motores.remove(0);
            Bateria bateria = baterias.remove(0);
            System.out.println("Se ha ensamblado un vehículo");
            notifyAll(); // Notificar a los productores
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}

class FabricarMotor implements Runnable {
    private Almacen almacen;

    public FabricarMotor(Almacen almacen) {
        this.almacen = almacen;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(1000); // Simulamos tiempo de fabricación.
                Motor motor = new Motor(120, 50);
                System.out.println("Se ha fabricado un motor.");
                almacen.almacenarMotor(motor);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class FabricarCarroceria implements Runnable {
    private Almacen almacen;

    public FabricarCarroceria(Almacen almacen) {
        this.almacen = almacen;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(1500); // Simulamos tiempo de fabricación.
                Carroceria carroceria = new Carroceria("Rojo", 4, true);
                System.out.println("Se ha fabricado una carrocería.");
                almacen.almacenarCarroceria(carroceria);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class FabricarBateria implements Runnable {
    private Almacen almacen;

    public FabricarBateria(Almacen almacen) {
        this.almacen = almacen;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(2000); // Simulamos tiempo de fabricación.
                // Se fabrica batería y luego se almacena.
                Bateria bateria = new Bateria(500, 100);
                System.out.println("Se ha fabricado una batería.");
                almacen.almacenarBateria(bateria);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class EnsamblarVehiculo implements Runnable {
    private Almacen almacen;
    private static int vehiculosEnsamblados = 0;

    public EnsamblarVehiculo(Almacen almacen) {
        this.almacen = almacen;
    }

    public int getVehiculosEnsamblados() {
        return vehiculosEnsamblados;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(3000); // Simulamos tiempo de ensamblaje.
                almacen.ensamblarVehiculo();
                vehiculosEnsamblados++;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // Instanciamos la clase almacén.
        Almacen almacen = new Almacen();

        // Creamos instancia de la clase EnsamblarVehiculo.
        EnsamblarVehiculo ensamblarVehiculo = new EnsamblarVehiculo(almacen);

        Thread hiloFabricarMotor = new Thread(new FabricarMotor(almacen));
        Thread hiloFabricarCarroceria = new Thread(new FabricarCarroceria(almacen));
        Thread hiloFabricarBateria = new Thread(new FabricarBateria(almacen));

        Thread hiloEnsamblarVehiculo = new Thread(ensamblarVehiculo);

        hiloFabricarMotor.start();
        hiloFabricarCarroceria.start();
        hiloFabricarBateria.start();
        hiloEnsamblarVehiculo.start();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Presiona Enter para detener...");
        String value = scanner.nextLine();

        if (value.isEmpty()) {
            hiloFabricarCarroceria.interrupt();
            hiloFabricarBateria.interrupt();
            hiloFabricarMotor.interrupt();
            hiloEnsamblarVehiculo.interrupt();

            try {
                hiloFabricarCarroceria.join();
                hiloFabricarBateria.join();
                hiloFabricarMotor.join();
                hiloEnsamblarVehiculo.join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            System.out.println("Se han ensamblado un total de " +
                    ensamblarVehiculo.getVehiculosEnsamblados() + " vehículos");
        }
    }
}
