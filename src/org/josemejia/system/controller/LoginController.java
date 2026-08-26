/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.josemejia.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.josemejia.system.model.User;
import org.josemejia.system.service.AuthService;
import org.josemejia.system.utils.AnimacionUtils;
import org.josemejia.system.utils.SessionManager;
import org.josemejia.system.utils.ViewFactory;
/**
 *
 * @author mejia
 */

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblError;

    @FXML
    private Button btnLogin;

    @FXML
    private Hyperlink linkRegistro;

    private final AuthService authService = new AuthService();
    private final ViewFactory viewFactory = new ViewFactory();

    @FXML
    private void handleLogin() {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        if (usuario == null || usuario.isBlank() || password == null || password.isBlank()) {
            lblError.setText("Usuario y contraseña son obligatorios.");
            AnimacionUtils.mostrarAlertaPersonalizada(
                    "Campos obligatorios",
                    "Usuario y contraseña son obligatorios.",
                    AnimacionUtils.TipoNotificacion.ADVERTENCIA
            );

            return;
        }

        User user = authService.login(usuario, password);

        if (user == null) {
            lblError.setText("Usuario o contraseña incorrectos.");
            AnimacionUtils.mostrarAlertaPersonalizada(
                    "Error de inicio de sesión",
                    "El usuario o la contraseña son incorrectos.",
                    AnimacionUtils.TipoNotificacion.ERROR
);

            return;
        }

        SessionManager.getInstanciaSessionManager().setUsuarioActual(user);
        viewFactory.viewDashboard();
    }

    @FXML
    private void handleIrARegistro() {
        viewFactory.viewRegistro();
    }
}
