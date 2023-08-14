package com.catalisa.iSchool;

import com.catalisa.iSchool.model.dto.AlunoDTO;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@WireMockTest(httpPort = 8080)
class ISchoolApplicationTests {

	@Test
	@DisplayName("Buscar todos os alunos usando WireMock")
	void testGetAlunosUsingWireMock() throws Exception {
		// Configurando o WireMock
		WireMock.stubFor(WireMock.get(urlEqualTo("/api/ischool/alunos"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBodyFile("alunosget.json")));

		RestTemplate restTemplate = new RestTemplate();

		AlunoDTO[] alunos = restTemplate.getForObject("http://localhost:8080/api/ischool/alunos", AlunoDTO[].class);

		assertNotNull(alunos);
		assertEquals(1, alunos.length);
		assertEquals("Joao Victor", alunos[0].getNome());
		assertEquals(23, alunos[0].getIdade());
		assertEquals("joao.cruz@zup.com.br", alunos[0].getEmail());

		WireMock.verify(getRequestedFor(urlEqualTo("/api/ischool/alunos")));

	}
}
