package com.catalisa.iSchool.service;

import com.catalisa.iSchool.exception.AtributoNaoEncontradoException;
import com.catalisa.iSchool.exception.EmailJaCadastradoException;
import com.catalisa.iSchool.exception.IdNaoEncontradoException;
import com.catalisa.iSchool.model.Aluno;
import com.catalisa.iSchool.model.Curso;
import com.catalisa.iSchool.model.Matricula;
import com.catalisa.iSchool.model.dto.MatriculaDTO;
import com.catalisa.iSchool.repository.AlunoRepository;
import com.catalisa.iSchool.repository.CursoRepository;
import com.catalisa.iSchool.repository.MatriculaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatriculaService {
    @Autowired
    MatriculaRepository matriculaRepository;

    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    CursoRepository cursoRepository;

    public List<MatriculaDTO> listarTodos() {
        List<Matricula> matriculas = matriculaRepository.findAll();
        List<MatriculaDTO> matriculasDTOS = new ArrayList<>();
        for (Matricula matricula : matriculas) {
            MatriculaDTO matriculaDTO = new MatriculaDTO();

            matriculaDTO.setDataMatricula(matricula.getDataMatricula());
            matriculaDTO.setCurso(matricula.getCurso().getNome());
            matriculaDTO.setAluno(matricula.getAluno().getNome());

            matriculasDTOS.add(matriculaDTO);
        }
        return matriculasDTOS;
    }

    public Optional<MatriculaDTO> listarPorId(Long id) {
        Optional<Matricula> matricula = matriculaRepository.findById(id);

        if (!matricula.isPresent()) {
            throw new IdNaoEncontradoException("Matricula de ID " + id + " não foi encontrado");
        }
        MatriculaDTO matriculaDTO = new MatriculaDTO();
        Matricula matriculaEncontrada = matricula.get();

        matriculaDTO.setDataMatricula(matriculaEncontrada.getDataMatricula());
        matriculaDTO.setCurso(matriculaEncontrada.getCurso().getNome());
        matriculaDTO.setAluno(matriculaEncontrada.getAluno().getNome());

        return Optional.of(matriculaDTO);
    }

    public MatriculaDTO criar(MatriculaDTO matriculaDTO){
        Matricula matricula = new Matricula();
        String emailInserido = matriculaDTO.getAluno();
        String cursoInserido = matriculaDTO.getCurso();

        Aluno aluno = buscarAlunoCadastrado(emailInserido);
        Curso curso = buscarCursoCadastrado(cursoInserido);
        verificarEmailJaCadastrado(emailInserido);

        BeanUtils.copyProperties(matriculaDTO, matricula);

        matricula.setAluno(aluno);
        matricula.setCurso(curso);

        matriculaRepository.save(matricula);
        return matriculaDTO;
    }

    //ToDo corrigir a alteração das matrículas
    public MatriculaDTO alterar(Long id, MatriculaDTO matriculaDTO){
        Optional<Matricula> matriculaOptional = matriculaRepository.findById(id);

        if (!matriculaOptional.isPresent()) {
            throw new IdNaoEncontradoException("Matricula de ID " + id + " não foi encontrado");
        }

        Matricula matriculaEncontrada = matriculaOptional.get();
//        if (matriculaDTO.getCurso() != null){
//            matriculaEncontrada.setCurso(matriculaDTO.getCurso());
//        }
        if (matriculaDTO.getDataMatricula() != null){
            matriculaEncontrada.setDataMatricula(matriculaDTO.getDataMatricula());
        }

        matriculaRepository.save(matriculaEncontrada);

        MatriculaDTO matriculaDTORetornada = new MatriculaDTO();
        BeanUtils.copyProperties(matriculaEncontrada, matriculaDTORetornada);

        return matriculaDTORetornada;

    }

    public void deletar(Long id){
        Optional<Matricula> curso = matriculaRepository.findById(id);
        if (!curso.isPresent()) {
            throw new IdNaoEncontradoException("Matricula de ID " + id + " não foi encontrado");
        }
        matriculaRepository.deleteById(id);
    }

    public void verificarEmailJaCadastrado(String email){
        Optional<Matricula> matriculaBuscada = matriculaRepository.findByAlunoEmail(email);
        if (matriculaBuscada.isPresent()){
            throw new EmailJaCadastradoException("Matricula com E-mail " + email + " já está cadastrada");
        }
    }

    public Aluno buscarAlunoCadastrado(String email){
        Optional<Aluno> alunoBuscado = alunoRepository.findByEmail(email);
        if (!alunoBuscado.isPresent()){
            throw new AtributoNaoEncontradoException("Aluno de E-mail " + email + " não foi encontrado");
        }
        return alunoBuscado.get();
    }

    public Curso buscarCursoCadastrado(String nomeCurso){
        Optional<Curso> cursoBuscado = cursoRepository.findByNome(nomeCurso);
        if (!cursoBuscado.isPresent()){
            throw new AtributoNaoEncontradoException("Curso de nome: " + nomeCurso + " não foi encontrado");
        }
        return cursoBuscado.get();
    }
}
