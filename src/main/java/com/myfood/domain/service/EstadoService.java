package com.myfood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.myfood.domain.exception.EntidadeEmUsoException;
import com.myfood.domain.exception.EntidadeNaoEncontradaException;
import com.myfood.domain.model.Estado;
import com.myfood.domain.repository.EstadoRepository;

@Service
public class EstadoService {
	
	
	private final String ESTADO_INEXISTENTE = "Não existe cadastro de estado com código";

	@Autowired
	private EstadoRepository repository;

	public Estado salvar(Estado estado) {
		return repository.save(estado);
	}

	public Estado findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException(
				String.format(ESTADO_INEXISTENTE + "%d", id)));
	}

	public void excluir(Long estadoId) {
		try {
			repository.deleteById(estadoId);

		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format(ESTADO_INEXISTENTE + " %d", estadoId));

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("Estado de código %d não pode ser removido, pois está em uso", estadoId));
		}
	}

}
