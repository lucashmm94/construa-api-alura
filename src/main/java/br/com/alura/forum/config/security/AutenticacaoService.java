package br.com.alura.forum.config.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repositories.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService {
	
	@Autowired
	UsuarioRepository repository;

	//Recebemos o usuario digitado pelo usuario
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// O Spring recebe o usuario e ele mesmo valida a senha
		Optional<Usuario> optionalUsuario = repository.findByEmail(username);
		if(optionalUsuario.isPresent()) {
			return optionalUsuario.get();
		}
		throw new UsernameNotFoundException("Dados invalidos");
	}

}
