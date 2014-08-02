package br.ufpi.compilador.beans;

import br.ufpi.compilador.enums.TipoErro;
/**
 * Representação dos erros encontrados pelo Compilador
 * 
 * @author Alcemir Rodrigues Santos  (06N10340) 
 * @author Janielton de Sousa Veloso (06N10242)
 * 
 */
public class Erro {

	private int coluna;
	private char caractere;
	private TipoErro tipo;
	private String mensagem;

	public Erro() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Erro(int coluna, char caractere, TipoErro tipo) {
		super();
		this.coluna = coluna;
		this.caractere = caractere;
		this.tipo = tipo;
	}
	
	public int getColuna() {
		return coluna;
	}
	
	public void setColuna(int coluna) {
		this.coluna = coluna;
	}
	
	public char getCaractere() {
		return caractere;
	}
	
	public void setCaractere(char caractere) {
		this.caractere = caractere;
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
}//fim da classe
