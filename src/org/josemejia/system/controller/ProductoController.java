package org.josemejia.system.controller;

import java.io.File;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.josemejia.system.MainClass;
import org.josemejia.system.model.Producto;
import org.josemejia.system.model.User;
import org.josemejia.system.service.ProductoService;
import org.josemejia.system.utils.AnimacionUtils;
import org.josemejia.system.utils.ImagenUtils;
import org.josemejia.system.utils.SessionManager;
import org.josemejia.system.utils.ViewFactory;

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
    private ImageView imgPreview;

    @FXML
    private Button btnSeleccionarImagen;

    @FXML
    private Label lblArchivoImagen;

    @FXML
    private TextField txtUrlImagen;

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
    private String rutaImagenSeleccionada;

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
                rutaImagenSeleccionada = actual.getImagen();
                lblArchivoImagen.setText(actual.getImagen() == null || actual.getImagen().isBlank()
                        ? "Sin imagen seleccionada" : "Imagen actual del producto");
                txtUrlImagen.setText(ImagenUtils.esArchivoLocalValido(actual.getImagen()) ? "" : actual.getImagen());
                cargarImagenEnPreview(rutaImagenSeleccionada);
            }
        });

        cargarTabla();

        AnimacionUtils.aplicarFadeIn(raiz);
        AnimacionUtils.aplicarEfectoHover(btnGuardar);
        AnimacionUtils.aplicarEfectoHover(btnEliminar);
        AnimacionUtils.aplicarEfectoHover(btnLimpiar);
        AnimacionUtils.aplicarEfectoHover(btnVolver);
        AnimacionUtils.aplicarEfectoHover(btnSeleccionarImagen);
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

        try {
            if (productoSeleccionado == null) {
                Producto producto = new Producto(txtNombre.getText(), precio, txtDescripcion.getText(), txtCategoria.getText(), rutaImagenSeleccionada);
                productoService.crear(producto, usuarioActual);
            } else {
                productoSeleccionado.setNombre(txtNombre.getText());
                productoSeleccionado.setPrecio(precio);
                productoSeleccionado.setDescripcion(txtDescripcion.getText());
                productoSeleccionado.setCategoria(txtCategoria.getText());
                productoSeleccionado.setImagen(rutaImagenSeleccionada);
                productoService.actualizar(productoSeleccionado, usuarioActual);
            }
        } catch (IllegalStateException e) {
            mostrarAlerta(e.getMessage());
            return;
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

        try {
            productoService.eliminar(productoSeleccionado, usuarioActual);
        } catch (IllegalStateException e) {
            mostrarAlerta(e.getMessage());
            return;
        }

        limpiarFormulario();
        cargarTabla();
    }

    @FXML
    private void handleSeleccionarImagen() {
        FileChooser selector = new FileChooser();
        selector.setTitle("Selecciona una imagen del producto");
        selector.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));

        File archivoOrigen = selector.showOpenDialog(btnSeleccionarImagen.getScene().getWindow());
        if (archivoOrigen == null) {
            return;
        }

        try {
            File archivoCopiado = ImagenUtils.copiarImagenAAppData(archivoOrigen);
            rutaImagenSeleccionada = ImagenUtils.obtenerUrlDeImagen(archivoCopiado);
            lblArchivoImagen.setText(archivoOrigen.getName());
            cargarImagenEnPreview(rutaImagenSeleccionada);
        } catch (IOException e) {
            AnimacionUtils.mostrarAlertaPersonalizada("Error", "No se pudo cargar la imagen seleccionada.", AnimacionUtils.TipoNotificacion.ERROR);
        }
    }

    @FXML
    private void handleUrlImagen() {
        String url = txtUrlImagen.getText();
        if (url == null || url.isBlank()) {
            return;
        }
        rutaImagenSeleccionada = url.trim();
        lblArchivoImagen.setText("URL de imagen");
        cargarImagenEnPreview(rutaImagenSeleccionada);
    }

    private void cargarImagenEnPreview(String rutaImagen) {
        if (rutaImagen == null || rutaImagen.isBlank()) {
            imgPreview.setImage(null);
            return;
        }
        try {
            imgPreview.setImage(new Image(ImagenUtils.obtenerUrlCargable(rutaImagen), 64, 64, true, true));
        } catch (Exception e) {
            imgPreview.setImage(null);
        }
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
        rutaImagenSeleccionada = null;
        lblArchivoImagen.setText("Sin imagen seleccionada");
        txtUrlImagen.clear();
        imgPreview.setImage(null);
        tablaProductos.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Atención");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
        @FXML
    private javafx.scene.control.Hyperlink contactarAlAdmin;

    @FXML
    private void handleContactarAdmin() {
        MainClass.getAppHostServices().showDocument("https://github.com/AngelML-2026285");
    }

}
