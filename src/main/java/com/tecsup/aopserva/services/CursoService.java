package com.tecsup.aopserva.services;

import com.tecsup.aopserva.domain.entities.Curso;
import java.util.List;

public interface CursoService {

    void grabar(Curso curso);
    void eliminar(int id);
    Curso buscar(Integer id);
    List<Curso> listar();
}
