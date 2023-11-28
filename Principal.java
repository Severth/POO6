package Actividad6EverthPOO;
import javax.swing.*;
public class Principal{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Interface pantalla = new Interface();
            pantalla.setVisible(true);
        });
    }
}
