/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author mejia
 */

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
    private Button btnCerrarSesion;
            

    private final AuthService authService = new AuthService();
    private final ViewFactory viewFactory = new ViewFactory();

    @FXML
    private void initialize() {
        User usuarioActual = SessionManager.getInstanciaSessionManager().getUsuarioActual();
        lblUsuario.setText("Bienvenido, " + usuarioActual.getName());

        boolean esAdministrador = "administrador".equals(usuarioActual.getRol());
        btnAuditoria.setVisible(esAdministrador);
        btnAuditoria.setManaged(esAdministrador);

        AnimacionUtils.aplicarFadeIn(raiz);
        AnimacionUtils.aplicarEfectoHover(btnProductos);
        AnimacionUtils.aplicarEfectoHover(btnAuditoria);
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
    private void handleCerrarSesion() {
        User usuarioActual = SessionManager.getInstanciaSessionManager().getUsuarioActual();
        authService.cerrarSesion(usuarioActual.getUser());
        SessionManager.getInstanciaSessionManager().setUsuarioActual(null);
        viewFactory.viewLogin();
    }
}