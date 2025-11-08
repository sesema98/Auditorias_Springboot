package com.tecsup.aopserva.domain.persistence;

import com.tecsup.aopserva.domain.entities.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface IUsuarioDao extends CrudRepository<Usuario,Integer> {
    public Usuario findByUsername(String username);
}
