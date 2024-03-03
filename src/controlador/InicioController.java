package controlador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dao.InicioDao;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**    

 * FXML Controller class
 *
 * @author Roberto
 */
public class InicioController implements Initializable {
    
    @FXML
    private TextField txtUsuario;
    
    @FXML
    private PasswordField txtPass;
    
    @FXML
    private Button btnIniciar;

    @FXML
    public void loguear() throws SQLException{
        InicioDao iniDao = new InicioDao();
        String usuario = txtUsuario.getText();
        String pass = txtPass.getText();
        boolean encontrado = iniDao.usuarioExistente(usuario, pass);
        int rol = iniDao.obtenerRolUsuario(usuario, pass);
        
        if (encontrado) {
            mostrarBienvenida();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Opsss...");
            alert.setContentText("Error: Credenciales incorrectas");
            alert.showAndWait();
            txtUsuario.setText("");
            txtPass.setText("");
        }
    }
    
    private void mostrarBienvenida() {
    try { 
        // Cargar la vista de Bienvenida desde el archivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Bienvenida.fxml"));
        Parent root = loader.load();

        // Crear la escena
        Scene scene = new Scene(root);

        // Obtener el escenario actual
        Stage stage = (Stage) btnIniciar.getScene().getWindow();

        // Establecer la nueva escena en el escenario
        stage.setScene(scene);
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("error al cambiarme: "+e.getMessage());
        // Manejar cualquier excepción que pueda ocurrir al cargar la vista de Bienvenida
    }
}
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
