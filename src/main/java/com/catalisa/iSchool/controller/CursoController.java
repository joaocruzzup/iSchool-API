package com.catalisa.iSchool.controller;

import com.catalisa.iSchool.model.dto.AlunoDTO;
import com.catalisa.iSchool.model.dto.CursoDTO;
import com.catalisa.iSchool.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ischool/cursos")
public class CursoController {
    @Autowired
    CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<CursoDTO>> listarTodosCursos(){
        return ResponseEntity.ok(cursoService.listarTodos());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Optional<CursoDTO>> listarCursoPorId(@PathVariable Long id){
        return ResponseEntity.ok(cursoService.listarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CursoDTO> criarCurso(@RequestBody CursoDTO cursoDTO){
        CursoDTO novoCurso = cursoService.criar(cursoDTO);
        return new ResponseEntity<>(novoCurso, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CursoDTO> alterarCurso(@PathVariable Long id, @RequestBody CursoDTO cursoDTO){
        CursoDTO novoAluno = cursoService.alterar(id, cursoDTO);
        return ResponseEntity.ok(novoAluno);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletarCurso(@PathVariable Long id){
        cursoService.deletar(id);
        return ResponseEntity.ok().build();
    }
}
