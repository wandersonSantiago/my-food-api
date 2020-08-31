package com.myfood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.myfood.domain.exception.EntidadeEmUsoException;
import com.myfood.domain.exception.RestauranteNaoEncontradoException;
import com.myfood.domain.model.Cozinha;
import com.myfood.domain.model.Restaurante;
import com.myfood.domain.repository.RestauranteRepository;

@Service
public class RestauranteService {

	@Autowired
	private RestauranteRepository repository;
	@Autowired
	private CozinhaService cozinhaService;

	private static final String MSG_RESTAURANTE_EM_USO = "Restaurante de código %d não pode ser removida, pois está em uso";

	public Restaurante insert(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();

		Cozinha cozinha = cozinhaService.findByIdOrFail(cozinhaId);

		restaurante.setCozinha(cozinha);

		return repository.save(restaurante);
	}

	public List<Restaurante> findAll() {
		return repository.findAll();
	}

	public Restaurante findByIdOrFail(Long restauranteId) {
		return repository.findById(restauranteId)
				.orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
	}
	
	public void delete(Long estadoId) {
		try {
			repository.deleteById(estadoId);

		} catch (EmptyResultDataAccessException e) {
			throw new RestauranteNaoEncontradoException(estadoId);

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_RESTAURANTE_EM_USO, estadoId));
		}
	}

}