package com.myfood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.myfood.domain.exception.EntidadeEmUsoException;
import com.myfood.domain.exception.EstadoNaoEncontradoException;
import com.myfood.domain.model.Estado;
import com.myfood.domain.repository.EstadoRepository;

@Service
public class EstadoService {

	private static final String MSG_ESTADO_EM_USO = "Estado de código %d não pode ser removido, pois está em uso";

	@Autowired
	private EstadoRepository repository;

	public Estado insert(Estado estado) {
		return repository.save(estado);
	}

	public Estado findByIdOrFail(Long id) {
		return repository.findById(id).orElseThrow(() -> new EstadoNaoEncontradoException(id));
	}

	public List<Estado> findAll() {
		return repository.findAll();
	}

	public void delete(Long estadoId) {
		try {
			repository.deleteById(estadoId);

		} catch (EmptyResultDataAccessException e) {
			throw new EstadoNaoEncontradoException(estadoId);

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_ESTADO_EM_USO, estadoId));
		}
	}

}