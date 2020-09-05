package com.myfood.api.model.mixin;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myfood.domain.model.Cozinha;
import com.myfood.domain.model.Endereco;
import com.myfood.domain.model.FormaPagamento;
import com.myfood.domain.model.Produto;

public abstract class RestauranteMixin {
	
	@JsonIgnoreProperties(value="nome", allowGetters = true)
	private Cozinha cozinha;
	
	@JsonIgnore
	private Endereco endereco;
	
	@JsonIgnore
	private OffsetDateTime dataCadastro;
	
	@JsonIgnore
	private OffsetDateTime dataAtualizacao;	
	
	@JsonIgnore
	@ManyToMany
	private List<FormaPagamento> formasPagamento = new ArrayList<>();
	
	@JsonIgnore
	private List<Produto> produtos = new ArrayList<>();

}
