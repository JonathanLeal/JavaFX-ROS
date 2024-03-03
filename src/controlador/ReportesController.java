/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dao.ReporteDao;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modelo.Reporte;

/**
 * FXML Controller class
 *
 * @author Roberto
 */
public class ReportesController implements Initializable {
    @FXML
    private DatePicker fechaInicioPicker;
    @FXML
    private DatePicker fechaFinPicker;
    @FXML
    private TextField txtNombreDocumento;

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

    @FXML
    private void verReporte(MouseEvent event) {
        ReporteDao rDao = new ReporteDao();
        LocalDate fechaInicio = fechaInicioPicker.getValue();
        LocalDate fechaFin = fechaFinPicker.getValue();

        try {
            if (fechaInicio == null || fechaFin == null || txtNombreDocumento.getText() == null) {
                mostrarAlerta("Notificacion", "Todos los campos son requeridos", Alert.AlertType.WARNING);
                return;
            }

            int datosOptenidos = rDao.insertarDatosArchivo(fechaInicio, fechaFin, txtNombreDocumento.getText());
            if (datosOptenidos <= 0) {
                mostrarAlerta("Notificacion", "No hay ninguna transaccion entre las fechas seleccionadas", Alert.AlertType.INFORMATION);
                return;
            }
            
            // Mostrar el FileChooser para seleccionar la ubicación de descarga
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar archivo CSV");
            fileChooser.setInitialFileName("reporte.csv");
            File file = fileChooser.showSaveDialog(stage);
            
            generarArchivoCSV(fechaInicio, fechaFin);
            // Si se selecciona un archivo, copiar el archivo generado al destino
            if (file != null) {
                try {
                    Files.copy(Paths.get("reporte.csv"), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    mostrarAlerta("Notificacion", "Archivo generado con exito", Alert.AlertType.CONFIRMATION);
                } catch (IOException e) {
                    System.out.println("Error al guardar el archivo CSV: " + e.getMessage());
                    e.printStackTrace();
                }
                } else {
                    mostrarAlerta("Notificacion", "Operacion cancelada", Alert.AlertType.INFORMATION);
                }
        } catch (Exception e) {
            System.out.println("error al insertar en controller reporte: "+e.getMessage());
        }
    }

    private void generarArchivoCSV(LocalDate fechaInicio, LocalDate fechaFin) {
        ReporteDao rDao = new ReporteDao();
        String nombreArchivo = txtNombreDocumento.getText();
        ArrayList<Reporte> datos = rDao.exportarDatosCSV(fechaInicio, fechaFin);
        System.out.println("datos: "+datos);

        try (PrintWriter writer = new PrintWriter(new FileWriter("reporte.csv"))) {
            // Escribir encabezados
            writer.println("ID,Nombre Cliente,Fecha,Nombre Reporte,Fecha Inicio,Fecha Fin");

            // Escribir datos
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (Reporte reporte : datos) {
                writer.println(reporte.getId() + ","
                        + reporte.getTransaccion().getNombreCliente() + ","
                        + formatter.format(reporte.getTransaccion().getFecha().toLocalDate()) + ","
                        + reporte.getNombreDocumento() + ","
                        + formatter.format(reporte.getFechaInicio().toLocalDate()) + ","
                        + formatter.format(reporte.getFechaFin().toLocalDate()));
            }
        } catch (IOException e) {
            System.out.println("Error al generar el archivo CSV: " + e.getMessage());
            mostrarAlerta("Error", "A ocurrido un error", Alert.AlertType.ERROR);
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
