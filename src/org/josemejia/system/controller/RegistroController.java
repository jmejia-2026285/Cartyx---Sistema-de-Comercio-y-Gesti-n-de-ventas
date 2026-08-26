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
import org.josemejia.system.utils.ViewFactory;
/**
 *
 * @author mejia
 */

public class RegistroController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtUsuario;

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmarPassword;

    @FXML
    private Label lblError;

    @FXML
    private Button btnRegistrarse;

    @FXML
    private Hyperlink linkVolverLogin;

    private final AuthService authService = new AuthService();
    private final ViewFactory viewFactory = new ViewFactory();

    @FXML
    private void handleRegistrarse() {
        if (camposVacios()) {
            lblError.setText("Todos los campos son obligatorios.");
            AnimacionUtils.mostrarAlertaPersonalizada(
                    "Campos obligatorios",
                    "Usuario y contraseña son obligatorios.",
                    AnimacionUtils.TipoNotificacion.ADVERTENCIA
);

            return;
        }

        if (!txtPassword.getText().equals(txtConfirmarPassword.getText())) {
            lblError.setText("Las contraseñas no coinciden.");
            AnimacionUtils.mostrarAlertaPersonalizada(
                    "Las contraseñas no coinciden.", " verifica tus credenciales",
                    AnimacionUtils.TipoNotificacion.ERROR
);

            return;
        }

        User nuevoUsuario = new User(
                txtNombre.getText(),
                txtApellidos.getText(),
                txtCorreo.getText(),
                txtPassword.getText(),
                txtUsuario.getText(),
                null
        );

        authService.registrar(nuevoUsuario);

        AnimacionUtils.mostrarAlertaPersonalizada("Registro exitoso", "Tu cuenta se creo correctamente. Ahora puedes iniciar sesion.", AnimacionUtils.TipoNotificacion.EXITO);

        viewFactory.viewLogin();
    }

    @FXML
    private void handleVolverLogin() {
        viewFactory.viewLogin();
    }

    private boolean camposVacios() {
        return txtNombre.getText().isBlank()
                || txtApellidos.getText().isBlank()
                || txtUsuario.getText().isBlank()
                || txtCorreo.getText().isBlank()
                || txtPassword.getText().isBlank()
                || txtConfirmarPassword.getText().isBlank();
    }
}