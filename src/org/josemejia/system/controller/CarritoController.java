package org.josemejia.system.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.josemejia.system.model.Compra;
import org.josemejia.system.model.DetalleCompra;
import org.josemejia.system.model.ItemCarrito;
import org.josemejia.system.model.User;
import org.josemejia.system.service.CompraService;
import org.josemejia.system.utils.AnimacionUtils;
import org.josemejia.system.utils.CarritoManager;
import org.josemejia.system.utils.SessionManager;
import org.josemejia.system.utils.ViewFactory;

public class CarritoController {

    @FXML
    private VBox raiz;

    @FXML
    private TableView<ItemCarrito> tablaCarrito;

    @FXML
    private TableColumn<ItemCarrito, String> colProducto;

    @FXML
    private TableColumn<ItemCarrito, String> colPrecioUnitario;

    @FXML
    private TableColumn<ItemCarrito, Void> colCantidad;

    @FXML
    private TableColumn<ItemCarrito, String> colSubtotal;

    @FXML
    private Label lblTotal;

    @FXML
    private Button btnConfirmar;

    @FXML
    private Button btnVolver;

    private final CompraService compraService = new CompraService();
    private final ViewFactory viewFactory = new ViewFactory();

    @FXML
    private void initialize() {
        colProducto.setCellValueFactory(datos ->
                new SimpleStringProperty(datos.getValue().getProducto().getNombre()));

        colPrecioUnitario.setCellValueFactory(datos ->
                new SimpleStringProperty(String.format("Q%.2f", datos.getValue().getProducto().getPrecio())));

        colSubtotal.setCellValueFactory(datos ->
                new SimpleStringProperty(String.format("Q%.2f", datos.getValue().getSubtotal())));

        colCantidad.setCellFactory(columna -> new TableCell<>() {
            private final Button botonMenos = new Button("-");
            private final Button botonMas = new Button("+");
            private final Label etiquetaCantidad = new Label();
            private final HBox contenedor = new HBox(8, botonMenos, etiquetaCantidad, botonMas);

            {
                contenedor.setAlignment(Pos.CENTER);
                botonMenos.getStyleClass().add("boton-cantidad");
                botonMas.getStyleClass().add("boton-cantidad");

                botonMenos.setOnAction(evento -> {
                    ItemCarrito item = getTableView().getItems().get(getIndex());
                    CarritoManager.getInstanciaCarritoManager().disminuirCantidad(item.getProducto());
                    refrescarTabla();
                });

                botonMas.setOnAction(evento -> {
                    ItemCarrito item = getTableView().getItems().get(getIndex());
                    CarritoManager.getInstanciaCarritoManager().aumentarCantidad(item.getProducto());
                    refrescarTabla();
                });
            }

            @Override
            protected void updateItem(Void valor, boolean vacio) {
                super.updateItem(valor, vacio);
                if (vacio) {
                    setGraphic(null);
                } else {
                    ItemCarrito item = getTableView().getItems().get(getIndex());
                    etiquetaCantidad.setText(String.valueOf(item.getCantidad()));
                    setGraphic(contenedor);
                }
            }
        });

        refrescarTabla();

        AnimacionUtils.aplicarFadeIn(raiz);
        AnimacionUtils.aplicarEfectoHover(btnConfirmar);
        AnimacionUtils.aplicarEfectoHover(btnVolver);
    }

    private void refrescarTabla() {
        List<ItemCarrito> items = CarritoManager.getInstanciaCarritoManager().getItems();
        ObservableList<ItemCarrito> datos = FXCollections.observableArrayList(items);
        tablaCarrito.setItems(datos);
        tablaCarrito.refresh();

        double total = CarritoManager.getInstanciaCarritoManager().calcularTotal();
        lblTotal.setText(String.format("Total: Q%.2f", total));
    }

    @FXML
    private void handleConfirmarCompra() {
        List<ItemCarrito> items = CarritoManager.getInstanciaCarritoManager().getItems();

        if (items.isEmpty()) {
            mostrarAlerta("Tu carrito está vacío.");
            return;
        }

        User usuarioActual = SessionManager.getInstanciaSessionManager().getUsuarioActual();
        double total = CarritoManager.getInstanciaCarritoManager().calcularTotal();

        Compra compra = new Compra(usuarioActual.getUser(), total);

        List<DetalleCompra> detalles = new ArrayList<>();
        for (ItemCarrito item : items) {
            detalles.add(new DetalleCompra(item.getProducto().getIdProducto(), item.getCantidad(), item.getProducto().getPrecio()));
        }

        compraService.crear(compra, detalles, usuarioActual);

        CarritoManager.getInstanciaCarritoManager().vaciar();

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Compra confirmada");
        alerta.setHeaderText(null);
        alerta.setContentText("Tu compra se registró correctamente. ¡Gracias!");
        alerta.showAndWait();

        viewFactory.viewDashboard();
    }

    @FXML
    private void handleVolver() {
        viewFactory.viewCatalogo();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Atención");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
