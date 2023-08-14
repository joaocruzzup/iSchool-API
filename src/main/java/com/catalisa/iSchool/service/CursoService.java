package com.catalisa.iSchool.service;

import com.catalisa.iSchool.enums.NomesCursos;
import com.catalisa.iSchool.exception.IdNaoEncontradoException;
import com.catalisa.iSchool.model.Aluno;
import com.catalisa.iSchool.model.Curso;
import com.catalisa.iSchool.model.dto.AlunoDTO;
import com.catalisa.iSchool.model.dto.CursoDTO;
import com.catalisa.iSchool.repository.AlunoRepository;
import com.catalisa.iSchool.repository.CursoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CursoService {


    @Autowired
    CursoRepository cursoRepository;

    public List<CursoDTO> listarTodos() {
        List<Curso> cursos = cursoRepository.findAll();
        List<CursoDTO> cursosDTOS = new ArrayList<>();
        for (Curso curso : cursos) {
            System.out.println(curso.getNome());
            CursoDTO cursoDTO = new CursoDTO();

            BeanUtils.copyProperties(curso, cursoDTO);
            System.out.println(cursoDTO.getNome());

            cursosDTOS.add(cursoDTO);
        }
        return cursosDTOS;
    }

    public Optional<CursoDTO> listarPorId(Long id) {
        Optional<Curso> curso = cursoRepository.findById(id);

        if (!curso.isPresent()) {
            throw new IdNaoEncontradoException("Curso de ID " + id + " não foi encontrado");
        }
        CursoDTO cursoDTO = new CursoDTO();
        Curso cursoEncontrado = curso.get();
        BeanUtils.copyProperties(cursoEncontrado, cursoDTO);
        return Optional.of(cursoDTO);
    }

    public CursoDTO criar(CursoDTO cursoDTO){
        Curso curso = new Curso();
        BeanUtils.copyProperties(cursoDTO, curso);
        cursoRepository.save(curso);
        return cursoDTO;
    }

    public CursoDTO alterar(Long id, CursoDTO cursoAtualizar){
        Optional<Curso> cursoOptional = cursoRepository.findById(id);
        if (!cursoOptional.isPresent()) {
            throw new IdNaoEncontradoException("Curso de ID " + id + " não foi encontrado");
        }
        Curso cursoEncontrado = cursoOptional.get();
        if (cursoAtualizar.getNome() != null){
            cursoEncontrado.setNome(String.valueOf(NomesCursos.valueOf(cursoAtualizar.getNome())));
        }
        if (cursoAtualizar.getCargaHoraria() != null){
            cursoEncontrado.setCargaHoraria(cursoAtualizar.getCargaHoraria());
        }

        cursoRepository.save(cursoEncontrado);

        CursoDTO cursoDTORetornado = new CursoDTO();
        BeanUtils.copyProperties(cursoEncontrado, cursoDTORetornado);

        return cursoDTORetornado;

    }

    public void deletar(Long id){
        Optional<Curso> curso = cursoRepository.findById(id);
        if (!curso.isPresent()) {
            throw new IdNaoEncontradoException("Curso de ID " + id + " não foi encontrado");
        }
        cursoRepository.deleteById(id);
    }
}
