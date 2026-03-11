package model;

import model.Prenda;

public class PrendaBase implements Prenda {

    /**
     * IMPLEMENTACIÓN BASE de Prenda Contiene los datos reales de la tabla
     * prendas
     */
    private String ref;
    private String color;
    private String marca;
    private String talla;
    private double valorAlquiler;
    private String tipo;

    public PrendaBase() {
    }

    public PrendaBase(String ref, String color, String marca, String talla, double valorAlquiler, String tipo) {
        this.ref = ref;
        this.color = color;
        this.marca = marca;
        this.talla = talla;
        this.valorAlquiler = valorAlquiler;
        this.tipo = tipo;
    }

    

    @Override
    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    @Override
    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    @Override
    public double getValorAlquiler() {
        return valorAlquiler;
    }

    public void setValorAlquiler(double valorAlquiler) {
        this.valorAlquiler = valorAlquiler;
    }
    
    @Override
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String marca) {
        this.tipo = marca;
    }
    
    

    @Override
    public String toString() {

        return "PrendaBase{"
                + "ref='" + ref + '\''
                + ", color='" + color + '\''
                + ", marca='" + marca + '\''
                + ", talla='" + talla + '\''
                + ", valorAlquiler=" + valorAlquiler
                + ", tipo="+ tipo + '}';

    }

}

