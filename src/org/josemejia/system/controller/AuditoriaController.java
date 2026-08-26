package org.josemejia.system.controller;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.josemejia.system.model.Auditoria;
import org.josemejia.system.model.User;
import org.josemejia.system.service.AuditoriaService;
import org.josemejia.system.utils.AnimacionUtils;
import org.josemejia.system.utils.SessionManager;
import org.josemejia.system.utils.ViewFactory;

public class AuditoriaController {

    @FXML
    private VBox raiz;

    @FXML
    private TableView<Auditoria> tablaAuditoria;

    @FXML
    private TableColumn<Auditoria, String> colUsuario;

    @FXML
    private TableColumn<Auditoria, String> colAccion;

    @FXML
    private TableColumn<Auditoria, String> colEntidad;

    @FXML
    private TableColumn<Auditoria, String> colDetalle;

    @FXML
    private TableColumn<Auditoria, String> colFecha;

    @FXML
    private Button btnVolver;

    private final AuditoriaService auditoriaService = new AuditoriaService();
    private final ViewFactory viewFactory = new ViewFactory();

    @FXML
    private void initialize() {
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colAccion.setCellValueFactory(new PropertyValueFactory<>("accion"));
        colEntidad.setCellValueFactory(new PropertyValueFactory<>("entidad"));
        colDetalle.setCellValueFactory(new PropertyValueFactory<>("detalle"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        tablaAuditoria.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        User usuarioActual = SessionManager.getInstanciaSessionManager().getUsuarioActual();

        try {
            List<Auditoria> registros = auditoriaService.listar(usuarioActual);
            ObservableList<Auditoria> datos = FXCollections.observableArrayList(registros);
            tablaAuditoria.setItems(datos);
        } catch (IllegalStateException e) {
            mostrarAlerta(e.getMessage());
            viewFactory.viewDashboard();
            return;
        }

        AnimacionUtils.aplicarFadeIn(raiz);
        AnimacionUtils.aplicarEfectoHover(btnVolver);
    }

    @FXML
    private void handleVolver() {
        viewFactory.viewDashboard();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Acceso restringido");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}