package com.catalisa.iSchool.service;

import com.catalisa.iSchool.exception.AtributoNaoEncontradoException;
import com.catalisa.iSchool.exception.EmailJaCadastradoException;
import com.catalisa.iSchool.exception.IdNaoEncontradoException;
import com.catalisa.iSchool.model.Aluno;
import com.catalisa.iSchool.model.Curso;
import com.catalisa.iSchool.model.Matricula;
import com.catalisa.iSchool.model.Professor;
import com.catalisa.iSchool.model.dto.CursoDTO;
import com.catalisa.iSchool.model.dto.MatriculaDTO;
import com.catalisa.iSchool.model.dto.ProfessorDTO;
import com.catalisa.iSchool.repository.CursoRepository;
import com.catalisa.iSchool.repository.ProfessorRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfessorService {
    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    CursoRepository cursoRepository;

    public List<ProfessorDTO> listarTodos() {
        List<Professor> professores = professorRepository.findAll();
        List<ProfessorDTO> professoresDTOS = new ArrayList<>();
        for (Professor professor : professores) {
            ProfessorDTO professorDTO = new ProfessorDTO();

            professorDTO.setIdade(professor.getIdade());
            professorDTO.setNome(professor.getNome());
            professorDTO.setSalario(professor.getSalario());

            List<String> listaNomesCursos = professor.getCursos().stream()
                    .map(Curso::getNome)
                    .collect(Collectors.toList());
            professorDTO.setListaCursos(listaNomesCursos);


            professoresDTOS.add(professorDTO);
        }
        return professoresDTOS;
    }

    public Optional<ProfessorDTO> listarPorId(Long id) {
        Optional<Professor> professor = professorRepository.findById(id);

        if (!professor.isPresent()) {
            throw new IdNaoEncontradoException("Professor de ID " + id + " não foi encontrado");
        }
        ProfessorDTO professorDTO = new ProfessorDTO();
        Professor professorEncontrado = professor.get();
        BeanUtils.copyProperties(professorEncontrado, professorDTO);
        List<String> listaNomesCursos = professor.get().getCursos().stream()
                .map(Curso::getNome)
                .collect(Collectors.toList());
        professorDTO.setListaCursos(listaNomesCursos);
        return Optional.of(professorDTO);
    }

    public ProfessorDTO criar(ProfessorDTO professorDTO){
        Professor professor = new Professor();
        List<String> cursosEnviados = professorDTO.getListaCursos();

        List<Curso> listaCursosCadastrados = cursoRepository.findAll();

        List<Curso> cursoAInserir = new ArrayList<>();


        for (String cursoEnviado : cursosEnviados) {
            boolean cursoEncontrado = false;

            for (Curso cursoCadastrado : listaCursosCadastrados) {
                if (cursoEnviado.equals(cursoCadastrado.getNome())) {
                    cursoEncontrado = true;
                    cursoAInserir.add(cursoCadastrado);
                    break;
                }
            }

            if (!cursoEncontrado) {
                throw new AtributoNaoEncontradoException("O curso " + cursoEnviado + " não está disponível");
            }
        }

        BeanUtils.copyProperties(professorDTO, professor);
        professor.setCursos(cursoAInserir);

        professorRepository.save(professor);
        return professorDTO;
    }

    //ToDo Melhorar o reuso do código
    public ProfessorDTO alterar(Long id, ProfessorDTO professorDTO){
        Optional<Professor> professorOptional = professorRepository.findById(id);

        if (!professorOptional.isPresent()) {
            throw new IdNaoEncontradoException("Professor de ID " + id + " não foi encontrado");
        }

        Professor professorEncontrado = professorOptional.get();

        if (professorDTO.getIdade() != null){
            professorEncontrado.setIdade(professorDTO.getIdade());
        }
        if (professorDTO.getSalario() != null){
            professorEncontrado.setSalario(professorDTO.getSalario());
        }
        if (professorDTO.getNome() != null){
            professorEncontrado.setNome(professorDTO.getNome());
        }

        //ToDo refatorar essa lógica
        if (professorDTO.getListaCursos() != null){
            List<String> cursosEnviados = professorDTO.getListaCursos();

            List<Curso> listaCursosCadastrados = cursoRepository.findAll();

            List<Curso> cursoAInserir = new ArrayList<>();


            for (String cursoEnviado : cursosEnviados) {
                boolean cursoEncontrado = false;

                for (Curso cursoCadastrado : listaCursosCadastrados) {
                    if (cursoEnviado.equals(cursoCadastrado.getNome())) {
                        cursoEncontrado = true;
                        cursoAInserir.add(cursoCadastrado);
                        break;
                    }
                }

                if (!cursoEncontrado) {
                    throw new AtributoNaoEncontradoException("O curso " + cursoEnviado + " não está disponível");
                }
            }

            professorEncontrado.setCursos(cursoAInserir);
        }


        professorRepository.save(professorEncontrado);

        ProfessorDTO professorDTORetornado = new ProfessorDTO();
        BeanUtils.copyProperties(professorEncontrado, professorDTORetornado);

        //ToDo refatorar essa logica
        List<String> listaNomesCursos = new ArrayList<>();
        for (Curso curso : professorEncontrado.getCursos()) {
            listaNomesCursos.add(curso.getNome());
        }

        professorDTORetornado.setListaCursos(listaNomesCursos);


        return professorDTORetornado;
    }

    public void deletar(Long id){
        Optional<Professor> professor = professorRepository.findById(id);
        if (!professor.isPresent()) {
            throw new IdNaoEncontradoException("Professor de ID " + id + " não foi encontrado");
        }
        professorRepository.deleteById(id);
    }



}
