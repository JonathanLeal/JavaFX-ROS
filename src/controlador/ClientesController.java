package controlador;

import dao.ClienteDao;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modelo.Cliente;

public class ClientesController implements Initializable{
    @FXML
    private TableView<Cliente> tablaPersona;
    @FXML
    private TableView<Cliente> tablaEmpresas;
    @FXML
    private TableColumn<Cliente, Integer> colIdBit;
    @FXML
    private TableColumn<Cliente, String> colCliente;
    @FXML
    private TableColumn<Cliente, String> colDocumento;
    @FXML
    private TableColumn<Cliente, String> colNacionalidad;
    @FXML
    private TableColumn<Cliente, Date> colFechaRegistrado;
    @FXML
    private Button btnAbrirModalPersona;
    @FXML
    private Button btnAbrirModalEmpresa;
    @FXML
    private TableColumn<Cliente, String> colDirec;
    @FXML
    private TableColumn<Cliente, String> colProfesion;
    @FXML
    private TableColumn<Cliente, Integer> colId;
    @FXML
    private TableColumn<Cliente, String> colEmpresa;
    @FXML
    private TableColumn<Cliente, String> colDireccion;
    @FXML
    private TableColumn<Cliente, String> colTelefono;
    @FXML
    private TableColumn<Cliente, String> colFechaEmpresa;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurando celdas de la tabla de personas
        colIdBit.setCellValueFactory(new PropertyValueFactory<>("id_cliente"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("numDocumento"));
        colDirec.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colProfesion.setCellValueFactory(new PropertyValueFactory<>("profesion"));
        colNacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        colFechaRegistrado.setCellValueFactory(new PropertyValueFactory<>("fecha_registro"));

        // Configurando celdas de la tabla de empresas
        colId.setCellValueFactory(new PropertyValueFactory<>("id_cliente"));
        colEmpresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colFechaEmpresa.setCellValueFactory(new PropertyValueFactory<>("fecha_registro"));
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
            Scene currentScene = ((Node) event.getSource()).getScene();

            Stage primaryStage = (Stage) currentScene.getWindow();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }

    @FXML
    private void abrirModalPersona() {
        // Crear el contenido del modal de Persona
        int tipo_cliente = 1;
        VBox root = new VBox(10);
        TextField nombreField = new TextField();
        TextField apellidoField = new TextField();
        TextField documentoField = new TextField();
        TextField direccionField = new TextField();
        TextField telefonoField = new TextField();
        TextField profesionField = new TextField();
        TextField nacionalidadField = new TextField();
        Button cerrarButton = new Button("Cerrar");
        Button guardarButton = new Button("Guardar");

        root.getChildren().addAll(
                new Label("Nombre:"), nombreField,
                new Label("Apellido:"), apellidoField,
                new Label("Numero Doc:"), documentoField,
                new Label("Direccion:"), direccionField,
                new Label("Telefono:"), telefonoField,
                new Label("Profesion:"), profesionField,
                new Label("Nacionalidad:"), nacionalidadField,
                cerrarButton,
                guardarButton
        );
        
        tablaEmpresas.setVisible(false);
        tablaPersona.setVisible(true);
        llenarTablaPersona();

        // Ajustar el tamaño del VBox
        root.setPrefSize(700, 500);

        // Crear el Stage para el modal de Persona
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initStyle(StageStyle.UNDECORATED);
        modalStage.setTitle("Modal de Persona");
        modalStage.setScene(new Scene(root, 400, 600));
        
        //evento para el boton de guardar
        guardarButton.setOnAction(event -> {
            try {
                Cliente cliente = new Cliente();
                ClienteDao cDao = new ClienteDao();
                if (nombreField.getText().isEmpty() || apellidoField.getText().isEmpty() || direccionField.getText().isEmpty()
                    || documentoField.getText().isEmpty() || telefonoField.getText().isEmpty()
                    || profesionField.getText().isEmpty() || nacionalidadField.getText().isEmpty()) 
                {
                    mostrarAlerta("Datos vacios", "Debe completar todos los campos por favor", Alert.AlertType.WARNING);
                } else {
                    boolean documentoExistente = cDao.validarCampos(1, documentoField.getText(), null);
                    if (documentoExistente) {
                        mostrarAlerta("Documente existente", "Ese documento ya existe", Alert.AlertType.WARNING);
                        documentoField.setText("");
                    } else {
                        cliente.setNombre(nombreField.getText());
                        cliente.setApellido(apellidoField.getText());
                        cliente.setDireccion(direccionField.getText());
                        cliente.setTelefono(telefonoField.getText());
                        cliente.setProfesion(profesionField.getText());
                        cliente.setNumDocumento(documentoField.getText());
                        cliente.setNacionalidad(nacionalidadField.getText());
                        cDao.insertarCliente(cliente, 1);

                        mostrarAlerta("Exito", "Cliente registrado con exito", Alert.AlertType.CONFIRMATION);
                        modalStage.close();

                        llenarTablaPersona();

                        //limpiando campos 
                        nombreField.setText("");
                        apellidoField.setText("");
                        documentoField.setText("");
                        nacionalidadField.setText("");
                        direccionField.setText("");
                        profesionField.setText("");
                        telefonoField.setText("");
                    }
                }
            } catch (Exception e) {
                System.out.println("error en insertar al cliente controller: "+e.getMessage());
            }
        });

        // Configurar el evento para cerrar el modal
        cerrarButton.setOnAction(event -> modalStage.close());

        // Mostrar el modal y esperar hasta que se cierre
        modalStage.showAndWait();
    }

    @FXML
    private void abrirModalEmpresa() {
        // Crear el contenido del modal de Empresa
        int tipo_cliente = 2;
        VBox root = new VBox(10);
        TextField nombreEmpresaField = new TextField();
        TextField direccionField = new TextField();
        TextField telefonoField = new TextField();
        Button cerrarButton = new Button("Cerrar");
        Button guardarButton = new Button("Guardar");

        root.getChildren().addAll(
                new Label("Nombre Empresa:"), nombreEmpresaField,
                new Label("Direccion:"), direccionField,
                new Label("Telefono:"), telefonoField,
                cerrarButton,
                guardarButton
        );
        
        tablaPersona.setVisible(false);
        tablaEmpresas.setVisible(true);
        llenarTablaEmpresa();

        // Ajustar el tamaño del VBox
        root.setPrefSize(700, 500);

        // Crear el Stage para el modal de Empresa
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initStyle(StageStyle.UNDECORATED);
        modalStage.setTitle("Modal de Empresa");
        modalStage.setScene(new Scene(root, 300, 250));
        
        //evento para el boton de guardar
        guardarButton.setOnAction(event -> {
            ClienteDao cDao = new ClienteDao();
            try {
                if (nombreEmpresaField.getText().isEmpty() || direccionField.getText().isEmpty() ||
                   telefonoField.getText().isEmpty()) 
                {
                   mostrarAlerta("Datos vacios", "Debe completar todos los campos por favor", Alert.AlertType.WARNING);
                } else {
                    boolean empresaExistente = cDao.validarCampos(2, null, nombreEmpresaField.getText());
                    if (empresaExistente) {
                        mostrarAlerta("Empresa ya existe", "Empresa ya registrada por favor ingresa otra", Alert.AlertType.WARNING);
                        nombreEmpresaField.setText("");
                    } else {
                        Cliente cliente = new Cliente();
                        cliente.setEmpresa(nombreEmpresaField.getText());
                        cliente.setDireccion(direccionField.getText());
                        cliente.setTelefono(telefonoField.getText());

                        cDao.insertarCliente(cliente, 2);

                        mostrarAlerta("Exito", "Cliente registrado con exito", Alert.AlertType.CONFIRMATION);
                        modalStage.close();

                        llenarTablaEmpresa();

                        //limpiar campos
                        nombreEmpresaField.setText("");
                        direccionField.setText("");
                        telefonoField.setText("");
                    }
                }
            } catch (Exception e) {
                System.out.println("errror al crear cliente controller: "+e.getMessage());
                e.printStackTrace();
            }
        });
        
        // Configurar el evento para cerrar el modal
        cerrarButton.setOnAction(event -> modalStage.close());

        // Mostrar el modal y esperar hasta que se cierre
        modalStage.showAndWait();
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private void llenarTablaPersona() {
        ClienteDao cDao = new ClienteDao();
        try {
            // Obtén los datos de la base de datos
            ArrayList<Cliente> listaClientes = cDao.listarClientes(1);
            // Limpia la tabla antes de agregar nuevos datos
            
            for (Cliente cliente : listaClientes) {
                System.out.println("Nombre: " + cliente.getNombre() + ", Documento: " 
                + cliente.getNumDocumento() + ", Profesión: " + cliente.getProfesion() 
                + "Direccion" + cliente.getDireccion());
            }

            
            tablaPersona.getItems().clear();

            // Agrega los nuevos datos a la tabla
            tablaPersona.getItems().addAll(listaClientes);
        } catch (Exception e) {
            System.out.println("Error al llenar tabla de personas: " + e.getMessage());
        }
    }

    private void llenarTablaEmpresa() {
        ClienteDao cDao = new ClienteDao();
        try {
            // Obtén los datos de la base de datos
            ArrayList<Cliente> listaClientes = cDao.listarClientes(2);

            // Limpia la tabla antes de agregar nuevos datos
            tablaEmpresas.getItems().clear();

            // Agrega los nuevos datos a la tabla
            tablaEmpresas.getItems().addAll(listaClientes);
        } catch (Exception e) {
            System.out.println("Error al llenar tabla de empresas: " + e.getMessage());
        }
    }
}
