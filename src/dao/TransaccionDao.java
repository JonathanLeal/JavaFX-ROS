package dao;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;
import modelo.Transaccion;

public class TransaccionDao {
    public ArrayList<Cliente> llenarPersonas() {
        Conexion conexion = new Conexion();
        ArrayList<Cliente> lista = new ArrayList();
        try {
            PreparedStatement ps = conexion.getCon().prepareStatement("select id_cliente, nombre, apellidos from clientes where id_tipo_persona=?");
            ps.setInt(1, 1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setTipo_cliente(1);
                cliente.setId_cliente(rs.getInt("id_cliente"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellidos"));
                System.out.println("ID: " + cliente.getId_cliente() + ", Nombre: " + cliente.getNombre() + ", Apellido: " + cliente.getApellido());
                lista.add(cliente);
            }
            return lista;
        } catch (Exception e) {
            System.out.println("error al llenar el select clientes dao: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Personas: " + lista);
        return lista;
    }
    
    public ArrayList<Cliente> llenarEmpresas() {
        Conexion conexion = new Conexion();
        ArrayList<Cliente> lista = new ArrayList();
        try {
            PreparedStatement ps = conexion.getCon().prepareStatement("select id_cliente,empresa from clientes where id_tipo_persona=?");
            ps.setInt(1, 2);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Cliente cliente = new Cliente();
                cliente.setTipo_cliente(2);
                cliente.setId_cliente(rs.getInt("id_cliente"));
                cliente.setEmpresa(rs.getString("empresa"));
                lista.add(cliente);
            }
            return lista;
        } catch (Exception e) {
            System.out.println("error al llenar el select clientes dao: "+e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
    
    public String recuperarNombreCliente(int idCliente) {
        Conexion con = new Conexion();
        String result = null;
        PreparedStatement pst;
        ResultSet rs;
        try {
            pst = con.getCon().prepareStatement("select c.nombre,c.apellidos,c.empresa from clientes as c where c.id_cliente=?");
            pst.setInt(1, idCliente);
            rs = pst.executeQuery();
            if (rs.next()) {
                String nombre = rs.getString("c.nombre");
                String apellido = rs.getString("c.apellidos");
                String empresa = rs.getString("c.empresa");
                
                if (nombre == null && apellido == null && empresa != null) {
                    return result = empresa;
                } else if(nombre != null && apellido != null && empresa == null) {
                    return result = nombre + " " + apellido;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al capturar el nombre del cliente: "+e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    
    public void insertarTransaccion(Transaccion transaccion, int idCliente){
        Conexion conexion = new Conexion();
        LocalDate fecha = LocalDate.now();
        java.sql.Date sqlDate = java.sql.Date.valueOf(fecha);
        try {
            PreparedStatement pst = conexion.getCon().prepareStatement("insert into transacciones(id_transaccion,fecha,"
                    + "id_cliente,monto,descripcion,nombreCliente) values(?,?,?,?,?,?)");
            pst.setInt(1, obtenerProximoIdUsuario());
            pst.setDate(2, sqlDate);
            pst.setInt(3, idCliente);
            pst.setDouble(4, transaccion.getMonto());
            pst.setString(5, transaccion.getDescripcion());
            pst.setString(6, recuperarNombreCliente(idCliente));
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error al insertar datos: "+e.getMessage());
            e.printStackTrace();
        }
    }
    
    public ArrayList<Transaccion> llenarTransacciones(){
        Conexion conexion = new Conexion();
        ArrayList<Transaccion> lista = new ArrayList();
        PreparedStatement pst;
        try {
            pst = conexion.getCon().prepareStatement("select t.id_transaccion,t.monto,t.descripcion,t.fecha,t.nombreCliente from transacciones as t");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {                    
                Transaccion trans = new Transaccion();
                trans.setId_transaccion(rs.getInt("t.id_transaccion"));
                trans.setMonto(rs.getDouble("t.monto"));
                trans.setDescripcion(rs.getString("t.descripcion"));
                trans.setFecha(rs.getDate("t.fecha"));
                trans.setNombreCliente(rs.getString("t.nombreCliente"));
                lista.add(trans);
            }
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar las transacciones: "+e.getMessage());
        }
        return lista;
    }
    
    private int obtenerProximoIdUsuario() throws SQLException {
        Conexion conexion = new Conexion();
        int proximoId = 0;

        try {
            // Consulta para obtener el máximo ID existente y sumar 1
            String consulta = "SELECT MAX(id_transaccion) FROM transacciones";
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
