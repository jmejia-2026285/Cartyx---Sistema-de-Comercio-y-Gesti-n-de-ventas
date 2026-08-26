package org.josemejia.system.model;

public class Compra {

    private String idCompra;
    private String usuario;
    private String fecha;
    private double total;

    public Compra(String idCompra, String usuario, String fecha, double total) {
        this.idCompra = idCompra;
        this.usuario = usuario;
        this.fecha = fecha;
        this.total = total;
    }

    public Compra(String usuario, double total) {
        this.usuario = usuario;
        this.total = total;
    }

    public Compra() {
    }

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
