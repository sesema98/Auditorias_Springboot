package com.tecsup.aopserva.domain.persistence;

import com.tecsup.aopserva.domain.entities.Auditoria;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaDao extends CrudRepository<Auditoria, Integer> {
}
