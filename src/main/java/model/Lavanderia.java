
package model;

/**
 *
 * @author jhona
 */
public class Lavanderia {
    
    private Long id;
    private String refPrenda;
    private String prioridad;
    private String observacion;

    public Lavanderia(Long id, String refPrenda, String prioridad, String observacion) {
        this.id = id;
        this.refPrenda = refPrenda;
        this.prioridad = prioridad;
        this.observacion = observacion;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefPrenda() {
        return refPrenda;
    }

    public void setRefPrenda(String refPrenda) {
        this.refPrenda = refPrenda;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
    
    
}
