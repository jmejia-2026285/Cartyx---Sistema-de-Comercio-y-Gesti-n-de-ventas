
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */package org.josemejia.system.repository;
 /**
 *
 * @author mejia
 */

import java.util.List;
import org.josemejia.system.model.Producto;

public interface ProductoInterface {
    void crear(Producto producto);
    List<Producto> listar();
    void actualizar(Producto producto);
    void eliminar(String idProducto);
}
