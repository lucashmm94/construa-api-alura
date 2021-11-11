package br.com.alura.forum.controllers;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controllers.dtos.DetalhesTopicoDTO;
import br.com.alura.forum.controllers.dtos.TopicoDTO;
import br.com.alura.forum.controllers.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controllers.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repositories.CursoRepository;
import br.com.alura.forum.repositories.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

	@Autowired
	private TopicoRepository repository;
	@Autowired
	private CursoRepository cursoRepository;

	@GetMapping
	@Cacheable(value="listaDeTopicos")
	public Page<TopicoDTO> findAll(@RequestParam(required = false)String nomeCurso,
			@PageableDefault(sort = "dataCriacao", direction = Direction.DESC, size = 10,page=0) Pageable paginacao) {
		
		
//		Pageable paginacao = PageRequest.of(pagina, qtd,Direction.DESC,ordenacao);
		Page<Topico> topicos;
		if (nomeCurso == null) {
			topicos = repository.findAll(paginacao);
		} else {
			topicos = repository.findByCursoNome(nomeCurso,paginacao);
		}
		return TopicoDTO.converter(topicos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DetalhesTopicoDTO> findById(@PathVariable Long id) {
		Optional <Topico> topico = repository.findById(id);
		if(topico.isPresent()) {
			return ResponseEntity.ok( new DetalhesTopicoDTO(topico.get())) ;
		} 
		return ResponseEntity.notFound().build();
		
		
	}

	@Transactional
	@PostMapping(value = "/cadastrar")
	@CacheEvict(value = "listaDeTopicos")
	public ResponseEntity<TopicoDTO> save(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriBuilder) {
		Topico topico = topicoForm.converter(cursoRepository);
		repository.save(topico);
		// O 201(created tem que passar um caminho para achar o recurso, o uri builder
		// ajuda nisso)
		// O spring pega a url dinamica(local host ou ip) e concatena com o recurso+ id
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

		return ResponseEntity.created(uri).body(new TopicoDTO(topico));
	}

	@PutMapping("/{id}")
	// Garantir o commit no banco de dados
	@Transactional
	@CacheEvict(value = "listaDeTopicos")
	public ResponseEntity<TopicoDTO> put(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional <Topico> optional = repository.findById(id);
		if(optional.isPresent()) {			
			Topico topico = form.atualizar(id, repository);
			return ResponseEntity.ok(new TopicoDTO(topico));
		}
		return ResponseEntity.notFound().build();
	}

	@Transactional
	@DeleteMapping("/{id}")
	@CacheEvict(value = "listaDeTopicos")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Optional <Topico> optional = repository.findById(id);
		if(optional.isPresent()) {	
			repository.deleteById(id);
			return ResponseEntity.ok().build();			
		}
		return ResponseEntity.notFound().build();
	}

}
