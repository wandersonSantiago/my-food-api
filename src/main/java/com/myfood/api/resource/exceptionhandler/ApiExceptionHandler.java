package com.myfood.api.resource.exceptionhandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.myfood.domain.exception.EntidadeEmUsoException;
import com.myfood.domain.exception.EntidadeNaoEncontradaException;
import com.myfood.domain.exception.NegocioException;

@ControllerAdvice
public class ApiExceptionHandler  extends ResponseEntityExceptionHandler{

	
	private static final String MSG_ERRO_GENERICA_USUARIO_FINAL = 
			"Ocorreu um erro interno inesperado no sistema. Tente novamente e se o problema persistir, entre em contato com o administrador do sistema.";
	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleExceptionEntity(EntidadeNaoEncontradaException e, WebRequest request){
		
		var problem = createProblemBuilder(HttpStatus.NOT_FOUND, ProblemType.RECURSO_NAO_ENCONTRADO, e.getMessage()).userMessage(e.getMessage()).build();
		
		return handleExceptionInternal(e, problem, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUso(EntidadeEmUsoException e, WebRequest request) {
		
		var problem = createProblemBuilder(HttpStatus.CONFLICT, ProblemType.ENTIDADE_EM_USO, e.getMessage()).userMessage(e.getMessage()).build();
		
		return handleExceptionInternal(e, problem, new HttpHeaders(), HttpStatus.CONFLICT, request);			
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleExceptionNegocio(NegocioException e, WebRequest request){
		
		var problem = createProblemBuilder(HttpStatus.BAD_REQUEST, ProblemType.ERRO_NEGOCIO, e.getMessage()).userMessage(e.getMessage()).build();
		
		return handleExceptionInternal(e, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
		
	    ex.printStackTrace();
	    
	    var problem = createProblemBuilder(HttpStatus.INTERNAL_SERVER_ERROR, ProblemType.ERRO_DE_SISTEMA, MSG_ERRO_GENERICA_USUARIO_FINAL).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

	    return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	} 
		
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	    
	    var detail = String.format("O recurso %s, que você tentou acessar, é inexistente.", ex.getRequestURL());
	    
	    var problem = createProblemBuilder(status, ProblemType.RECURSO_NAO_ENCONTRADO, detail).userMessage(detail).build();
	    
	    return handleExceptionInternal(ex, problem, headers, status, request);
	}  
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,  HttpStatus status, WebRequest request) {
	    
	    if (ex instanceof MethodArgumentTypeMismatchException) 
	        return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
	    
	    return super.handleTypeMismatch(ex, headers, status, request);
	}

	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

	    var detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
	            ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

	    var problem = createProblemBuilder(status, ProblemType.PARAMETRO_INVALIDO, detail).userMessage(detail).build();

	    return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		var rootCause = ExceptionUtils.getRootCause(e);
		
		if(rootCause instanceof InvalidFormatException) {
			return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
		}else if (rootCause instanceof PropertyBindingException) {
	        return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request); 
	    }
		
		var detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
		var problem = createProblemBuilder(status, ProblemType.MENSAGEM_INCOMPREENSIVEL, detail).userMessage(detail).build();
		
		return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);	
	}
	
	private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		var path = ex.getPath().stream()
				.map(ref -> ref.getFieldName())
				.collect(Collectors.joining("."));
		
		var detail = String.format("A propriedade '%s' recebeu o valor '%s', que é de um tipo inválido. "
				+ "Corrija e informe um valor compativel com o tipo %s.", path , ex.getValue(), ex.getTargetType().getSimpleName());
		
		var problem = createProblemBuilder(status, ProblemType.MENSAGEM_INCOMPREENSIVEL, detail).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,    HttpHeaders headers, HttpStatus status, WebRequest request) {
	    
	    ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
	    String detail = String.format("A propriedade '%s' não existe. Corrija ou remova essa propriedade e tente novamente.", joinPath(ex.getPath()));

	    Problem problem = createProblemBuilder(status, problemType, detail).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
	    
	    return handleExceptionInternal(ex, problem, headers, status, request);
	}  
		
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		if(body == null || body instanceof String)
			  body = Problem.builder()
					  .timestamp(LocalDateTime.now())
					  .title( body == null ? status.getReasonPhrase() : (String) body)
					  .status(status.value())
					  .build();	  
		  
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
		
	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType type, String detail){
		return Problem.builder()
				.status(status.value())
				.type(type.getUri())
				.title(type.getTitle())
				.timestamp(LocalDateTime.now())
				.detail(detail);				
	}
	
	private String joinPath(List<Reference> references) {
	    return references.stream()
	        .map(ref -> ref.getFieldName())
	        .collect(Collectors.joining("."));
	}  
}
