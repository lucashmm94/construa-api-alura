package br.com.alura.forum.controllers.dtos;

import java.time.LocalDateTime;

import br.com.alura.forum.modelo.Resposta;

public class RespostaDTO {
	
	private Long id;
	private String mensagem;
	private LocalDateTime dataCriacao;
	private String nomeAutor;
	
	public RespostaDTO (Resposta resposta) {
		this.id = resposta.getId();
		this.mensagem = resposta.getMensagem();
		this.dataCriacao = resposta.getDataCriacao();
		this.nomeAutor = resposta.getAutor().getNome();
	}

	@Override
	public String toString() {
		return "RespostaDTO [id=" + id + ", mensagem=" + mensagem + ", dataCriacao=" + dataCriacao + ", nomeAutor="
				+ nomeAutor + "]";
	}

	public Long getId() {
		return id;
	}

	public String getMensagem() {
		return mensagem;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public String getNomeAutor() {
		return nomeAutor;
	}
	
	
}