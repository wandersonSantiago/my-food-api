package com.myfood.api.resource;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myfood.core.validation.ValidationException;
import com.myfood.domain.exception.CozinhaNaoEncontradaException;
import com.myfood.domain.exception.NegocioException;
import com.myfood.domain.model.Restaurante;
import com.myfood.domain.service.RestauranteService;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteResource {

	@Autowired
	private RestauranteService service;
	@Autowired
	private SmartValidator validator;

	@GetMapping
	public List<Restaurante> findAll() {
		return service.findAll();
	}

	@GetMapping("/{restauranteId}")
	public Restaurante findById(@PathVariable Long restauranteId) {
		return service.findByIdOrFail(restauranteId);
	}

	@PostMapping
	public Restaurante insert(@RequestBody @Valid Restaurante restaurante) {
		restaurante.setId(null);
		try {
			return service.insert(restaurante);
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@PutMapping("/{restauranteId}")
	public Restaurante update(@PathVariable Long restauranteId, @RequestBody @Valid Restaurante restaurante) {
		var restauranteAtual = service.findByIdOrFail(restauranteId);
		BeanUtils.copyProperties(restaurante, restauranteAtual, "id");
		try {
			return service.insert(restauranteAtual);
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e.getCause());
		}
	}

	@PatchMapping("/{restauranteId}")
	public Restaurante updatePath(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos, HttpServletRequest request) {
		var restauranteAtual = service.findByIdOrFail(restauranteId);
		merge(campos, restauranteAtual, request);
		validate(restauranteAtual, "restaurante");
		return update(restauranteId, restauranteAtual);
	}

	private void validate(Restaurante restaurante, String objectName) {
		BeanPropertyBindingResult bindingResults = new BeanPropertyBindingResult(restaurante, objectName);
		validator.validate(restaurante, bindingResults);
		
		if(bindingResults.hasErrors()) {
			throw new ValidationException(bindingResults);
		}		
	}

	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
		try {
			var objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			
			var restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
	
			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);
	
				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
	
				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});
		}catch (IllegalArgumentException e) {
			throw new HttpMessageNotReadableException(e.getMessage(), ExceptionUtils.getRootCause(e), new ServletServerHttpRequest(request));
		}
	}

	@DeleteMapping("/{restauranteId}")
	public void delete(@PathVariable Long restauranteId) {
		service.delete(restauranteId);
	}

}
