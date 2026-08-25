/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.josemejia.system.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.josemejia.system.MainClass;
import org.josemejia.system.model.Producto;
import org.josemejia.system.model.User;
import org.josemejia.system.service.ProductoService;
import org.josemejia.system.utils.AnimacionUtils;
import org.josemejia.system.utils.SessionManager;
import org.josemejia.system.utils.ViewFactory;
/**
 *
 * @author mejia
 */

public class ProductoController {

    @FXML
    private VBox raiz;

    @FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, Double> colPrecio;

    @FXML
    private TableColumn<Producto, String> colDescripcion;

    @FXML
    private TableColumn<Producto, String> colCategoria;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtDescripcion;

    @FXML
    private TextField txtCategoria;

    @FXML
    private TextField txtImagen;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnVolver;

    private final ProductoService productoService = new ProductoService();
    private final ViewFactory viewFactory = new ViewFactory();

    private Producto productoSeleccionado;

    @FXML
    private void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        tablaProductos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tablaProductos.getSelectionModel().selectedItemProperty().addListener((observable, anterior, actual) -> {
            productoSeleccionado = actual;
            if (actual != null) {
                txtNombre.setText(actual.getNombre());
                txtPrecio.setText(String.valueOf(actual.getPrecio()));
                txtDescripcion.setText(actual.getDescripcion());
                txtCategoria.setText(actual.getCategoria());
                txtImagen.setText(actual.getImagen());
            }
        });

        cargarTabla();

        AnimacionUtils.aplicarFadeIn(raiz);
        AnimacionUtils.aplicarEfectoHover(btnGuardar);
        AnimacionUtils.aplicarEfectoHover(btnEliminar);
        AnimacionUtils.aplicarEfectoHover(btnLimpiar);
        AnimacionUtils.aplicarEfectoHover(btnVolver);
    }

    private void cargarTabla() {
        ObservableList<Producto> productos = FXCollections.observableArrayList(productoService.listar());
        tablaProductos.setItems(productos);
    }

    @FXML
    private void handleGuardar() {
        if (txtNombre.getText().isBlank() || txtPrecio.getText().isBlank()) {
            mostrarAlerta("El nombre y el precio son obligatorios.");
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(txtPrecio.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("El precio debe ser un número.");
            return;
        }

        User usuarioActual = SessionManager.getInstanciaSessionManager().getUsuarioActual();

        if (productoSeleccionado == null) {
            Producto producto = new Producto(txtNombre.getText(), precio, txtDescripcion.getText(), txtCategoria.getText(), txtImagen.getText());
            productoService.crear(producto, usuarioActual);
        } else {
            productoSeleccionado.setNombre(txtNombre.getText());
            productoSeleccionado.setPrecio(precio);
            productoSeleccionado.setDescripcion(txtDescripcion.getText());
            productoSeleccionado.setCategoria(txtCategoria.getText());
            productoSeleccionado.setImagen(txtImagen.getText());
            productoService.actualizar(productoSeleccionado, usuarioActual);
        }

        limpiarFormulario();
        cargarTabla();
    }

    @FXML
    private void handleEliminar() {
        if (productoSeleccionado == null) {
            mostrarAlerta("Selecciona un producto de la tabla primero.");
            return;
        }

        User usuarioActual = SessionManager.getInstanciaSessionManager().getUsuarioActual();
        productoService.eliminar(productoSeleccionado, usuarioActual);
        limpiarFormulario();
        cargarTabla();
    }

    @FXML
    private void handleLimpiar() {
        limpiarFormulario();
    }

    @FXML
    private void handleVolver() {
        viewFactory.viewDashboard();
    }

    private void limpiarFormulario() {
        productoSeleccionado = null;
        txtNombre.clear();
        txtPrecio.clear();
        txtDescripcion.clear();
        txtCategoria.clear();
        txtImagen.clear();
        tablaProductos.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String mensaje) {
        AnimacionUtils.mostrarAlertaPersonalizada("Atención", mensaje, AnimacionUtils.TipoNotificacion.ADVERTENCIA);
    }
    
        @FXML
    private javafx.scene.control.Hyperlink contactarAlAdmin;

    @FXML
    private void handleContactarAdmin() {
        MainClass.getAppHostServices().showDocument("https://github.com/jmejia-2026285");
    }

}