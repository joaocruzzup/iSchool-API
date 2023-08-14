package com.catalisa.iSchool.controller;

import com.catalisa.iSchool.model.dto.CursoDTO;
import com.catalisa.iSchool.model.dto.MatriculaDTO;
import com.catalisa.iSchool.service.CursoService;
import com.catalisa.iSchool.service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ischool/matriculas")
public class MatriculaController {
    @Autowired
    MatriculaService matriculaService;

    @GetMapping
    public ResponseEntity<List<MatriculaDTO>> listarTodasMatriculas(){
        return ResponseEntity.ok(matriculaService.listarTodos());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Optional<MatriculaDTO>> listarMatriculaPorId(@PathVariable Long id){
        return ResponseEntity.ok(matriculaService.listarPorId(id));
    }

    @PostMapping
    public ResponseEntity<MatriculaDTO> criarMatricula(@RequestBody MatriculaDTO matriculaDTO){
        MatriculaDTO novaMatricula = matriculaService.criar(matriculaDTO);
        return new ResponseEntity<>(novaMatricula, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<MatriculaDTO> alterarMatricula(@PathVariable Long id, @RequestBody MatriculaDTO matriculaDTO){
        MatriculaDTO novaMatricula = matriculaService.alterar(id, matriculaDTO);
        return ResponseEntity.ok(novaMatricula);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletarMatricula(@PathVariable Long id){
        matriculaService.deletar(id);
        return ResponseEntity.ok().build();
    }
}
