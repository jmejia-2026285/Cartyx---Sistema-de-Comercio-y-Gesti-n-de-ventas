package org.josemejia.system.utils;

import java.util.ArrayList;
import java.util.List;
import org.josemejia.system.model.ItemCarrito;
import org.josemejia.system.model.Producto;

public class CarritoManager {

    private static CarritoManager instanciaCarritoManager;
    private final List<ItemCarrito> items = new ArrayList<>();

    private CarritoManager() {
    }

    public static CarritoManager getInstanciaCarritoManager() {
        if (instanciaCarritoManager == null) {
            instanciaCarritoManager = new CarritoManager();
        }
        return instanciaCarritoManager;
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    public void agregarProducto(Producto producto) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getIdProducto().equals(producto.getIdProducto())) {
                item.setCantidad(item.getCantidad() + 1);
                return;
            }
        }
        items.add(new ItemCarrito(producto, 1));
    }

    public void quitarProducto(Producto producto) {
        items.removeIf(item -> item.getProducto().getIdProducto().equals(producto.getIdProducto()));
    }

    public void aumentarCantidad(Producto producto) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getIdProducto().equals(producto.getIdProducto())) {
                item.setCantidad(item.getCantidad() + 1);
                return;
            }
        }
    }

    public void disminuirCantidad(Producto producto) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getIdProducto().equals(producto.getIdProducto())) {
                if (item.getCantidad() <= 1) {
                    items.remove(item);
                } else {
                    item.setCantidad(item.getCantidad() - 1);
                }
                return;
            }
        }
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemCarrito item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void vaciar() {
        items.clear();
    }
}
