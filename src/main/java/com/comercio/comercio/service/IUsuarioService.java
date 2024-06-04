package com.comercio.comercio.service;

import com.comercio.comercio.model.Usuario;

import java.util.Optional;


public interface IUsuarioService {
    Optional<Usuario> findById(Integer id);
    Usuario save (Usuario usuario);
    Optional<Usuario> findByEmail(String email);

}

