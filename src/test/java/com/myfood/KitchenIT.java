
package com.myfood;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.myfood.domain.exception.CozinhaNaoEncontradaException;
import com.myfood.domain.exception.EntidadeEmUsoException;
import com.myfood.domain.model.Cozinha;
import com.myfood.domain.service.CozinhaService;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class KitchenIT {

	@Autowired
	private CozinhaService service;

	@Test
	public void whenRegisteringKitchenSuccess_thenAssignId() {

		// scenario
		var newCozinha = new Cozinha();
		newCozinha.setNome("Chinesa");

		// action

		newCozinha = service.salvar(newCozinha);

		// validations

		assertThat(newCozinha).isNotNull();
		assertThat(newCozinha.getId()).isNotNull();
	}

	@Test
	public void whenRegisteringKitchenWithoutName_thenGenerateException() {
		// scenario
		var newCozinha = new Cozinha();
		newCozinha.setNome(null);

		// action

		// validations
		assertThrows(ConstraintViolationException.class, () -> service.salvar(newCozinha));
	}

	@Test
	public void whenExcludingKitchenInUse_thenMustFail() {
		// validations
		assertThrows(EntidadeEmUsoException.class, () -> service.excluir(1L));
	}

	@Test
	public void whenExcludingKitchenNonexistent_thenMustFail() {

		// validations
		assertThrows(CozinhaNaoEncontradaException.class, () -> service.excluir(1000L));
	}
}
