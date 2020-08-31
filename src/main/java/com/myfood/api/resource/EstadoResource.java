package com.myfood.api.resource;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.myfood.domain.model.Estado;
import com.myfood.domain.service.EstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoResource {

	@Autowired
	private EstadoService service;

	@GetMapping
	public List<Estado> findAll() {
		return service.findAll();
	}

	@GetMapping("/{estadoId}")
	public Estado findById(@PathVariable Long estadoId) {
		return  service.findByIdOrFail(estadoId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Estado insert(@RequestBody Estado estado) {
		return service.insert(estado);
	}

	@PutMapping("/{estadoId}")
	public Estado update(@PathVariable Long estadoId, @RequestBody Estado estado) {
		var estadoAtual = service.findByIdOrFail(estadoId);
		BeanUtils.copyProperties(estado, estadoAtual, "id");
		return  service.insert(estadoAtual);
	}

	@DeleteMapping("/{estadoId}")
	public void delete(@PathVariable Long estadoId) {
		service.delete(estadoId);
	}

}
