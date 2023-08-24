package com.catalisa.iSchool.repository;

import com.catalisa.iSchool.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    @Query("SELECT matricula FROM Matricula matricula WHERE matricula.aluno.email = :email")
    Optional<Matricula> findByAlunoEmail(String email);
}
