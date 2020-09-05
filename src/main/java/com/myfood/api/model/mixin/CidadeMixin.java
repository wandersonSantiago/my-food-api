package com.myfood.api.model.mixin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myfood.domain.model.Estado;


public class CidadeMixin {

	
	@JsonIgnoreProperties(value = "nome" , allowGetters = true)
	private Estado estado;

}