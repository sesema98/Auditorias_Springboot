package com.tecsup.aopserva.domain.persistence;

import com.tecsup.aopserva.domain.entities.Curso;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoDao extends CrudRepository<Curso, Integer> {
}
