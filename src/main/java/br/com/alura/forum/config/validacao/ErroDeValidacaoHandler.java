package br.com.alura.forum.config.validacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErroDeValidacaoHandler {

	//Spring pega a mensagem de erro com essa interface
	@Autowired
	private MessageSource messageSource;
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	// Pegar as exceções, e falar quais tipos de exceções esse metodo vai tratar
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<ErroDeFormularioDTO> handle(MethodArgumentNotValidException exception) {

		List<ErroDeFormularioDTO> errosDTO = new ArrayList<ErroDeFormularioDTO>();
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		
		fieldErrors.forEach(e -> {
			//Pega a mensagem de messageSource, passando a excessão e a localização
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			ErroDeFormularioDTO erro = new ErroDeFormularioDTO(e.getField(), mensagem);
			errosDTO.add(erro);
		});
		
		return errosDTO;

	}

}
