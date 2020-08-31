package com.myfood.api.resource.exceptionhandler;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;

@JsonInclude(value = Include.NON_NULL)
@Getter
@Builder
public class Problem {
	
	private Integer status;
	private String type;
	private String title;
	private String detail;
	private LocalDateTime timestamp;
	
	private String userMessage;
	
}