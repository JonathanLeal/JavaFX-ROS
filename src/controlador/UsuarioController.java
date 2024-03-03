/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dao.UsuariosDao;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.Usuario;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Rol;

/**
 * FXML Controller class
 *
 * @author Roberto
 */
public class UsuarioController implements Initializable {
    @FXML
    private TableView<Usuario> tablaUsuarios;
    @FXML
    private TextField txtUsuario;
    @FXML
    private ComboBox<Rol> selecRol;
    @FXML
    private Button btnRegistrarUsuarios;
    @FXML
    private TableColumn<Usuario, Integer> colId;
    @FXML
    private TableColumn<Usuario, String> colUsuario;
    @FXML
    private TableColumn<Usuario, String> colClave;
    @FXML
    private TableColumn<Usuario, String> colRol;
    @FXML
    private PasswordField txtPass;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_usuario"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("nombre_usuario"));
        colClave.setCellValueFactory(new PropertyValueFactory<>("clave"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("nivel_acceso"));
        llenarTabla();
        llenarSelectRoles();
        
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editarMenuItem = new MenuItem("Editar");
        editarMenuItem.setOnAction(event -> handleEditarUsuario());

        MenuItem eliminarMenuItem = new MenuItem("Eliminar");
        eliminarMenuItem.setOnAction(event -> handleEliminarUsuario());

        contextMenu.getItems().addAll(editarMenuItem, eliminarMenuItem);

        tablaUsuarios.setContextMenu(contextMenu);
    }    

    @FXML
    private void vistaInicio(MouseEvent event) throws IOException {
        loadPage("/vistas/Bienvenida", event);
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
    
    public void llenarTabla() {
        UsuariosDao usuarioDAO = new UsuariosDao();
        try {
            // Obtener la lista de usuarios desde el DAO
            ArrayList<Usuario> listaUsuarios = usuarioDAO.listarUsuarios();

            // Limpiar la tabla
            tablaUsuarios.getItems().clear();

            // Agregar los datos a la tabla
            tablaUsuarios.getItems().addAll(listaUsuarios);
        } catch (SQLException e) {
            System.out.println("Errro en listarlos controller: "+e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void llenarSelectRoles(){
        UsuariosDao user = new UsuariosDao();
        try {
            ArrayList<Rol> roles = user.roles();
            selecRol.getItems().clear();
            selecRol.getItems().addAll(roles);
        } catch (Exception e) {
            System.out.println("error en listar roles controller: "+e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    public void RegistrarUsuario(){
        UsuariosDao user = new UsuariosDao();
        try {
            String usuario = txtUsuario.getText();
            String pass = txtPass.getText();
            Rol selectedRol = selecRol.getSelectionModel().getSelectedItem();
            
            if (usuario.isEmpty() || pass.isEmpty() || selectedRol == null) {
                mostrarAlerta("Campos vacios", "No puede dejar ningun campo vacio", AlertType.WARNING);
            } else {
                int idRol = selectedRol.getId_rol();
                
                if (btnRegistrarUsuarios.getText().equals("Actualizar")) {
                    Usuario selectedUsuario = tablaUsuarios.getSelectionModel().getSelectedItem();
                    
                    if (selectedUsuario != null) {
                        selectedUsuario.setNombre_usuario(usuario);
                        selectedUsuario.setClave(pass);
                        selectedUsuario.setNivel_acceso(selectedRol);
                        user.editarUsuarios(selectedUsuario);
                        mostrarAlerta("Exito", "Usuario editado con exito", AlertType.CONFIRMATION);
                        btnRegistrarUsuarios.setText("Registrar");
                    }
                } else {
                    user.insertarUsuario(usuario, pass, idRol);
                    mostrarAlerta("Exito", "Usuario ingresado correctamente", AlertType.CONFIRMATION);
                }
                txtUsuario.clear();
                txtPass.clear();
                llenarTabla();
            }
        } catch (Exception e) {
            System.out.println("error en insertar usuarios controller: "+e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Método para manejar la acción de editar usuario
    private void handleEditarUsuario() {
        // Obtener el usuario seleccionado en la TableView
        Usuario selectedUsuario = tablaUsuarios.getSelectionModel().getSelectedItem();

        // Verificar si se seleccionó un usuario
        if (selectedUsuario != null) {
            // Llenar los campos con los datos del usuario seleccionado
            txtUsuario.setText(selectedUsuario.getNombre_usuario());
            txtPass.setText(selectedUsuario.getClave());
            btnRegistrarUsuarios.setText("Actualizar");
        }
    }
    
    private void handleEliminarUsuario() {
        UsuariosDao dao = new UsuariosDao();
        Usuario selectedUsuario = tablaUsuarios.getSelectionModel().getSelectedItem();

        if (selectedUsuario != null) {
            // Mostrar una alerta de confirmación
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar eliminación");
            alert.setHeaderText("¿Estás seguro de que quieres eliminar a este usuario?");
            alert.setContentText("Esta acción no se puede deshacer.");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    dao.eliminarUsuario(selectedUsuario);
                    mostrarAlerta("Exito", "Usuario eliminado con exito", AlertType.CONFIRMATION);
                    llenarTabla();
                } catch (Exception ex) {
                    Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
