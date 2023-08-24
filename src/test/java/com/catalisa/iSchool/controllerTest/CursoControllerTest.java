package com.catalisa.iSchool.controllerTest;

import com.catalisa.iSchool.controller.CursoController;
import com.catalisa.iSchool.exception.IdNaoEncontradoException;
import com.catalisa.iSchool.model.dto.AlunoDTO;
import com.catalisa.iSchool.model.dto.CursoDTO;
import com.catalisa.iSchool.service.CursoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CursoController.class)
public class CursoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CursoService cursoService;

    @Test
    @DisplayName("Buscar todos os cursos")
    public void testeGetTodosCursos() throws Exception {

        CursoDTO curso1 = new CursoDTO("Engenharia", 30);
        Mockito.when(cursoService.listarTodos()).thenReturn(List.of(curso1));

        mockMvc.perform(get("/api/ischool/cursos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nome").value("Engenharia"))
                .andExpect(jsonPath("$[0].cargaHoraria").value("30"))
                .andDo(print());

        Mockito.verify(cursoService, times(1)).listarTodos();

    }

    @Test
    @DisplayName("Buscar curso por ID ")
    public void testeGetCursoPorId() throws Exception {

        Long idCurso = 1L;
        CursoDTO curso1 = new CursoDTO("Engenharia", 30);
        Mockito.when(cursoService.listarPorId(idCurso)).thenReturn(Optional.of(curso1));

        // Configurando o MockMVC
        mockMvc.perform(get("/api/ischool/cursos/{id}", idCurso))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Engenharia"))
                .andExpect(jsonPath("$.cargaHoraria").value("30"))
                .andDo(print());

        // Verificando se o método listarTodos do alunoService foi chamado uma vez
        Mockito.verify(cursoService, times(1)).listarPorId(Mockito.any());

    }

    @Test
    @DisplayName("Cadastrar um curso")
    public void cadastrarCursosTest() throws Exception {

        CursoDTO cursoDTO = new CursoDTO("Engenharia", 30);
        when(cursoService.criar(Mockito.any(cursoDTO.getClass()))).thenReturn(cursoDTO);

        mockMvc.perform(post("/api/ischool/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Engenharia"))
                .andExpect(jsonPath("$.cargaHoraria").value("30"));

        Mockito.verify(cursoService, times(1)).criar(Mockito.any(cursoDTO.getClass()));

    }

    @Test
    @DisplayName("Alterar um curso")
    public void alterarUmCursoTest() throws Exception{

        CursoDTO cursoDTO = new CursoDTO("Engenharia", 30);
        Long idCurso = 1L;
        when(cursoService.alterar(Mockito.any(), Mockito.any(cursoDTO.getClass()))).thenReturn(cursoDTO);

        mockMvc.perform(put("/api/ischool/cursos/{id}", idCurso)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Engenharia"))
                .andExpect(jsonPath("$.cargaHoraria").value("30"));

        Mockito.verify(cursoService, times(1)).alterar(Mockito.any(), Mockito.any(cursoDTO.getClass()));


    }

    @Test
    @DisplayName("Deletar um curso")
    public void deletarUmCursoTest() throws Exception{
        mockMvc.perform(delete("/api/ischool/cursos/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(cursoService, times(1)).deletar(Mockito.any());
    }

    @Test
    @DisplayName("Deletar um curso inexistente")
    public void deletarUmCursoInexistenteTest() throws Exception{

        doThrow(new IdNaoEncontradoException("Curso de ID 1 não foi encontrado"))
                .when(cursoService).deletar(1L);

        mockMvc.perform(delete("/api/ischool/cursos/{id}", 1L))
                .andDo(print())
                .andExpect(status().isNotFound());

        Mockito.verify(cursoService, times(1)).deletar(Mockito.any());
    }
}
