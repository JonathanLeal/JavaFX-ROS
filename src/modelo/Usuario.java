package modelo;

public class Usuario {
    private int id_usuario;
    private String nombre_usuario, clave;
    private Rol nivel_acceso;

    public Usuario() {
    }

    public Usuario(int id_usuario, String nombre_usuario, String clave, Rol nivel_acceso) {
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.clave = clave;
        this.nivel_acceso = nivel_acceso;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Rol getNivel_acceso() {
        return nivel_acceso;
    }

    public void setNivel_acceso(Rol nivel_acceso) {
        this.nivel_acceso = nivel_acceso;
    }
}
