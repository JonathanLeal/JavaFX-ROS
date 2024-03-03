package modelo;

import java.sql.Date;

public class Cliente {
    private int id_cliente,tipo_cliente;
    private String nombre,apellido,genero,nacionalidad,estadoCivil,numDocumento,profesion,empresa,direccion,telefono;
    private Date fecha_registro;

    public Cliente() {
    }

    public Cliente(int id_cliente, int tipo_cliente, String nombre, String apellido, String genero, String nacionalidad, String estadoCivil, String numDocumento, String profesion, String empresa, String direccion, Date fecha_registro, String telefono) {
        this.id_cliente = id_cliente;
        this.tipo_cliente = tipo_cliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.genero = genero;
        this.nacionalidad = nacionalidad;
        this.estadoCivil = estadoCivil;
        this.numDocumento = numDocumento;
        this.profesion = profesion;
        this.empresa = empresa;
        this.direccion = direccion;
        this.fecha_registro = fecha_registro;
        this.telefono = telefono;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getTipo_cliente() {
        return tipo_cliente;
    }

    public void setTipo_cliente(int tipo_cliente) {
        this.tipo_cliente = tipo_cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    @Override
    public String toString() {
        if (getTipo_cliente() == 1) { // Tipo de cliente persona
            return getNombre() + " " + getApellido();
        } else if (getTipo_cliente() == 2) { // Tipo de cliente empresa
            return getEmpresa();
        } else {
            System.out.println("Tipo de cliente no identificado: " + getTipo_cliente());
            return "Cliente no identificado";
        }
    }
}
