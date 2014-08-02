package br.ufpi.compilador.beans;

import java.util.ArrayList;




/**
 * Represetação das partes a serem reconhecidas pelo Analizador Léxico.
 * 
 * @author Alcemir Rodrigues Santos  (06N10340) 
 * @author Janielton de Sousa Veloso (06N10242)
 * 
 */
public class TabelaDeSimbolos {

	// OPERADORES BASICOS
	public static final ArrayList<String> OP_ARITIMETICO = new OperatorList(new String[] { "+", "-","*","/" });	
	public static final ArrayList<String> OP_LOGICO = new OperatorList(new String[] { "AND","and","And", "OR","or","Or"});
	
	// SIMBOLOS
	public static final String ASPAS_DUPLAS = "\"";
	public static final String ASPAS_SIMPLES = "'";
	public static final String PARENTESE_D = "\\)";
	public static final String PARENTESE_E = "\\(";
	public static final String COLCHETE_D = "\\]";
	public static final String COLCHETE_E = "\\[";
	public static final String PTO_VIRGULA = ";";
	public static final String PTO = ".";
	public static final String VIRGULA = ",";
	public static final String ESPACO_BRANCO = "( |\t|\n\r)+";

	// OPERADORES RELACIONAIS
	public static final String OP_SEL = "SELECT";
	public static final String OP_PROJ = "PROJECT";
	public static final String OP_PCART = "TIMES";
	public static final String OP_REN = "RENAME";
	public static final String OP_JUN = "JOIN";
	public static final String OP_DIV = "DIVISION";
	public static final String OP_ATRIB = "ATTRIBUTE";
	public static final String NUM_INTEIRO = "[+|-]?(\\d+|\\d{1,3}(.\\d{3})+)";
	public static final String NUM_REAL = "[+|-]?(\\d+|\\d{1,3}(.\\d{3})+)(,\\d*)?|,\\d+";
	
	public static final String LETRA = "[a-zA-Z]";
	// IDENTIFICADOR DE CARACTERE
	public static final String CARACTERE = "([a-zA-Z\\/;\\.\\,\\_\\*\\<\\>\\=\\!\\+\\-\\(\\)\"]|\\d)*";
	// IDENTIFICADOR DE NAO IDENTIFICADORES
	public static final String NAOIDENTIFICADOR = "\\d+([a-zA-Z_\\.])+([a-zA-Z_\\.]|\\d)*";
	public static final String LITERAL = "\"(\\d | \\w | \\W)*^\"\"";
	}

	class OperatorList extends ArrayList<String> {

		private static final long serialVersionUID = 1L;

		public OperatorList(String o[]) {
			super(o.length);
			for (int i = 0; i < o.length; i++)
				super.add(o[i]);
		}
	
}
