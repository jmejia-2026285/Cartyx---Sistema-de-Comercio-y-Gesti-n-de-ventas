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
        if (valorGuardado == null || valorGuardado.isBlank()) {
            return false;
        }

        try {
            if (valorGuardado.startsWith("file:")) {
                File archivo = new File(new URI(valorGuardado));
                return archivo.exists();
            }
            return new File(valorGuardado).exists();
        } catch (Exception e) {
            return false;
        }
    }
}
