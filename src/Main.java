import javax.swing.*;
public class Main {
    static int[][] qubitGateList = new int[10][10];
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUI();
            }
        });
    }

}

