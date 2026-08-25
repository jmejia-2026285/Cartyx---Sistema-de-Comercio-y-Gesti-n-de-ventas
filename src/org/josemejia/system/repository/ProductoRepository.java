package org.josemejia.system.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.josemejia.system.config.ConexionDB;
import org.josemejia.system.model.Producto;

public class ProductoRepository implements ProductoInterface {

    private static final String SP_CREAR = "{call sp_producto_crear(?, ?, ?, ?, ?)}";

    private static final String SP_LISTAR = "{call sp_producto_listar()}";

    private static final String SP_ACTUALIZAR = "{call sp_producto_actualizar(?, ?, ?, ?, ?, ?)}";

    private static final String SP_ELIMINAR = "{call sp_producto_eliminar(?)}";

    @Override
    public void crear(Producto producto) {
        try (Connection conexion = ConexionDB.getInstanciaConexionDB().getConnection();
             CallableStatement sentencia = conexion.prepareCall(SP_CREAR)) {

            sentencia.setString(1, producto.getNombre());
            sentencia.setDouble(2, producto.getPrecio());
            sentencia.setString(3, producto.getDescripcion());
            sentencia.setString(4, producto.getCategoria());
            sentencia.setString(5, producto.getImagen());

            sentencia.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo crear el producto.", e);
        }
    }

    @Override
    public List<Producto> listar() {
        List<Producto> productos = new ArrayList<>();

        try (Connection conexion = ConexionDB.getInstanciaConexionDB().getConnection();
             CallableStatement sentencia = conexion.prepareCall(SP_LISTAR);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                productos.add(new Producto(
                        resultado.getString("id_producto"),
                        resultado.getString("nombre"),
                        resultado.getDouble("precio"),
                        resultado.getString("descripcion"),
                        resultado.getString("categoria"),
                        resultado.getString("imagen")
                ));
            }

        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron listar los productos.", e);
        }

        return productos;
    }

    @Override
    public void actualizar(Producto producto) {
        try (Connection conexion = ConexionDB.getInstanciaConexionDB().getConnection();
             CallableStatement sentencia = conexion.prepareCall(SP_ACTUALIZAR)) {

            sentencia.setString(1, producto.getIdProducto());
            sentencia.setString(2, producto.getNombre());
            sentencia.setDouble(3, producto.getPrecio());
            sentencia.setString(4, producto.getDescripcion());
            sentencia.setString(5, producto.getCategoria());
            sentencia.setString(6, producto.getImagen());

            sentencia.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo actualizar el producto.", e);
        }
    }

    @Override
    public void eliminar(String idProducto) {
        try (Connection conexion = ConexionDB.getInstanciaConexionDB().getConnection();
             CallableStatement sentencia = conexion.prepareCall(SP_ELIMINAR)) {

            sentencia.setString(1, idProducto);
            sentencia.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo eliminar el producto.", e);
        }
    }
}
