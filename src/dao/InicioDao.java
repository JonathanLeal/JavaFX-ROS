package dao;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Usuario;

public class InicioDao {
    
    public boolean usuarioExistente(String user, String pass) throws SQLException{
        Conexion conexion = new Conexion();
        boolean usuario = false;
        PreparedStatement usuarioEncontrado = conexion.getCon().prepareStatement("select * from usuarios where nombre_usuario=? and clave=?");
        try {
            usuarioEncontrado.setString(1, user);
            usuarioEncontrado.setString(2, pass);
            ResultSet rs = usuarioEncontrado.executeQuery();
            if (rs.next()) {
                usuario = true;
                System.out.println("usuario: "+usuario);
            }
        } catch (Exception e) {
            System.out.println("Error al loguearse: "+e.getMessage());
        }
        return usuario;
    }
    
    public int obtenerRolUsuario(String user, String pass) throws SQLException{
        int rolObtenido = 0;
        Conexion conexion = new Conexion();
        PreparedStatement id = conexion.getCon().prepareStatement("select id_usuario from usuarios where nombre_usuario=? and clave=?");
        try {
            id.setString(1, user);
            id.setString(2, pass);
            ResultSet rs = id.executeQuery();
            if (rs.next()) {
                int idEncontrado = rs.getInt("id_usuario");
                return idEncontrado;
            } 
        } catch (Exception e) {
            System.out.println("error al encontar el rol del usuario: "+e.getMessage());
        }
        return rolObtenido;
    }
}
