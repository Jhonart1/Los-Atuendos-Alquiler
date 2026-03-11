
package model;

import java.time.OffsetDateTime;

/**
 *
 * @author jhona
 */
public class ServicioAlquiler {
    private long id;
    private OffsetDateTime fechaSolicitud;
    private OffsetDateTime fechaAlquiler;
    private long idEmpleado;
    private long idCliente;
    private String refPrenda;

    public ServicioAlquiler(long id, OffsetDateTime fechaSolicitud, OffsetDateTime fechaAlquiler, long idEmpleado, long idCliente, String refPrenda) {
        this.id = id;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaAlquiler = fechaAlquiler;
        this.idEmpleado = idEmpleado;
        this.idCliente = idCliente;
        this.refPrenda = refPrenda;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OffsetDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(OffsetDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public OffsetDateTime getFechaAlquiler() {
        return fechaAlquiler;
    }

    public void setFechaAlquiler(OffsetDateTime fechaAlquiler) {
        this.fechaAlquiler = fechaAlquiler;
    }

    public long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getRefPrenda() {
        return refPrenda;
    }

    public void setRefPrenda(String refPrenda) {
        this.refPrenda = refPrenda;
    }

    @Override
    public String toString() {
        return "servicioAlquiler{" + "id=" + id + ", fechaSolicitud=" + fechaSolicitud + ", fechaAlquiler=" + fechaAlquiler + ", idEmpleado=" + idEmpleado + ", idCliente=" + idCliente + ", refPrenda=" + refPrenda + '}';
    }
    
    
}
