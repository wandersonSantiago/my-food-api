package com.myfood.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.myfood.domain.exception.CozinhaNaoEncontradaException;
import com.myfood.domain.exception.EntidadeEmUsoException;
import com.myfood.domain.model.Cozinha;
import com.myfood.domain.repository.CozinhaRepository;

@Service
public class CozinhaService {

	private static final String MSG_COZINHA_EM_USO = "Cozinha de código %d não pode ser removida, pois está em uso";

	
	@Autowired
	private CozinhaRepository repository;

	public Cozinha salvar(Cozinha cozinha) {
		return repository.save(cozinha);
	}

	public Optional<Cozinha> findById(Long cozinhaId) {
		return repository.findById(cozinhaId);
	}

	public Cozinha findByIdOrFail(Long cozinhaId) {
		return repository.findById(cozinhaId).orElseThrow(() -> new CozinhaNaoEncontradaException(cozinhaId));
	}
	
	public List<Cozinha> findAll() {
		return repository.findAll();
	}

	public void excluir(Long cozinhaId) {
		try {
			repository.deleteById(cozinhaId);

		} catch (EmptyResultDataAccessException e) {
			throw new CozinhaNaoEncontradaException(cozinhaId);

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_COZINHA_EM_USO, cozinhaId));
		}
	}

}