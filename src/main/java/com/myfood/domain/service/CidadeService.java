package com.myfood.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.myfood.domain.exception.CidadeNaoEncontradaException;
import com.myfood.domain.exception.EntidadeEmUsoException;
import com.myfood.domain.model.Cidade;
import com.myfood.domain.model.Estado;
import com.myfood.domain.repository.CidadeRepository;

@Service
public class CidadeService {

	private static final String MSG_ENTIDADE_EM_USO = "Cidade de código %d não pode ser removida, pois está em uso";

	@Autowired
	private CidadeRepository repository;

	@Autowired
	private EstadoService estadoService;

	public Cidade insert(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();

		Estado estado = estadoService.findByIdOrFail(estadoId);

		cidade.setEstado(estado);

		return repository.save(cidade);
	}

	public List<Cidade> findAll() {
		return repository.findAll();
	}

	public Optional<Cidade> findById(Long cidadeId) {
		return repository.findById(cidadeId);
	}

	public Cidade findByIdOrFail(Long cidadeId) {
		return repository.findById(cidadeId)
				.orElseThrow(() -> new CidadeNaoEncontradaException(cidadeId));
	}

	public void delete(Long cidadeId) {
		try {
			repository.deleteById(cidadeId);

		} catch (EmptyResultDataAccessException e) {
			throw new CidadeNaoEncontradaException(cidadeId);

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_ENTIDADE_EM_USO, cidadeId));
		}
	}


}
