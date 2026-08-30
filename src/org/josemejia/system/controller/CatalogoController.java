package org.josemejia.system.controller;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.josemejia.system.model.Producto;
import org.josemejia.system.service.ProductoService;
import org.josemejia.system.utils.AnimacionUtils;
import org.josemejia.system.utils.CarritoManager;
import org.josemejia.system.utils.ImagenUtils;
import org.josemejia.system.utils.ViewFactory;

public class CatalogoController {

    @FXML
    private VBox raiz;

    @FXML
    private FlowPane panelTarjetas;

    @FXML
    private Button btnVerCarrito;

    @FXML
    private Button btnVolver;

    private final ProductoService productoService = new ProductoService();
    private final ViewFactory viewFactory = new ViewFactory();

    @FXML
    private void initialize() {
        List<Producto> productos = productoService.listar();

        for (Producto producto : productos) {
            panelTarjetas.getChildren().add(crearTarjeta(producto));
        }

        actualizarContadorCarrito();

        AnimacionUtils.aplicarFadeIn(raiz);
        AnimacionUtils.aplicarEfectoHover(btnVerCarrito);
        AnimacionUtils.aplicarEfectoHover(btnVolver);
    }

        private VBox crearTarjeta(Producto producto) {
        ImageView imagenView = new ImageView();
        imagenView.setFitWidth(180);
        imagenView.setFitHeight(140);
        imagenView.setPreserveRatio(false);

        Label lblSinImagen = new Label("🖼");
        lblSinImagen.getStyleClass().add("icono-sin-imagen");

        StackPane contenedorImagen = new StackPane(lblSinImagen, imagenView);
        contenedorImagen.getStyleClass().add("contenedor-imagen");
        contenedorImagen.setPrefSize(180, 140);
        contenedorImagen.setMaxSize(180, 140);

        cargarImagenProducto(producto, imagenView);

        Label lblNombre = new Label(producto.getNombre());
        lblNombre.getStyleClass().add("nombre-producto");
        lblNombre.setWrapText(true);

        Label lblPrecio = new Label(String.format("Q%.2f", producto.getPrecio()));
        lblPrecio.getStyleClass().add("precio-producto");

        Label lblDescripcion = new Label(producto.getDescripcion());
        lblDescripcion.getStyleClass().add("descripcion-producto");
        lblDescripcion.setWrapText(true);

        Button botonAgregar = new Button("Agregar al carrito");
        botonAgregar.getStyleClass().add("boton-primario");
        botonAgregar.setMaxWidth(Double.MAX_VALUE);
        botonAgregar.setOnAction(evento -> {
            CarritoManager.getInstanciaCarritoManager().agregarProducto(producto);
            actualizarContadorCarrito();
        });
        AnimacionUtils.aplicarEfectoHover(botonAgregar);

        VBox tarjeta = new VBox(8, contenedorImagen, lblNombre, lblPrecio, lblDescripcion, botonAgregar);
        tarjeta.getStyleClass().add("tarjeta-producto");
        tarjeta.setPrefWidth(200);

        return tarjeta;
    }


    private void cargarImagenProducto(Producto producto, ImageView imagenView) {
        String valorImagen = producto.getImagen();
        if (valorImagen == null || valorImagen.isBlank()) {
            return;
        }

        if (ImagenUtils.esArchivoLocalValido(valorImagen)) {
            try {
                imagenView.setImage(new Image(ImagenUtils.obtenerUrlCargable(valorImagen), 180, 140, false, true));
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen de " + producto.getNombre() + ": " + e.getMessage());
            }
            return;
        }

        // Carga asíncrona compatible con Wikipedia y servidores protegidos (URLs remotas heredadas)
        new Thread(() -> {
            try {
                java.net.URL url = new java.net.URL(valorImagen);
                java.net.URLConnection conexion = url.openConnection();
                conexion.setConnectTimeout(8000);
                conexion.setReadTimeout(8000);

                // Engaña al servidor simulando una petición desde un navegador web
                conexion.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");

                try (java.io.InputStream stream = conexion.getInputStream()) {
                    Image imagen = new Image(stream);

                    // Modifica la interfaz gráfica de forma segura en el hilo de JavaFX
                    javafx.application.Platform.runLater(() -> imagenView.setImage(imagen));
                }
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen de " + producto.getNombre() + ": " + e.getMessage());
            }
        }).start();
    }

    private void actualizarContadorCarrito() {
        int cantidad = CarritoManager.getInstanciaCarritoManager().getItems().size();
        btnVerCarrito.setText("Ver Carrito (" + cantidad + ")");
    }

    @FXML
    private void handleVerCarrito() {
        viewFactory.viewCarrito();
    }

    @FXML
    private void handleVolver() {
        viewFactory.viewDashboard();
    }
}
