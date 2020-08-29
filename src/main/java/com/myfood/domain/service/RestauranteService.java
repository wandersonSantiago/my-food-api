package com.myfood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myfood.domain.model.Cozinha;
import com.myfood.domain.model.Restaurante;
import com.myfood.domain.repository.RestauranteRepository;

@Service
public class RestauranteService {

	@Autowired
	private RestauranteRepository repository;

	@Autowired
	private CozinhaService cozinhaService;

	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();

		Cozinha cozinha = cozinhaService.findById(cozinhaId);

		restaurante.setCozinha(cozinha);

		return repository.save(restaurante);
	}

}