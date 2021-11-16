package br.com.alura.forum.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.config.security.TokenService;
import br.com.alura.forum.controllers.dtos.TokenDTO;
import br.com.alura.forum.controllers.form.LoginForm;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenService tokenService;
	
	
	//Metodo para gerar token e devolver ao cliente
	//Recebendo usuario e senha, gerando UsernamePasswordAuthenticationToken
	@PostMapping
	public ResponseEntity<TokenDTO> aunteticar(@RequestBody @Valid LoginForm form){
		UsernamePasswordAuthenticationToken dadosLogin = form.converter();
		
		try {
			//Vai validar no AutenticacaoService(classe da logica de autenticação)
			Authentication authentication = authManager.authenticate(dadosLogin);
			
			//Com o authManager, consigo extrair quem é o usuario,qual usuario pertence aquele token
			String token = tokenService.gerarToken(authentication);
			return ResponseEntity.ok(new TokenDTO(token,"Bearer"));
			
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
		
		
		
	}

}
