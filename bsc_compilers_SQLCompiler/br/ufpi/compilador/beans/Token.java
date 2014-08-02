package br.ufpi.compilador.beans;

import br.ufpi.compilador.enums.TipoToken;

/**
 * Represetação de cada pedaço léxicamente correta comando submetido ao compilador.
 * 
 * @author Alcemir Rodrigues Santos  (06N10340) 
 * @author Janielton de Sousa Veloso (06N10242)
 * 
 */
public class Token {

	// tipo de token
	private TipoToken lexema;
	// valor do token
	private String atributo;
	// coluna de inicio do token
	private int colunaDeInicio;
	
	
	
	public Token() {
		super();		
	}
	
	public Token(TipoToken lexema, String atributo, int colunaDeInicio) {
		super();
		this.lexema = lexema;
		this.atributo = atributo;
		this.colunaDeInicio = colunaDeInicio;
	}

	
	public TipoToken getLexema() {
		return lexema;
	}

	public void setLexema(TipoToken lexema) {
		this.lexema = lexema;
	}

	public String getAtributo() {
		return atributo;
	}
	
	public void setAtributo(String atributo) {
		this.atributo = atributo;
	}

	public int getColunaDeInicio() {
		return colunaDeInicio;
	}

	public void setColunaDeInicio(int colunaDeInicio) {
		this.colunaDeInicio = colunaDeInicio;
	}
}//fim da classe

