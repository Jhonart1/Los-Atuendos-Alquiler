
package model;
import model.Prenda;

/**
 *
 * @author jhona
 */
public class PrendaDecorator implements Prenda {
    
    protected Prenda prenda;

    public PrendaDecorator(Prenda prenda) {
        this.prenda = prenda;
    }
    
    @Override
    public String getRef() {
        return prenda.getRef();
    }

    @Override
    public String getColor() {
        return prenda.getColor();
    }

    @Override
    public String getMarca() {
        return prenda.getMarca();
    }

    @Override
    public String getTalla() {
        return prenda.getTalla();
    }

    @Override
    public double getValorAlquiler() {
        return prenda.getValorAlquiler();
    }
    
    @Override
    public String getTipo() {
        return prenda.getTipo();
    }


    
}
