
package model;

import model.PrendaDecorator;

/**
 *
 * @author jhona
 */
public class TrajeCaballero extends PrendaDecorator{
    
    private String tipoTraje;
    private String aderezo;
    private String refTemporal;

    /**
     * Constructor obligatorio del Decorator
     * Recibe la prenda base
     */
    public TrajeCaballero (Prenda prenda) {
        super(prenda);
    }

    /**
     * Constructor completo
     */
    public TrajeCaballero (Prenda prenda, String tipo, String aderezo) {
        super(prenda);
        this.tipoTraje = tipo;
        this.aderezo = aderezo;
    }

    public String getTipoTraje() {
        return tipoTraje;
    }

    public void setTipoTraje(String tipoTraje) {
        this.tipoTraje = tipoTraje;
    }

    public String getAderezo() {
        return aderezo;
    }

    public void setAderezo(String aderezo) {
        this.aderezo = aderezo;
    }
    
    public void setRefTemporal(String ref) {
        this.refTemporal = ref;
    }

    public String getRefTemporal() {
        return refTemporal;
    }
    
    @Override
    public String toString() {
        return "TrajeCaballero{" + "tipo=" + tipoTraje + ", aderezo=" + aderezo + '}';
    }
    
    
}
