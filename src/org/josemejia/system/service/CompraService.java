package org.josemejia.system.service;

import java.util.List;
import org.josemejia.system.model.Auditoria;
import org.josemejia.system.model.Compra;
import org.josemejia.system.model.DetalleCompra;
import org.josemejia.system.model.User;
import org.josemejia.system.repository.AuditoriaRepository;
import org.josemejia.system.repository.CompraRepository;

public class CompraService {

    private final CompraRepository compraRepository = new CompraRepository();
    private final AuditoriaRepository auditoriaRepository = new AuditoriaRepository();

    public void crear(Compra compra, List<DetalleCompra> detalles, User usuarioActual) {
        compraRepository.crear(compra, detalles);
        auditoriaRepository.registrar(new Auditoria(
                usuarioActual.getUser(),
                "confirmar_compra",
                "compra",
                "Compró " + detalles.size() + " producto(s) por un total de Q" + String.format("%.2f", compra.getTotal())
        ));
    }
}
