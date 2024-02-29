import java.util.Scanner;

public class HiloRunnable implements Runnable {

    private int n1;
    private int n2;

    public HiloRunnable(int n1, int n2) {
        this.n1 = n1;
        this.n2 = n2;
    }

    @Override
    public void run() {
        for (int i = n1; i <= n2; i++) {
            System.out.println(i);
            try {
                Thread.sleep((int) (Math.random() * 1000) + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

