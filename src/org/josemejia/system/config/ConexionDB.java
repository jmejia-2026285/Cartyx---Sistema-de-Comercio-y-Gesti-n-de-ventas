package org.josemejia.system.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionDB {

    private static ConexionDB instanciaConexionDB;

    private ConexionDB() {
    }

    public static ConexionDB getInstanciaConexionDB() {
        if (instanciaConexionDB == null) {
            instanciaConexionDB = new ConexionDB();
        }
        return instanciaConexionDB;
    }

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://" + Enviroment.LOCATION_SERVICE + "/" + Enviroment.DATA_BASE,
                    Enviroment.USER,
                    Enviroment.PASSWORD);
        } catch (ClassNotFoundException e) {
            //aqui use IllegalStateException porque SQLException no detenia la aplicacion
            throw new IllegalStateException("No se encontró el driver de MySQL .", e);
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo conectar a la base de datos.", e);
        }
    }
}
