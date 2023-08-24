package com.catalisa.iSchool.controllerTest;

import com.catalisa.iSchool.controller.MatriculaController;
import com.catalisa.iSchool.exception.IdNaoEncontradoException;
import com.catalisa.iSchool.model.dto.CursoDTO;
import com.catalisa.iSchool.model.dto.MatriculaDTO;
import com.catalisa.iSchool.service.MatriculaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(MatriculaController.class)
public class MatriculaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MatriculaService matriculaService;

    @Test
    @DisplayName("Buscar todos as matriculas usando MockMVC")
    public void testeGetTodasMatriculas() throws Exception {

        // Criando uma lista de alunos para o alunoService retornar
        MatriculaDTO matricula1 = new MatriculaDTO(LocalDate.parse("2023-08-20"), "Joao", "Engenharia");
        Mockito.when(matriculaService.listarTodos()).thenReturn(List.of(matricula1));

        // Configurando o MockMVC
        mockMvc.perform(get("/api/ischool/matriculas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].dataMatricula").value("2023-08-20"))
                .andExpect(jsonPath("$[0].aluno").value("Joao"))
                .andExpect(jsonPath("$[0].curso").value("Engenharia"))
                .andDo(print());


        // Verificando se o método listarTodos do alunoService foi chamado uma vez
        Mockito.verify(matriculaService, times(1)).listarTodos();

    }

    @Test
    @DisplayName("Buscar matricula por ID ")
    public void testeGetMatriculaPorId() throws Exception {

        Long idMatricula = 1L;
        MatriculaDTO matricula1 = new MatriculaDTO(LocalDate.parse("2023-08-20"), "Joao", "Engenharia");
        Mockito.when(matriculaService.listarPorId(idMatricula)).thenReturn(Optional.of(matricula1));

        // Configurando o MockMVC
        mockMvc.perform(get("/api/ischool/matriculas/{id}", idMatricula))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dataMatricula").value("2023-08-20"))
                .andExpect(jsonPath("$.aluno").value("Joao"))
                .andExpect(jsonPath("$.curso").value("Engenharia"))
                .andDo(print());


        // Verificando se o método listarTodos do alunoService foi chamado uma vez
        Mockito.verify(matriculaService, times(1)).listarPorId(Mockito.any());

    }

    @Test
    @DisplayName("Cadastrar uma matrícula")
    public void cadastrarMatriculasTest() throws Exception {

        MatriculaDTO matriculaDTO = new MatriculaDTO(LocalDate.parse("2023-08-20"), "Joao", "Engenharia");
        when(matriculaService.criar(Mockito.any(matriculaDTO.getClass()))).thenReturn(matriculaDTO);

        mockMvc.perform(post("/api/ischool/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(matriculaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dataMatricula").value("2023-08-20"))
                .andExpect(jsonPath("$.aluno").value("Joao"))
                .andExpect(jsonPath("$.curso").value("Engenharia"))
                .andDo(print());

        Mockito.verify(matriculaService, times(1)).criar(Mockito.any(matriculaDTO.getClass()));


    }

    @Test
    @DisplayName("Alterar uma matrícula")
    public void alterarUmaMatriculaTest() throws Exception{

        MatriculaDTO matriculaDTO = new MatriculaDTO(LocalDate.parse("2023-08-20"), "Joao", "Engenharia");
        Long idMatricula = 1L;
        when(matriculaService.alterar(Mockito.any(), Mockito.any(matriculaDTO.getClass()))).thenReturn(matriculaDTO);

        mockMvc.perform(put("/api/ischool/matriculas/{id}", idMatricula)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(matriculaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataMatricula").value("2023-08-20"))
                .andExpect(jsonPath("$.aluno").value("Joao"))
                .andExpect(jsonPath("$.curso").value("Engenharia"))
                .andDo(print());

        Mockito.verify(matriculaService, times(1)).alterar(Mockito.any(), Mockito.any(matriculaDTO.getClass()));


    }

    @Test
    @DisplayName("Deletar uma matrícula")
    public void deletarUmaMatriculaTest() throws Exception{
        mockMvc.perform(delete("/api/ischool/matriculas/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(matriculaService, times(1)).deletar(Mockito.any());
    }

    @Test
    @DisplayName("Deletar uma matrícula inexistente")
    public void deletarUmaMatriculaInexistenteTest() throws Exception{

        doThrow(new IdNaoEncontradoException("Matricula de ID 1 não foi encontrado"))
                .when(matriculaService).deletar(1L);

        mockMvc.perform(delete("/api/ischool/matriculas/{id}", 1L))
                .andDo(print())
                .andExpect(status().isNotFound());

        Mockito.verify(matriculaService, times(1)).deletar(Mockito.any());
    }
}
