package com.catalisa.iSchool.controllerTest;

import com.catalisa.iSchool.controller.ProfessorController;
import com.catalisa.iSchool.exception.IdNaoEncontradoException;
import com.catalisa.iSchool.model.dto.CursoDTO;
import com.catalisa.iSchool.model.dto.MatriculaDTO;
import com.catalisa.iSchool.model.dto.ProfessorDTO;
import com.catalisa.iSchool.service.CursoService;
import com.catalisa.iSchool.service.ProfessorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ProfessorController.class)
public class ProfessorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfessorService professorService;

    @Test
    @DisplayName("Buscar todos os professores")
    public void testeGetTodosCursos() throws Exception {
        List<String> listaCursos = new ArrayList<>();
        listaCursos.add("Engenharia");

        ProfessorDTO professor1 = new ProfessorDTO("Joao", 23, listaCursos, new BigDecimal(3000));
        Mockito.when(professorService.listarTodos()).thenReturn(List.of(professor1));

        mockMvc.perform(get("/api/ischool/professores"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nome").value("Joao"))
                .andExpect(jsonPath("$[0].idade").value(23))
                .andExpect(jsonPath("$[0].listaCursos").value(listaCursos))
                .andExpect(jsonPath("$[0].salario").value(3000))
                .andDo(print());

        Mockito.verify(professorService, times(1)).listarTodos();

    }

    @Test
    @DisplayName("Buscar professor por ID ")
    public void testeGetMatriculaPorId() throws Exception {

        Long idProfessor = 1L;

        List<String> listaCursos = new ArrayList<>();
        listaCursos.add("Engenharia");

        ProfessorDTO professor1 = new ProfessorDTO("Joao", 23, listaCursos, new BigDecimal(3000));
        Mockito.when(professorService.listarPorId(idProfessor)).thenReturn(Optional.of(professor1));

        mockMvc.perform(get("/api/ischool/professores/{id}", idProfessor))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Joao"))
                .andExpect(jsonPath("$.idade").value(23))
                .andExpect(jsonPath("$.listaCursos").value(listaCursos))
                .andExpect(jsonPath("$.salario").value(3000))
                .andDo(print());


        Mockito.verify(professorService, times(1)).listarPorId(Mockito.any());

    }

    @Test
    @DisplayName("Cadastrar um professor")
    public void cadastrarProfessorTest() throws Exception {
        List<String> listaCursos = new ArrayList<>();
        listaCursos.add("Engenharia");

        ProfessorDTO professorDTO = new ProfessorDTO("Joao", 23, listaCursos, new BigDecimal(3000));
        when(professorService.criar(Mockito.any(professorDTO.getClass()))).thenReturn(professorDTO);

        mockMvc.perform(post("/api/ischool/professores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(professorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Joao"))
                .andExpect(jsonPath("$.idade").value(23))
                .andExpect(jsonPath("$.listaCursos").value(listaCursos))
                .andExpect(jsonPath("$.salario").value(3000))
                .andDo(print());

        Mockito.verify(professorService, times(1)).criar(Mockito.any(professorDTO.getClass()));


    }

    @Test
    @DisplayName("Alterar um professor")
    public void alterarUmProfessorTest() throws Exception{

        List<String> listaCursos = new ArrayList<>();
        listaCursos.add("Engenharia");

        ProfessorDTO professorDTO = new ProfessorDTO("Joao", 23, listaCursos, new BigDecimal(3000));
        Long idProfessor = 1L;
        when(professorService.alterar(Mockito.any(), Mockito.any(professorDTO.getClass()))).thenReturn(professorDTO);

        mockMvc.perform(put("/api/ischool/professores/{id}", idProfessor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(professorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Joao"))
                .andExpect(jsonPath("$.idade").value(23))
                .andExpect(jsonPath("$.listaCursos").value(listaCursos))
                .andExpect(jsonPath("$.salario").value(3000))
                .andDo(print());

        Mockito.verify(professorService, times(1)).alterar(Mockito.any(), Mockito.any(professorDTO.getClass()));


    }

    @Test
    @DisplayName("Deletar um professor")
    public void deletarUmProfessorTest() throws Exception{
        mockMvc.perform(delete("/api/ischool/professores/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk());
        Mockito.verify(professorService, times(1)).deletar(Mockito.any());

    }

    @Test
    @DisplayName("Deletar um professor inexistente")
    public void deletarUmProfessorInexistenteTest() throws Exception{

        doThrow(new IdNaoEncontradoException("Professor de ID 1 não foi encontrado"))
                .when(professorService).deletar(1L);

        mockMvc.perform(delete("/api/ischool/professores/{id}", 1L))
                .andDo(print())
                .andExpect(status().isNotFound());

        Mockito.verify(professorService, times(1)).deletar(Mockito.any());

    }
}
