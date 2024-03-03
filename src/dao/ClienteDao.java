package dao;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import modelo.Cliente;

public class ClienteDao {
    public void insertarCliente(Cliente cliente, int tipoCliente) throws Exception{
        Conexion conexion = new Conexion();
        LocalDate fecha = LocalDate.now();
        java.sql.Date sqlDate = java.sql.Date.valueOf(fecha);
        PreparedStatement cNatural = conexion.getCon().prepareStatement("insert into clientes(id_cliente,nombre,apellidos,genero,nacionalidad,"
                + "documento,profesion,id_tipo_persona,fecha_creacion,telefono) values(?,?,?,?,?,?,?,?,?,?)");
        PreparedStatement cJuridico = conexion.getCon().prepareStatement("insert into clientes(id_cliente,empresa,direccion,telefono,fecha_creacion,id_tipo_persona)"
                + "values(?,?,?,?,?,?)");
        try {
            if (tipoCliente == 1) {
                cNatural.setInt(1, obtenerProximoIdUsuario());
                cNatural.setString(2, cliente.getNombre());
                cNatural.setString(3, cliente.getApellido());
                cNatural.setString(4, cliente.getGenero());
                cNatural.setString(5, cliente.getNacionalidad());
                cNatural.setString(6, cliente.getNumDocumento());
                cNatural.setString(7, cliente.getProfesion());
                cNatural.setInt(8, tipoCliente);
                // Convertir LocalDate a java.sql.Date y establecer el valor en la consulta
                cNatural.setDate(9, sqlDate);
                cNatural.setString(10, cliente.getTelefono());
                cNatural.executeUpdate();
            } else {
                cJuridico.setInt(1, obtenerProximoIdUsuario());
                cJuridico.setString(2, cliente.getEmpresa());
                cJuridico.setString(3, cliente.getDireccion());
                cJuridico.setString(4, cliente.getTelefono());
                cJuridico.setDate(5, sqlDate);
                cJuridico.setInt(6, tipoCliente);
                cJuridico.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("error al insertar clientes: "+e.getMessage());
            e.printStackTrace();
        }
    }
    
    public ArrayList<Cliente> listarClientes(int tipoCliente){
        ArrayList lista = new ArrayList();
        Conexion conexion = new Conexion();
        PreparedStatement pst;
        try {
            if (tipoCliente == 1) {
                pst = conexion.getCon().prepareStatement("select id_cliente,nombre,apellidos,nacionalidad,documento,"
                        + "telefono,ultima_consulta,profesion,direccion from clientes where id_tipo_persona=?");
                pst.setInt(1, tipoCliente);
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    Cliente cliente = new Cliente();
                    cliente.setId_cliente(rs.getInt("id_cliente"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setApellido(rs.getString("apellidos"));
                    cliente.setNacionalidad(rs.getString("nacionalidad"));
                    cliente.setTelefono(rs.getString("telefono"));
                    cliente.setFecha_registro(rs.getDate("ultima_consulta"));
                    cliente.setNumDocumento(rs.getString("documento"));
                    cliente.setProfesion(rs.getString("profesion"));
                    cliente.setDireccion(rs.getString("direccion"));
                    lista.add(cliente);
                }
                System.out.println("personas: "+lista);
                return lista;
            } else {
                pst = conexion.getCon().prepareStatement("select id_cliente,empresa,telefono,direccion,"
                        + "ultima_consulta from clientes where id_tipo_persona=?");
                pst.setInt(1, tipoCliente);
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    Cliente cliente = new Cliente();
                    cliente.setId_cliente(rs.getInt("id_cliente"));
                    cliente.setEmpresa(rs.getString("empresa"));
                    cliente.setTelefono(rs.getString("telefono"));
                    cliente.setDireccion(rs.getString("direccion"));
                    cliente.setFecha_registro(rs.getDate("ultima_consulta"));
                    lista.add(cliente);
                }
                System.out.println("empresas: "+lista);
                return lista;
            }
        } catch (Exception e) {
            System.out.println("error en listar clientes: "+e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
    
    private int obtenerProximoIdUsuario() throws SQLException {
        Conexion conexion = new Conexion();
        int proximoId = 0;

        try {
            // Consulta para obtener el máximo ID existente y sumar 1
            String consulta = "SELECT MAX(id_cliente) FROM clientes";
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
    
    public boolean validarCampos(int tipoCliente, String documento, String empresa){
        Conexion conexion = new Conexion();
        boolean existe = false;
        PreparedStatement pst;
        ResultSet rs;
        try {
            if (tipoCliente == 1) {
                pst = conexion.getCon().prepareStatement("select id_cliente from clientes where id_tipo_persona=? and documento=?");
                pst.setInt(1, tipoCliente);
                pst.setString(2, documento);
                rs = pst.executeQuery();
                if (rs.next()) {
                    existe = rs.getBoolean("id_cliente");
                    return existe;
                }
            } else {
                pst = conexion.getCon().prepareStatement("select id_cliente from clientes where id_tipo_persona=? and empresa=?");
                pst.setInt(1, tipoCliente);
                pst.setString(2, empresa);
                rs = pst.executeQuery();
                if (rs.next()) {
                    existe = rs.getBoolean("id_cliente");
                    return existe;
                }
            }
        } catch (Exception e) {
            System.out.println("error al validar los campos de: "+e.getMessage());
            e.printStackTrace();
        }
        return existe;
    }
}
