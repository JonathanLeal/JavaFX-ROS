package dao;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import modelo.Rol;
import modelo.Usuario;

public class UsuariosDao {
    
    public ArrayList<Usuario> listarUsuarios() throws SQLException {
        ArrayList<Usuario> lista = new ArrayList();
        Conexion conexion = new Conexion();
        PreparedStatement pst = conexion.getCon().prepareStatement("SELECT u.id_usuario,u.nombre_usuario,u.clave,r.nombre_rol "
                + "FROM usuarios as u "
                + "inner join roles as r on u.nivel_acceso = r.id_rol");
        ResultSet rs;
        try {
            rs = pst.executeQuery();
            while(rs.next()){
                Rol rol = new Rol();
                Usuario usuario = new Usuario();
                rol.setNombre_rol(rs.getString("r.nombre_rol"));
                usuario.setId_usuario(rs.getInt("u.id_usuario"));
                usuario.setNombre_usuario(rs.getString("u.nombre_usuario"));
                usuario.setClave(rs.getString("u.clave"));
                usuario.setNivel_acceso(rol);
                lista.add(usuario);
                System.out.println("lleno: "+lista);
            }
        } catch (Exception e) {
            System.out.println("Error al listar usuarios: "+e.getMessage());
            e.printStackTrace();
        }
        System.out.println("lleno: "+lista);
        return lista;
    }
    
    public ArrayList<Rol> roles() throws SQLException{
        Conexion conexion = new Conexion();
        ArrayList<Rol> roles = new ArrayList();
        PreparedStatement pst = conexion.getCon().prepareStatement("select * from roles");
        try {
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                Rol rol = new Rol();
                rol.setId_rol(rs.getInt("id_rol"));
                rol.setNombre_rol(rs.getString("nombre_rol"));
                roles.add(rol);
            }
            return roles;
        } catch (Exception e) {
            System.out.println("error en listar Roles: "+e.getMessage());
            e.printStackTrace();
        }
        return roles;
    }
    
    public void insertarUsuario(String usuario, String clave, int rolId) throws Exception {
        Conexion conexion = new Conexion();

        // Método ficticio para obtener el próximo ID de usuario
        int proximoIdUsuario = obtenerProximoIdUsuario();

        PreparedStatement insertar = conexion.getCon().prepareStatement(
                "insert into usuarios(id_usuario, nombre_usuario, clave, nivel_acceso) values(?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);

        try {
            // Asigna el próximo ID al PreparedStatement
            insertar.setInt(1, proximoIdUsuario);
            insertar.setString(2, usuario);
            insertar.setString(3, clave);
            insertar.setInt(4, rolId);

            // Ejecutar la inserción
            int filasAfectadas = insertar.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Usuario insertado con ID: " + proximoIdUsuario);
            }
        } catch (Exception e) {
            System.out.println("Error al insertar usuarios dao: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void editarUsuarios(Usuario usuario) throws Exception {
        Conexion conexion = new Conexion();
        PreparedStatement pst = conexion.getCon().prepareStatement("update usuarios set nombre_usuario=?, clave=?, nivel_acceso=? where id_usuario=?");
        try {
            pst.setString(1, usuario.getNombre_usuario());
            pst.setString(2, usuario.getClave());
            pst.setInt(3, usuario.getNivel_acceso().getId_rol());
            pst.setInt(4, usuario.getId_usuario());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("errro en editar dao: "+e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void eliminarUsuario(Usuario usuario) throws Exception {
        Conexion conexion = new Conexion();
        PreparedStatement pst = conexion.getCon().prepareStatement("delete from usuarios where id_usuario=?");
        try {
            pst.setInt(1, usuario.getId_usuario());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("error en eliminar dao: "+e.getMessage());
            e.printStackTrace();
        }
    }
    
    private int obtenerProximoIdUsuario() throws SQLException {
        Conexion conexion = new Conexion();
        int proximoId = 0;

        try {
            // Consulta para obtener el máximo ID existente y sumar 1
            String consulta = "SELECT MAX(id_usuario) FROM usuarios";
            PreparedStatement statement = conexion.getCon().prepareStatement(consulta);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                proximoId = resultSet.getInt(1) + 1;
            } else {
                // Si no hay registros en la tabla, comienza desde 1
                proximoId = 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al obtener el próximo ID de usuario.");
        } 
        return proximoId;
    }
}
