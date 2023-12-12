package com.example.redesocial.repository;

import com.example.redesocial.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostagemRepository extends JpaRepository<Postagem, Integer> {

    List<Postagem> findByUsuario(String usuario); //Método responsável por buscar as postagen pelo usuário.
}
