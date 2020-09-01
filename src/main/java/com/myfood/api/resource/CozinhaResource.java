package com.myfood.api.resource;

import java.util.List;

import javax.validation.Valid;

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

import com.myfood.domain.model.Cozinha;
import com.myfood.domain.service.CozinhaService;

@RestController
@RequestMapping(value = "/cozinhas")
public class CozinhaResource {

	@Autowired
	private CozinhaService service;

	@GetMapping
	public List<Cozinha> listar() {
		return service.findAll();
	}

	@GetMapping("/{cozinhaId}")
	public Cozinha findById(@PathVariable Long cozinhaId) {
		return service.findByIdOrFail(cozinhaId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cozinha insert(@RequestBody @Valid Cozinha cozinha) {
		return service.salvar(cozinha);
	}

	@PutMapping("/{cozinhaId}")
	public Cozinha update(@PathVariable Long cozinhaId, @RequestBody @Valid Cozinha cozinha) {
			var cozinhaAtual = service.findByIdOrFail(cozinhaId);
			BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
			return service.salvar(cozinhaAtual);
	}

	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long cozinhaId) {
		service.excluir(cozinhaId);				
	}

}
