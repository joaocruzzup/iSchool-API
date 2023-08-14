package com.catalisa.iSchool.controller;

import com.catalisa.iSchool.model.Aluno;
import com.catalisa.iSchool.model.dto.AlunoDTO;
import com.catalisa.iSchool.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ischool/alunos")
public class AlunoController {
    @Autowired
    AlunoService alunoService;

    @GetMapping
    public ResponseEntity<List<AlunoDTO>> listarTodosAlunos(){
        return ResponseEntity.ok(alunoService.listarTodos());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Optional<AlunoDTO>> listarAlunoPorId(@PathVariable Long id){
        return ResponseEntity.ok(alunoService.listarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AlunoDTO> criarAluno(@RequestBody AlunoDTO alunoDTO){
        AlunoDTO novoAluno = alunoService.criar(alunoDTO);
        return new ResponseEntity<>(novoAluno, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<AlunoDTO> alterarAluno(@PathVariable Long id, @RequestBody AlunoDTO alunoDTO){
        AlunoDTO novoAluno = alunoService.alterar(id, alunoDTO);
        return ResponseEntity.ok(novoAluno);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable Long id){
        alunoService.deletar(id);
        return ResponseEntity.ok().build();
    }
}
