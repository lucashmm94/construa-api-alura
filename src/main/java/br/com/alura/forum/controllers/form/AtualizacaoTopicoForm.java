package br.com.alura.forum.controllers.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repositories.TopicoRepository;

public class AtualizacaoTopicoForm {
	@NotNull @NotEmpty @Length(min=5)
	private String titulo;
	
	@NotNull @NotEmpty @Length(min=10)
	private String mensagem;

	public String getTitulo() {
		return titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Topico atualizar(Long id, TopicoRepository repository) {
		Topico topicoBanco = repository.findById(id).get();
		BeanUtils.copyProperties(this, topicoBanco);
		return topicoBanco;
	}
	
	
	

}
