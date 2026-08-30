package org.josemejia.system.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class ImagenUtils {

    private static final Path CARPETA_IMAGENES =
            Path.of(System.getProperty("user.home"), ".cartyx", "productos");

    private ImagenUtils() {
    }

    public static File copiarImagenAAppData(File origen) throws IOException {
        Files.createDirectories(CARPETA_IMAGENES);

        String nombreOriginal = origen.getName();
        String extension = "";
        int puntoIndex = nombreOriginal.lastIndexOf('.');
        if (puntoIndex >= 0) {
            extension = nombreOriginal.substring(puntoIndex);
        }

        String nombreDestino = UUID.randomUUID() + extension;
        Path destino = CARPETA_IMAGENES.resolve(nombreDestino);

        Files.copy(origen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

        return destino.toFile();
    }

    public static String obtenerUrlDeImagen(File archivo) {
        return archivo.toURI().toString();
    }

    public static boolean esArchivoLocalValido(String valorGuardado) {
        return obtenerArchivoLocal(valorGuardado) != null;
    }

    /**
     * Convierte lo que haya guardado en BD (una URI "file:..." o una ruta
     * cruda tipo "C:\Users\...") en una URL "file:" valida para que
     * javafx.scene.image.Image la pueda cargar.
     *
     * Antes esArchivoLocalValido() aceptaba rutas crudas (sin "file:"), pero
     * cargarImagenProducto() le pasaba esa misma ruta cruda directo a
     * new Image(url, ...), y esa clase exige una URL con esquema. Por eso
     * cargaba bien el preview (que usaba la misma ruta que guardaba
     * ImagenUtils, con "file:") pero fallaba con rutas guardadas manualmente.
     */
    public static String obtenerUrlCargable(String valorGuardado) {
        File archivo = obtenerArchivoLocal(valorGuardado);
        return archivo != null ? archivo.toURI().toString() : valorGuardado;
    }

    private static File obtenerArchivoLocal(String valorGuardado) {
        if (valorGuardado == null || valorGuardado.isBlank()) {
            return null;
        }

        try {
            File archivo = valorGuardado.startsWith("file:")
                    ? new File(new URI(valorGuardado))
                    : new File(valorGuardado);
            return archivo.exists() ? archivo : null;
        } catch (Exception e) {
            return null;
        }
    }
}
