package model;

/**
 *
 * @author jhona
 */
/**
 * Decorador concreto para VestidoDama Añade atributos específicos de vestidos
 * de dama
 */
public class VestidoDama extends PrendaDecorator {
    private boolean pedreria;
    private String altura;
    private int cantPiezas;
    private String refTemporal;

    /**
     * Constructor obligatorio del Decorator Recibe la prenda base
     */
    public VestidoDama(Prenda prenda) {
        super(prenda);
    }

    /**
     * Constructor completo
     */
    public VestidoDama(Prenda prenda, boolean pedreria, String altura, int cantPiezas) {
        super(prenda);
        this.pedreria = pedreria;
        this.altura = altura;
        this.cantPiezas = cantPiezas;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public int getCantPiezas() {
        return cantPiezas;
    }

    public void setCantPiezas(int cantPiezas) {
        this.cantPiezas = cantPiezas;
    }

    public void setRefTemporal(String ref) {
        this.refTemporal = ref;
    }

    public String getRefTemporal() {
        return refTemporal;
    }

    public boolean isPedreria() {
        return pedreria;
    }

    public void setPedreria(boolean pedreria) {
        this.pedreria = pedreria;
    }
    
    

    @Override
    public String toString() {

        return "VestidoDama{"
                + "ref=" + getRef()
                + ", color=" + getColor()
                + ", marca=" + getMarca()
                + ", talla=" + getTalla()
                + ", valorAlquiler=" + getValorAlquiler()
                + ", tipo=" + getTipo()
                + ", altura=" + altura
                + ", cantPiezas=" + cantPiezas
                + '}';
    }
}
