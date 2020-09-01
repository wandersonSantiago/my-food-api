package com.myfood.api.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myfood.domain.exception.EntidadeNaoEncontradaException;
import com.myfood.domain.exception.NegocioException;
import com.myfood.domain.model.Cidade;
import com.myfood.domain.service.CidadeService;

@RestController
@RequestMapping(value = "/cidades")
public class CidadeResource {

	@Autowired
	private CidadeService service;

	@GetMapping
	public List<Cidade> findAll() {
		return service.findAll();
	}

	@GetMapping("/{cidadeId}")
	public Cidade findById(@PathVariable Long cidadeId) {
		return service.findByIdOrFail(cidadeId);
	}

	@PostMapping
	public Cidade insert(@RequestBody @Valid Cidade cidade) {
		return service.insert(cidade);
	}

	@PutMapping("/{cidadeId}")
	public Cidade update(@PathVariable Long cidadeId, @RequestBody @Valid Cidade cidade) {
		var cidadeAtual = service.findByIdOrFail(cidadeId);
		BeanUtils.copyProperties(cidade, cidadeAtual, "id");

		try {
			return cidadeAtual = service.insert(cidadeAtual);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e.getCause());
		}
	}

	@DeleteMapping("/{cidadeId}")
	public void remover(@PathVariable Long cidadeId) {
		service.delete(cidadeId);
	}
	
}