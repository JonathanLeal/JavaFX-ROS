/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dao.TransaccionDao;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.Cliente;
import modelo.Transaccion;

/**
 * FXML Controller class
 *
 * @author Roberto
 */
public class TransaccionesController implements Initializable {
    @FXML
    private TableColumn<Transaccion, Integer>colId;
    @FXML
    private TableColumn<Transaccion, String> colCliente;
    @FXML
    private TableColumn<Transaccion, Double> colMonto;
    @FXML
    private TableColumn<Transaccion, String> colDescripcion;
    @FXML
    private TableColumn<Transaccion, Date> colFecha;
    @FXML
    private TextField txtMonto;
    @FXML
    private Button btnRegistrar;
    @FXML
    private TableView<Transaccion> tablaTrasaccion;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private ComboBox<Cliente> selTransaccionPersona;
    @FXML
    private ComboBox<Cliente> selTransaccionEmpresa;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        llenarSelectPersonas();
        llenarSelectEmpresas();
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id_transaccion"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        llenarTabla();
    }    

    @FXML
    private void vistaInicio(MouseEvent event) throws IOException {
        loadPage("/vistas/Inicio", event);
    }

    @FXML
    private void vistaClientes(MouseEvent event) throws IOException {
        loadPage("/vistas/Clientes", event);
    }

    @FXML
    private void vistaTransacciones(MouseEvent event) throws IOException {
        loadPage("/vistas/Transacciones", event);
    }

    @FXML
    private void vistaReportes(MouseEvent event) throws IOException {
        loadPage("/vistas/Reportes", event);
    }

    @FXML
    private void vistaBitacoras(MouseEvent event) throws IOException {
        loadPage("/vistas/Bitacoras", event);
    }
    
    @FXML
    private void vistaUsuarios(MouseEvent event) throws IOException {
        loadPage("/vistas/Usuarios", event);
    }
    
    public void loadPage(String page, MouseEvent event) throws IOException {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(page + ".fxml"));
        } catch (Exception e) {
            System.out.println("Error al cambiarme de vista: " + e.getMessage());
            e.printStackTrace();
        }

        if (root != null) {
            // Obtén la escena actual del botón clicado
            Scene currentScene = ((Node) event.getSource()).getScene();

            // Establece la nueva escena en la ventana principal
            Stage primaryStage = (Stage) currentScene.getWindow();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }

    @FXML
    private void registrarTransaccion(ActionEvent event) {
        TransaccionDao tDao = new TransaccionDao();
        try {
            String monto = txtMonto.getText();
            String descripcion = txtDescripcion.getText();
            Cliente persona = selTransaccionPersona.getSelectionModel().getSelectedItem();
            Cliente empresa = selTransaccionEmpresa.getSelectionModel().getSelectedItem();
            
            if (monto.isEmpty() || descripcion.isEmpty() || persona != null && empresa != null) {
                mostrarAlerta("Notificacion", "Los campos de monto y descripcion son obligatorios, pero solo puede escoger o persona o empresa", Alert.AlertType.ERROR);
                selTransaccionPersona.getSelectionModel().clearSelection();
                selTransaccionEmpresa.getSelectionModel().clearSelection();
                return;
            }
            
            int idEmpresa = (empresa != null) ? empresa.getId_cliente() : 0;
            int idPersona = (persona != null) ? persona.getId_cliente() : 0;
            
            Transaccion trans = new Transaccion();
            trans.setDescripcion(descripcion);
            trans.setMonto(Double.parseDouble(monto));
            if (empresa == null) {
                tDao.insertarTransaccion(trans, idPersona);
            } else if (persona == null){
                tDao.insertarTransaccion(trans, idEmpresa);
            }
            
            mostrarAlerta("Exito", "Transaccion realizada con exito", Alert.AlertType.CONFIRMATION);
            llenarTabla();
            selTransaccionPersona.getSelectionModel().clearSelection();
            selTransaccionEmpresa.getSelectionModel().clearSelection();
            txtMonto.setText("");
            txtDescripcion.setText("");
        } catch (Exception e) {
            System.out.println("error en insertar transaccion controller: "+e.getMessage());
        }
    }
    
    public void llenarTabla() {
        TransaccionDao tDao = new TransaccionDao();
        int tipoCliente = 0;
        Cliente persona = selTransaccionPersona.getSelectionModel().getSelectedItem();
        Cliente empresa = selTransaccionEmpresa.getSelectionModel().getSelectedItem();

        // Obtener la lista de transacciones directamente
        ArrayList<Transaccion> listaTransacciones = tDao.llenarTransacciones();

        // Limpiar y llenar la tabla con la lista obtenida
        tablaTrasaccion.getItems().clear();
        tablaTrasaccion.getItems().addAll(listaTransacciones);
    }
    
    public void llenarSelectPersonas() {
        TransaccionDao tDao = new TransaccionDao();
        try {
            ArrayList<Cliente> personas = tDao.llenarPersonas();
            selTransaccionPersona.getItems().clear();
            selTransaccionPersona.getItems().addAll(personas);
        } catch (Exception e) {
            System.out.println("error en listar select de personas controller: " + e.getMessage());
        }
    }
    
    public void llenarSelectEmpresas(){
        TransaccionDao tDao = new TransaccionDao();
        try {
            ArrayList<Cliente> empresas = tDao.llenarEmpresas();
            selTransaccionEmpresa.getItems().clear();
            selTransaccionEmpresa.getItems().addAll(empresas);
        } catch (Exception e) {
            System.out.println("error en listar select de empresas controller: "+e.getMessage());
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
