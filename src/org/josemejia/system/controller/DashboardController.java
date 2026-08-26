package org.josemejia.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.josemejia.system.model.User;
import org.josemejia.system.service.AuthService;
import org.josemejia.system.utils.AnimacionUtils;
import org.josemejia.system.utils.SessionManager;
import org.josemejia.system.utils.ViewFactory;

public class DashboardController {

    @FXML
    private BorderPane raiz;

    @FXML
    private Label lblUsuario;

    @FXML
    private Button btnProductos;

    @FXML
    private Button btnAuditoria;

    @FXML
    private Button btnCatalogo;

    @FXML
    private Button btnCarrito;

    @FXML
    private Button btnCerrarSesion;

    private final AuthService authService = new AuthService();
    private final ViewFactory viewFactory = new ViewFactory();

    @FXML
    private void initialize() {
        User usuarioActual = SessionManager.getInstanciaSessionManager().getUsuarioActual();
        lblUsuario.setText("Bienvenido, " + usuarioActual.getName());

        // 1. Limpiamos espacios e ignoramos mayúsculas/minúsculas
        String rol = (usuarioActual.getRol() != null) ? usuarioActual.getRol().trim() : "";
        boolean esAdministrador = "admin".equalsIgnoreCase(rol) || "administrador".equalsIgnoreCase(rol);

        // 2. Verificación rápida en consola
        System.out.println("¿Es admin la condición?: " + esAdministrador); // Debe dar true

        // 3. Aplicar visibilidad
        btnProductos.setVisible(esAdministrador);
        btnProductos.setManaged(esAdministrador);

        btnAuditoria.setVisible(esAdministrador);
        btnAuditoria.setManaged(esAdministrador);

        btnCatalogo.setVisible(!esAdministrador);
        btnCatalogo.setManaged(!esAdministrador);

        btnCarrito.setVisible(!esAdministrador);
        btnCarrito.setManaged(!esAdministrador);

        // Animaciones
        AnimacionUtils.aplicarFadeIn(raiz);
        AnimacionUtils.aplicarEfectoHover(btnProductos);
        AnimacionUtils.aplicarEfectoHover(btnAuditoria);
        AnimacionUtils.aplicarEfectoHover(btnCatalogo);
        AnimacionUtils.aplicarEfectoHover(btnCarrito);
        AnimacionUtils.aplicarEfectoHover(btnCerrarSesion);
    }

    @FXML
    private void handleProductos() {
        viewFactory.viewProductos();
    }

    @FXML
    private void handleAuditoria() {
        viewFactory.viewAuditoria();
    }

    @FXML
    private void handleCatalogo() {
        viewFactory.viewCatalogo();
    }

    @FXML
    private void handleCarrito() {
        viewFactory.viewCarrito();
    }

    @FXML
    private void handleCerrarSesion() {
        User usuarioActual = SessionManager.getInstanciaSessionManager().getUsuarioActual();
        authService.cerrarSesion(usuarioActual.getUser());
        SessionManager.getInstanciaSessionManager().setUsuarioActual(null);
        viewFactory.viewLogin();
    }
}
