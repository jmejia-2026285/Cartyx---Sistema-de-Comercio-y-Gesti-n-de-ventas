package org.josemejia.system.repository;

import java.util.List;
import org.josemejia.system.model.Producto;

public interface ProductoInterface {
    void crear(Producto producto);
    List<Producto> listar();
    void actualizar(Producto producto);
    void eliminar(String idProducto);
}
