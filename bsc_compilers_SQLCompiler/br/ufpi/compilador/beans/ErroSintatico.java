package br.ufpi.compilador.beans;

import br.ufpi.compilador.enums.TipoErro;

public class ErroSintatico {
	private int coluna;	
	private TipoErro tipo;
	private String mensagem;

	
	/**
	 * Construtor da classe
	 */
	public ErroSintatico() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Construtor completo da classe
	 * @param coluna
	 * @param caractere
	 * @param tipo
	 */
	public ErroSintatico(int coluna, TipoErro tipo, String mensagem) {		
		this.coluna = coluna;		
		this.tipo = tipo;
		this.mensagem = mensagem;
	}
	
	public int getColuna() {
		return coluna;
	}
	
	public void setColuna(int coluna) {
		this.coluna = coluna;
	}
	
	public TipoErro getTipo() {
		return tipo;
	}
	
	public void setTipo(TipoErro tipo) {
		this.tipo = tipo;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getMensagem() {
		return mensagem;
	}		

}

