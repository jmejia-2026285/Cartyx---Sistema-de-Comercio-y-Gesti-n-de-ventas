package org.josemejia.system.service;

import java.util.List;
import org.josemejia.system.model.Auditoria;
import org.josemejia.system.model.User;
import org.josemejia.system.repository.AuditoriaRepository;

public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository = new AuditoriaRepository();

    public List<Auditoria> listar(User usuarioActual) {
        if (!"admin".equals(usuarioActual.getRol())) {
            throw new IllegalStateException("Solo un administrador puede ver la auditoría.");
        }
        return auditoriaRepository.listar();
    }
}
