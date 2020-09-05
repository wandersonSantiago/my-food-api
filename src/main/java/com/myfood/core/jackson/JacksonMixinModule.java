package com.myfood.core.jackson;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.myfood.api.model.mixin.CidadeMixin;
import com.myfood.api.model.mixin.CozinhaMixin;
import com.myfood.api.model.mixin.RestauranteMixin;
import com.myfood.domain.model.Cidade;
import com.myfood.domain.model.Cozinha;
import com.myfood.domain.model.Restaurante;

@Component
public class JacksonMixinModule extends SimpleModule{
	private static final long serialVersionUID = 1L;
	
	
	public JacksonMixinModule() {
		setMixInAnnotation(Cidade.class, CidadeMixin.class);
		setMixInAnnotation(Cozinha.class, CozinhaMixin.class);
		setMixInAnnotation(Restaurante.class, RestauranteMixin.class);
	}

}
