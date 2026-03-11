
package model;

/**
 *
 * @author jhona
 */
public class Empleado extends Persona {

    private String cargo;
    private String password;

    public Empleado(long id, String nombre, String cargo, String correo, String direccion, long telefono) {
        super(id, nombre, direccion, telefono, correo);
        this.cargo = cargo;
    }

    public Empleado(long id, String nombre, String cargo, String correo, String direccion, long telefono, String password) {
        super(id, nombre, direccion, telefono, correo);
        this.cargo = cargo;
        this.password = password;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
