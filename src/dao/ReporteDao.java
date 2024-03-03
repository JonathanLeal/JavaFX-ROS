package dao;

import conexion.Conexion;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import modelo.Reporte;
import modelo.Transaccion;

public class ReporteDao {
    public int insertarDatosArchivo(LocalDate fechaInicio, LocalDate fechaFin, String nombreReporte){
        Conexion conexion = new Conexion();
        int idsEncontrado = 0;
        try {
            PreparedStatement ids = conexion.getCon().prepareStatement("SELECT id_transaccion FROM transacciones where date(fecha) between ? and ?");
            ids.setDate(1, java.sql.Date.valueOf(fechaInicio));
            ids.setDate(2, java.sql.Date.valueOf(fechaFin));
            ResultSet rs = ids.executeQuery();
            while(rs.next()){
                idsEncontrado = rs.getInt("id_transaccion");
                Reporte reporte = new Reporte();
                PreparedStatement pst = conexion.getCon().prepareStatement("insert into reportes(nombre_reporte,archivo,id_transac,fecha_inicio,fecha_fin) values(?,?,?,?,?)");
                pst.setString(1, nombreReporte);
                pst.setString(2, nombreReporte);
                pst.setInt(3, idsEncontrado);
                pst.setDate(4, java.sql.Date.valueOf(fechaInicio));
                pst.setDate(5, java.sql.Date.valueOf(fechaFin));
                pst.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("error en insertar datos: "+e.getMessage());
        }
        return idsEncontrado;
    }
    
    public ArrayList<Reporte> exportarDatosCSV(LocalDate fechaInicio, LocalDate fechaFin){
        Conexion conexion = new Conexion();
        ArrayList<Reporte> lista = new ArrayList();
        try {
            PreparedStatement pst = conexion.getCon().prepareStatement("select r.id,r.nombre_reporte,t.fecha,r.fecha_inicio,r.fecha_fin,t.nombreCliente from reportes as r \n" +
                                                                        "inner join transacciones as t on r.id_transac = t.id_transaccion\n" +
                                                                        "where t.fecha between ? and ?");
            pst.setDate(1, java.sql.Date.valueOf(fechaInicio));
            pst.setDate(2, java.sql.Date.valueOf(fechaFin));
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                Transaccion transaccion = new Transaccion();
                Reporte reporte = new Reporte();
                reporte.setId(rs.getInt("r.id"));
                reporte.setNombreDocumento(rs.getString("r.nombre_reporte"));
                reporte.setFechaInicio(rs.getDate("r.fecha_inicio"));
                reporte.setFechaFin(rs.getDate("r.fecha_fin"));
                transaccion.setNombreCliente(rs.getString("t.nombreCliente"));
                transaccion.setFecha(rs.getDate("t.fecha"));
                reporte.setTransaccion(transaccion);
                lista.add(reporte);
            }
            System.out.println("lista: "+lista);
            return lista;
        } catch (Exception e) {
            System.out.println("error al exportar los datos del CSV: "+e.getMessage());
        }
        System.out.println("fuera");
        return lista;
    }
}
