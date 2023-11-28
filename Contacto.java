package Actividad6EverthPOO;
import java.io.Serializable;
public class Contacto implements Serializable {
    private String nombre;
    private String numero;
    public Contacto(String nombre, String numero) {
        this.nombre = nombre;
        this.numero = numero;
    }
    public String getNombre() {
        return nombre;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
}

