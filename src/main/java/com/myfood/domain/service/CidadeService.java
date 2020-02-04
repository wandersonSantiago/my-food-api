package com.myfood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.myfood.domain.exception.EntidadeEmUsoException;
import com.myfood.domain.exception.EntidadeNaoEncontradaException;
import com.myfood.domain.model.Cidade;
import com.myfood.domain.model.Estado;
import com.myfood.domain.repository.CidadeRepository;

@Service
public class CidadeService {

	@Autowired
	private CidadeRepository repository;
	
	@Autowired
	private EstadoService estadoService;
	
	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();

		Estado estado = estadoService.findById(estadoId);
		
		cidade.setEstado(estado);
		
		return repository.save(cidade);
	}
	
	public void excluir(Long cidadeId) {
		try {
			repository.deleteById(cidadeId);
			
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
				String.format("Não existe um cadastro de cidade com código %d", cidadeId));
		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format("Cidade de código %d não pode ser removida, pois está em uso", cidadeId));
		}
	}
	
}
