package br.com.alura.forum.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${forum.jwt.expiration}")
	private String expiration;

	@Value("${forum.jwt.secret}")
	private String secret;

	public String gerarToken(Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		System.out.println(secret);

		return Jwts.builder()
				.setIssuer("API Forum Alura")
				.setSubject(usuarioLogado.getId().toString())
				.setIssuedAt(hoje).setExpiration(dataExpiracao)
				.signWith(SignatureAlgorithm.HS256, secret)
                .compact();
	}

	
	//Valida se o token ainda esta valido(não expirado)
	public boolean isTokenValido(String token) {
		try {
			// Parse transforma nosso token e compara com a secret
			// parseClaimsJws retorna um objeto com informações do token
			// Se tiver valido ele devolve um objeto de Claims, se cair na exception retorna false(token invalido)
			Jwts.parser()
				.setSigningKey(this.secret)
				.parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;

		}
	}


	public Long getIdUsuario(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}

}
