
package model;

/**
 *
 * @author jhona
 */
public class Disfraz extends PrendaDecorator{
    private String nombre;
    private String refTemporal;

    
    /**
     * Constructor obligatorio del Decorator
     * Recibe la prenda base
     */
    public Disfraz (Prenda prenda) {
        super(prenda);
    }

    /**
     * Constructor completo
     */
    public Disfraz (Prenda prenda, String nombre) {
        super(prenda);
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setRefTemporal(String ref) {
        this.refTemporal = ref;
    }

    public String getRefTemporal() {
        return refTemporal;
    }

    @Override
    public String toString() {
        return "Disfraz{" + "nombre=" + nombre + '}';
    }
    
    
}
