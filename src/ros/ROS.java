/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ros;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Roberto
 */
public class ROS extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Cargar la vista Inicio desde el archivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Inicio.fxml"));
        Parent root = loader.load();

        // Configurar la escena
        Scene scene = new Scene(root);

        // Configurar el escenario (Stage)
        primaryStage.setTitle("Mi Aplicación JavaFX");
        primaryStage.setScene(scene);

        // Mostrar el escenario
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
