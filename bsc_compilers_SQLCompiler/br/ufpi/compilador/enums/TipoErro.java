package br.ufpi.compilador.enums;

/**
 * Tipo de cada Erro encontrado pelo Compilador.
 * 
 * @author Alcemir Rodrigues Santos  (06N10340) 
 * @author Janielton de Sousa Veloso (06N10242)
 *
 */
public enum TipoErro {

	Caracter_Invalido,
	Esperava_Numero,
	Esperava_Letra,
	LOOKAHEAD_OUT_OF_BOUND,
	TOKEN_INVALIDO_PARA_INICIO_DE_RELACAO,
	ERRO_AO_RECONHECER_TOKEN, 
	TOKEN_INVALIDO, 
}
