package org.josemejia.system.service;

import java.util.List;
import org.josemejia.system.model.Auditoria;
import org.josemejia.system.model.Producto;
import org.josemejia.system.model.User;
import org.josemejia.system.repository.AuditoriaRepository;
import org.josemejia.system.repository.ProductoRepository;

public class ProductoService {

    private final ProductoRepository productoRepository = new ProductoRepository();
    private final AuditoriaRepository auditoriaRepository = new AuditoriaRepository();

    public List<Producto> listar() {
        return productoRepository.listar();
    }

    public void crear(Producto producto, User usuarioActual) {
        verificarEsAdministrador(usuarioActual);
        productoRepository.crear(producto);
        auditoriaRepository.registrar(new Auditoria(usuarioActual.getUser(), "crear_producto", "producto", "Creó: " + producto.getNombre()));
    }

    public void actualizar(Producto producto, User usuarioActual) {
        verificarEsAdministrador(usuarioActual);
        productoRepository.actualizar(producto);
        auditoriaRepository.registrar(new Auditoria(usuarioActual.getUser(), "editar_producto", "producto", "Editó: " + producto.getNombre()));
    }

    public void eliminar(Producto producto, User usuarioActual) {
        verificarEsAdministrador(usuarioActual);
        productoRepository.eliminar(producto.getIdProducto());
        auditoriaRepository.registrar(new Auditoria(usuarioActual.getUser(), "eliminar_producto", "producto", "Eliminó: " + producto.getNombre()));
    }

    private void verificarEsAdministrador(User usuarioActual) {
        if (!"admin".equals(usuarioActual.getRol())) {
            throw new IllegalStateException("Solo un administrador puede modificar productos.");
        }
    }
}
