package com.catalisa.iSchool.controllerTest;

import com.catalisa.iSchool.controller.AlunoController;

import com.catalisa.iSchool.exception.IdNaoEncontradoException;
import com.catalisa.iSchool.model.dto.AlunoDTO;
import com.catalisa.iSchool.service.AlunoService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AlunoController.class)
class AlunoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	//Mockando o Service
	@MockBean
	private AlunoService alunoService;


	@Test
	@DisplayName("Buscar todos os alunos ")
	public void testeGetTodosAlunos() throws Exception {

		// Criando uma lista de alunos para o alunoService retornar
		AlunoDTO aluno1 = new AlunoDTO("João", 20, "joao@example.com");
		AlunoDTO aluno2 = new AlunoDTO("Maria", 22, "maria@example.com");
		Mockito.when(alunoService.listarTodos()).thenReturn(List.of(aluno1, aluno2));

		// Configurando o MockMVC
		mockMvc.perform(get("/api/ischool/alunos"))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].nome").value("João"))
				.andExpect(jsonPath("$[0].idade").value(20))
				.andExpect(jsonPath("$[0].email").value("joao@example.com"))
				.andExpect(jsonPath("$[1].nome").value("Maria"))
				.andExpect(jsonPath("$[1].idade").value(22))
				.andExpect(jsonPath("$[1].email").value("maria@example.com"));

		// Verificando se o método listarTodos do alunoService foi chamado uma vez
		Mockito.verify(alunoService, times(1)).listarTodos();

	}

	@Test
	@DisplayName("Buscar aluno por ID ")
	public void testeGetAlunoPorId() throws Exception {

		Long idAluno = 1L;
		AlunoDTO aluno1 = new AlunoDTO("João", 20, "joao@example.com");
		Mockito.when(alunoService.listarPorId(idAluno)).thenReturn(Optional.of(aluno1));

		// Configurando o MockMVC
		mockMvc.perform(get("/api/ischool/alunos/{id}", idAluno))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.nome").value("João"))
				.andExpect(jsonPath("$.idade").value(20))
				.andExpect(jsonPath("$.email").value("joao@example.com"))
				.andDo(print());

		// Verificando se o método listarTodos do alunoService foi chamado uma vez
		Mockito.verify(alunoService, times(1)).listarPorId(Mockito.any());

	}

	@Test
	@DisplayName("Cadastrar um aluno")
	public void cadastrarAlunosTest() throws Exception {

		AlunoDTO alunoDTO = new AlunoDTO("Iris", 26, "iris@gmail.com");

		when(alunoService.criar(Mockito.any(alunoDTO.getClass()))).thenReturn(alunoDTO);

		mockMvc.perform(post("/api/ischool/alunos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(alunoDTO)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.nome").value("Iris"))
				.andExpect(jsonPath("$.idade").value("26"))
				.andExpect(jsonPath("$.email").value("iris@gmail.com"));

//		 utilizando o mockito verify ocorre um erro. tentar corrigir mais tarde
		Mockito.verify(alunoService, times(1)).criar(Mockito.any(alunoDTO.getClass()));

	}

	@Test
	@DisplayName("Alterar um aluno")
	public void alterarUmAlunoTest() throws Exception{

		AlunoDTO alunoDTO = new AlunoDTO("Iris", 26, "iris@gmail.com");
		Long idAluno = 1L;
		when(alunoService.alterar(Mockito.any(), Mockito.any(alunoDTO.getClass()))).thenReturn(alunoDTO);

		mockMvc.perform(put("/api/ischool/alunos/{id}", idAluno)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(alunoDTO)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Iris"))
				.andExpect(jsonPath("$.idade").value("26"))
				.andExpect(jsonPath("$.email").value("iris@gmail.com"));

		Mockito.verify(alunoService, times(1)).alterar(Mockito.any(), Mockito.any(alunoDTO.getClass()));


	}

	@Test
	@DisplayName("Deletar um aluno")
	public void deletarUmAlunoTest() throws Exception{
		mockMvc.perform(delete("/api/ischool/alunos/{id}", 1L))
				.andDo(print())
				.andExpect(status().isOk());

		Mockito.verify(alunoService, times(1)).deletar(Mockito.any());
	}

	@Test
	@DisplayName("Deletar um aluno inexistente")
	public void deletarUmAlunoInexistenteTest() throws Exception{

		doThrow(new IdNaoEncontradoException("Aluno de ID 1 não foi encontrado"))
				.when(alunoService).deletar(1L);

		mockMvc.perform(delete("/api/ischool/alunos/{id}", 1L))
				.andDo(print())
				.andExpect(status().isNotFound());

		Mockito.verify(alunoService, times(1)).deletar(Mockito.any());

	}

}
