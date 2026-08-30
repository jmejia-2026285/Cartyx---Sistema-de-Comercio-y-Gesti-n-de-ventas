package org.josemejia.system.utils;

import java.util.regex.Pattern;

public class ValidationsUtils {

    private static final Pattern PATRON_CORREO =
            Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._%+-]*@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private ValidationsUtils() {
    }

    public static boolean esCorreoValido(String correo) {
        return obtenerErrorCorreo(correo) == null;
    }

    public static String obtenerErrorCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            return "El correo es obligatorio.";
        }

        String valor = correo.trim();

        if (valor.contains(" ")) {
            return "El correo no puede contener espacios.";
        }

        if (contarOcurrencias(valor, '@') != 1) {
            return "El correo debe tener una sola @.";
        }

        if (valor.contains("..")) {
            return "El correo no puede tener dos puntos seguidos.";
        }

        String[] partes = valor.split("@", -1);
        String usuario = partes[0];
        String dominio = partes[1];

        if (usuario.isEmpty() || dominio.isEmpty()) {
            return "El correo debe tener texto antes y después de la @.";
        }

        if (usuario.startsWith(".") || usuario.endsWith(".")) {
            return "El correo no puede empezar o terminar con un punto antes de la @.";
        }

        if (dominio.startsWith(".") || dominio.startsWith("-")
                || dominio.endsWith(".") || dominio.endsWith("-")) {
            return "El dominio del correo no es válido.";
        }

        if (!dominio.contains(".")) {
            return "El dominio del correo debe incluir una extensión (ejemplo: .com).";
        }

        if (!PATRON_CORREO.matcher(valor).matches()) {
            return "El formato del correo no es válido.";
        }

        return null;
    }

    private static int contarOcurrencias(String texto, char caracter) {
        int contador = 0;
        for (int i = 0; i < texto.length(); i++) {
            if (texto.charAt(i) == caracter) {
                contador++;
            }
        }
        return contador;
    }
}
