/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Roberto
 */
public class BitacorasController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
}
