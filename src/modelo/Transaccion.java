package modelo;

import java.sql.Date;

public class Transaccion {
    private int id_transaccion;
    private Date fecha;
    private Cliente cliente;
    private double monto;
    private String descripcion;
    private String nombreCliente;

    public Transaccion() {
    }

    public Transaccion(int id_transaccion, Date fecha, Cliente cliente, double monto, String descripcion, String nombreCliente) {
        this.id_transaccion = id_transaccion;
        this.fecha = fecha;
        this.cliente = cliente;
        this.monto = monto;
        this.descripcion = descripcion;
        this.nombreCliente = nombreCliente;
    }

    public int getId_transaccion() {
        return id_transaccion;
    }

    public void setId_transaccion(int id_transaccion) {
        this.id_transaccion = id_transaccion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
