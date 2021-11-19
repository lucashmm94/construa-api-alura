package br.com.alura.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.alura.forum.repositories.UsuarioRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

	@Autowired
	TokenService tokenService;
	
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		// É iniciado ao subir a aplicação para fazer a autenticação manual
		return super.authenticationManager();
	}

	// Configura de autenticacao(recebe um usuario e senha, caso não exista ele retorna uma UsernameNotFoundException, caso tenha segue o fluxo normal)
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Sobe ao iniciar a aplicação
		// Voce passa um service para autenticação que voce mesmo cria
		// Um encoder para descriptografar a senha
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}

	// Configura autorização(url, perfil de acesso,oq é public ou oq restrito)
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests() // autorirar requições
				.antMatchers(HttpMethod.GET, "/topicos").permitAll() // qual url filtra(permitir ou bloquear)
				.antMatchers(HttpMethod.GET, "/topicos/*").permitAll()// Se nao falar o metodo http, libera tudo
				.antMatchers(HttpMethod.POST, "/auth").permitAll().anyRequest().authenticated() // Qualquer outra
																								// requisição tem que
																								// autenticar;
//		.and().formLogin(); //Spring gerar formulario de autentição, cria sessao(nao ideal)
				.and().csrf().disable() // Via token não precisa validar essa config anti hacker;
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Cria sessao stateless
				.and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);// Classe criada AutenticacaoViaTokenFilter																														// para
																														//  para ser executada antes do Spring Padrao
																														// Nessa classe, se recupera o token 
																														//e valida o token e pede o Spring para validar o token também
	}

	// Configura arquivos estasticos(js,css,imagens,etc).
	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		super.configure(web);
	}

}
