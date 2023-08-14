package com.catalisa.iSchool.controller;

import com.catalisa.iSchool.model.dto.MatriculaDTO;
import com.catalisa.iSchool.model.dto.ProfessorDTO;
import com.catalisa.iSchool.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ischool/professores")
public class ProfessorController {
    @Autowired
    ProfessorService professorService;

    @GetMapping
    public ResponseEntity<List<ProfessorDTO>> listarTodosProfessores(){
        return ResponseEntity.ok(professorService.listarTodos());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Optional<ProfessorDTO>> listarProfessorPorId(@PathVariable Long id){
        return ResponseEntity.ok(professorService.listarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProfessorDTO> criarProfessor(@RequestBody ProfessorDTO professorDTO){
        ProfessorDTO novoProfessor = professorService.criar(professorDTO);
        return new ResponseEntity<>(novoProfessor, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ProfessorDTO> alterarProfessor(@PathVariable Long id, @RequestBody ProfessorDTO professorDTO){
        ProfessorDTO novoProfessor = professorService.alterar(id, professorDTO);
        return ResponseEntity.ok(novoProfessor);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletarProfessor(@PathVariable Long id){
        professorService.deletar(id);
        return ResponseEntity.ok().build();
    }


}
