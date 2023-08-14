package com.catalisa.iSchool.service;

import com.catalisa.iSchool.exception.IdNaoEncontradoException;
import com.catalisa.iSchool.model.Aluno;
import com.catalisa.iSchool.model.dto.AlunoDTO;
import com.catalisa.iSchool.repository.AlunoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    AlunoRepository alunoRepository;

    public List<AlunoDTO> listarTodos() {
        List<Aluno> alunos = alunoRepository.findAll();
        List<AlunoDTO> alunosDTOS = new ArrayList<>();
        for (Aluno aluno : alunos) {
            AlunoDTO alunoDTO = new AlunoDTO();

            BeanUtils.copyProperties(aluno, alunoDTO);

            alunosDTOS.add(alunoDTO);
        }
        return alunosDTOS;
    }

    public Optional<AlunoDTO> listarPorId(Long id) {
        Optional<Aluno> aluno = alunoRepository.findById(id);

        if (!aluno.isPresent()) {
            throw new IdNaoEncontradoException("Aluno de ID " + id + " não foi encontrado");
        }
        AlunoDTO alunoDTO = new AlunoDTO();
        Aluno alunoEncontrado = aluno.get();
        BeanUtils.copyProperties(alunoEncontrado, alunoDTO);
        return Optional.of(alunoDTO);
    }

    public AlunoDTO criar(AlunoDTO alunoDTO){
        Aluno aluno = new Aluno();
        BeanUtils.copyProperties(alunoDTO, aluno);
        alunoRepository.save(aluno);
        return alunoDTO;
    }

    public AlunoDTO alterar(Long id, AlunoDTO alunoAtualizar){
        Optional<Aluno> alunoOptional = alunoRepository.findById(id);
        if (!alunoOptional.isPresent()) {
            throw new IdNaoEncontradoException("Aluno de ID " + id + " não foi encontrado");
        }
        Aluno alunoEncontrado = alunoOptional.get();
        if (alunoAtualizar.getNome() != null){
            alunoEncontrado.setNome(alunoAtualizar.getNome());
        }
        if (alunoAtualizar.getEmail() != null){
            alunoEncontrado.setEmail(alunoAtualizar.getEmail());
        }
        if (alunoAtualizar.getIdade() != null){
            alunoEncontrado.setIdade(alunoAtualizar.getIdade());
        }

        alunoRepository.save(alunoEncontrado);

        AlunoDTO alunoDTORetornado = new AlunoDTO();
        BeanUtils.copyProperties(alunoEncontrado, alunoDTORetornado);

        return alunoDTORetornado;

    }

    public void deletar(Long id){
        Optional<Aluno> aluno = alunoRepository.findById(id);
        if (!aluno.isPresent()) {
            throw new IdNaoEncontradoException("Aluno de ID " + id + " não foi encontrado");
        }
        alunoRepository.deleteById(id);
    }

}
