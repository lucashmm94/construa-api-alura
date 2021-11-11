package br.com.alura.forum.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forum.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

	//Padrao findBy é o padrao, passa a propriedade curso que esta relacionado
	//com titulo. Nome é atributo de curso, é so concatenar.
	List<Topico> findByCursoNome(String nomeCurso);
}
