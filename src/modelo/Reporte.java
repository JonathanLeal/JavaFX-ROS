package modelo;

import java.io.FileWriter;
import java.sql.Date;

public class Reporte {
    private int id;
    private String nombreDocumento;
    private Transaccion transaccion;
    private Date fechaInicio, fechaFin;
    private FileWriter archivo;

    public Reporte() {
    }

    public Reporte(int id, String nombreDocumento, Transaccion transaccion, Date fechaInicio, Date fechaFin, FileWriter archivo) {
        this.id = id;
        this.nombreDocumento = nombreDocumento;
        this.transaccion = transaccion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.archivo = archivo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public Transaccion getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(Transaccion transaccion) {
        this.transaccion = transaccion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public FileWriter getArchivo() {
        return archivo;
    }

    public void setArchivo(FileWriter archivo) {
        this.archivo = archivo;
    }
}
