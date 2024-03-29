package com.algaworks.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se o problema persistir, entre em contato com o administrador do sistema.";

	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
		var status = HttpStatus.FORBIDDEN;
		var detail = ex.getMessage();
		var problem = createProblemBuilder(status, ProblemType.ACESSO_NEGADO, detail)
				.userMessage("Você não tem permissão para executar essa operação").build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity.status(status).headers(headers).build();
	}
	
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
	}

	

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
		var status = HttpStatus.INTERNAL_SERVER_ERROR;
		var detail = MSG_ERRO_GENERICA_USUARIO_FINAL;

		ex.printStackTrace();

		var problem = createProblemBuilder(status, ProblemType.ERRO_DE_SISTEMA, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		var detail = String.format("O recurso '%s', que você tentou acessar, é inexistente.", ex.getRequestURL());
		var problem = createProblemBuilder(status, ProblemType.RECURSO_NAO_ENCONTRADO, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException) ex, headers, status,
					request);
		}

		return super.handleTypeMismatch(ex, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		var rootCause = ExceptionUtils.getRootCause(ex);

		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
		}

		var detail = "O corpo da requisição está inválido. Verifique erro e sintaxe.";
		var problem = createProblemBuilder(status, ProblemType.MENSAGEM_INCOMPREENSIVEL, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<Object> handleNegocioException(NegocioException ex, WebRequest request) {
		var status = HttpStatus.BAD_REQUEST;
		var detail = ex.getMessage();
		var problem = createProblemBuilder(status, ProblemType.ERRO_NEGOCIO, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<Object> handleEntidaEmUsoException(EntidadeEmUsoException ex, WebRequest request) {
		var status = HttpStatus.CONFLICT;
		var detail = ex.getMessage();
		var problem = createProblemBuilder(status, ProblemType.ENTIDADE_EM_USO, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<Object> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex,
			WebRequest request) {

		var status = HttpStatus.NOT_FOUND;
		var detail = ex.getMessage();
		var problem = createProblemBuilder(status, ProblemType.RECURSO_NAO_ENCONTRADO, detail).userMessage(detail)
				.build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (body == null) {
			body = Problem.builder().timestamp(OffsetDateTime.now()).title(status.getReasonPhrase())
					.status(status.value()).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
		} else if (body instanceof String) {
			body = Problem.builder().timestamp(OffsetDateTime.now()).title(String.valueOf(body)).status(status.value())
					.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	
	private ResponseEntity<Object> handleValidationInternal(Exception ex, HttpHeaders headers,
			HttpStatus status, WebRequest request, BindingResult bindingResult) {
		
		String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente";
		var problemObjects = bindingResult.getAllErrors().stream().map(objectError -> {
			var message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
			var name = objectError.getObjectName();

			if (objectError instanceof FieldError) {
				name = ((FieldError) objectError).getField();
			}

			return ProblemObject.builder().name(name).userMessage(message).build();
		}).collect(Collectors.toList());

		var problem = createProblemBuilder(status, ProblemType.DADOS_INVALIDOS, detail).userMessage(detail)
				.objects(problemObjects).build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		var path = joinPath(ex.getPath());
		var detail = String
				.format("A propriedade '%s' recebeu o valor '%s' que é  de um tipo inválido. Corrija e informe o valor "
						+ "compatível com o tipo '%s'.", path, ex.getValue(), ex.getTargetType().getSimpleName());
		var problem = createProblemBuilder(status, ProblemType.MENSAGEM_INCOMPREENSIVEL, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		var path = joinPath(ex.getPath());
		var detail = String
				.format("A propriedade '%s' não existe, corrija ou remova essa propriedade e tente novamente.", path);
		var problem = createProblemBuilder(status, ProblemType.MENSAGEM_INCOMPREENSIVEL, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		var detail = String.format(
				"O parâmetro da URL '%s' recebeu o valor '%s', que é de um tipo inválido. Corrija e informe um valor compatível com o tipo '%s'",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
		var problem = createProblemBuilder(status, ProblemType.PARAMETRO_INVALIDO, detail).build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {
		return Problem.builder().timestamp(OffsetDateTime.now()).status(status.value()).type(problemType.getUri())
				.title(problemType.getTitle()).detail(detail);
	}

	private String joinPath(List<Reference> ex) {
		return ex.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
	}
}
