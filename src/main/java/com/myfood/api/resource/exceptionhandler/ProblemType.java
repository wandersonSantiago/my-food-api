package com.myfood.api.resource.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

	DADOS_INVALIDOS("dados-invalidos", "Dados inválidos"),
	ERRO_DE_SISTEMA("erro-de-sistema", "Erro de sistema"),
	PARAMETRO_INVALIDO("parametro-invalido", "Parâmetro inválido"),
	RECURSO_NAO_ENCONTRADO("recurso-nao-encontrado", "Recurso não encontrado"),
	ENTIDADE_EM_USO("entidade-em-uso", "Entidade em uso"),
	ERRO_NEGOCIO("erro-negocio", "Erro de negocio"),
	MENSAGEM_INCOMPREENSIVEL("mensagem-incompreensivel", "Mensagem incompreensível");
	
	private String title;
	private String uri;
	
	ProblemType(String path, String title){
		this.uri = "https://myfood.com.br/" + path;
		this.title = title;
	}
}
