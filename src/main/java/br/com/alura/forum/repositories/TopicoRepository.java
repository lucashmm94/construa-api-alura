package br.com.alura.forum.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forum.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

	//Padrao findBy é o padrao, passa a propriedade curso que esta relacionado
	//com titulo. Nome é atributo de curso, é so concatenar.
	Page<Topico> findByCursoNome(String nomeCurso,Pageable paginacao);
}
