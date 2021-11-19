package br.com.alura.forum.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repositories.UsuarioRepository;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

	private TokenService tokenService;
	
	@Autowired 
	private UsuarioRepository usuarioRepository;
	
	
	
	public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//Recupera o token
		String token = recuperarToken(request);
		boolean isTokenValido = tokenService.isTokenValido(token);
		if(isTokenValido) {
			autenticarCliente(token);
		}
		
		//Filtra para proxima requisição, passando o request e o response(barrando)
		filterChain.doFilter(request, response);
		
	}

	private void autenticarCliente(String token) {
		Long idUsuario = tokenService.getIdUsuario(token);
		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null,usuario.getAuthorities());
		//Forçar a a autenticação
		SecurityContextHolder.getContext().setAuthentication(authentication );
		
	}

	private String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token ==null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		} else {
			return token.substring(7, token.length());
		}
	}

}
