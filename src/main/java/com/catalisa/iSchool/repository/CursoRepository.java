package com.catalisa.iSchool.repository;

import com.catalisa.iSchool.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    Optional<Curso> findByNome(String nome);
}
