package br.ufpi.compilador.kernel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import br.ufpi.compilador.basededados.BaseDeDados;
import br.ufpi.compilador.beans.Erro;
import br.ufpi.compilador.beans.TabelaDeSimbolos;
import br.ufpi.compilador.beans.Token;
import br.ufpi.compilador.enums.TipoErro;
import br.ufpi.compilador.enums.TipoToken;
import br.ufpi.compilador.exceptions.NotFoundTokenException;

/**
 * Processamento de Análise Léxica
 * 
 * @author Alcemir Rodrigues Santos (06N10340)
 * @author Janielton de Sousa Veloso (06N10242)
 * 
 * 
 */
public class AnalisadorLexico {
	private int inicio_do_lexema = 0, apontador_adiante = -1;
	private int estado = 0;
	private static String entrada = null;
	private int num_tokens = -1;
	private boolean is_fim_do_comando = false, isLetra = false,
			isDigito = false;
	private String buffer = "", c = "";

	/**
	 * Contrutor completo
	 * 
	 * @param novaEntrada
	 */
	public AnalisadorLexico(String novaEntrada) {
		entrada = novaEntrada;
	}

	public void setEntrada(String novaEntrada) {
		entrada = novaEntrada;
	}

	/**
	 * Construtor
	 */
	public AnalisadorLexico() {
	}

	private Token proximoToken() {
		Token novoToken;

		while (true) {
			switch (estado) {
			case 0: // inicio
				c = proximoCaractere();

				if (compara(c, TabelaDeSimbolos.ESPACO_BRANCO)) {
					// é espaco em branco, tabular, nova linha, ou quebra de
					// linha?
					estado = 0;
					inicio_do_lexema = apontador_adiante + 1;
				} else if (c.equals("<")) {
					// < , <=
					estado = 10;
					buffer += c;
				} else if (c.equals("=")) {
					// =
					estado = 20;
					buffer += c;
				} else if (c.equals(">")) {
					// >, >=
					estado = 30;
					buffer += c;
				} else if (compara(c, "[a-zA-Z]")) {
					// IDENTIFICADORES
					estado = 40;
					buffer += c;
				} else if (c.equals("-")) {
					// NÚMEROS NEGATIVOS
					estado = 48;
					buffer += c;
				} else if (c.equals(".")) {
					// NÚMEROS PONTO FLUTUANTES
					estado = 49;
					buffer += c;
				} else if (compara(c, "[0-9]")) {
					// NÚMEROS POSITIVOS ou HORA
					estado = 50;
					buffer += c;
				} else if (c.equals("'")) {
					// LITERAIS
					estado = 60;
					buffer += c;
				} else if (c.equals(",")) {
					// OPERADOR VIRGULA
					novoToken = new Token(TipoToken.VIRGULA, c,
							inicio_do_lexema);
					reseta();
					return novoToken;
				} else if (c.equals("&")) {
					// OPERADOR AND
					novoToken = new Token(TipoToken.AND, c,
							inicio_do_lexema);
					reseta();
					return novoToken;
				} else if (c.equals("|")) {
					// OPERADOR OR
					novoToken = new Token(TipoToken.OR, c,
							inicio_do_lexema);
					reseta();
					return novoToken;
				} else if (TabelaDeSimbolos.PARENTESE_D.contains(c)) {
					// PARÊNTESE DIREITO
					novoToken = new Token(TipoToken.PARENTESE_D, c,
							inicio_do_lexema);
					reseta();
					return novoToken;
				} else if (TabelaDeSimbolos.PARENTESE_E.contains(c)) {
					// PARÊNTESE ESQUERDO
					novoToken = new Token(TipoToken.PARENTESE_E, c,
							inicio_do_lexema);
					reseta();
					return novoToken;
				} else if (TabelaDeSimbolos.COLCHETE_D.contains(c)) {
					// COLCHETE DIREITO
					novoToken = new Token(TipoToken.COLCHETE_D, c,
							inicio_do_lexema);
					reseta();
					return novoToken;
				} else if (TabelaDeSimbolos.COLCHETE_E.contains(c)) {
					// COLCHETE ESQUERDO
					novoToken = new Token(TipoToken.COLCHETE_E, c,
							inicio_do_lexema);
					reseta();
					return novoToken;
				} else if (c.equals(";")) {
					// FIM DO COMANDO
					estado = 70;
					apontador_adiante--;
				} else {
					// ERROR, CARACTERE DESCONHECIDO
					estado = 99;
				}

				break;

			case 10: /* caractere '<' */
				c = proximoCaractere(); // c é o caractere lookahead
				if (c.equals("=")) {
					estado = 11;
					buffer += c;
				} else if (c.equals(">")) {
					estado = 12;
					buffer += c;
				} else
					estado = 13;
				break;

			case 11:/* token MENOR OU IGUAL */
				c = proximoCaractere();
				if (c.equals(";"))
					is_fim_do_comando = true;
				else
					apontador_adiante--;
				novoToken = new Token(TipoToken.OP_RELACIONAL, buffer,
						inicio_do_lexema);
				reseta();
				return novoToken;

			case 12:/* token DIFERENTE */
				c = proximoCaractere();
				if (c.equals(";"))
					is_fim_do_comando = true;
				else
					apontador_adiante--;
				novoToken = new Token(TipoToken.OP_RELACIONAL, buffer,
						inicio_do_lexema);
				reseta();
				return novoToken;

			case 13:/* token MENOR QUE */
				retroceder(1);
				c = proximoCaractere();
				if (c.equals(";"))
					is_fim_do_comando = true;
				else
					apontador_adiante--;
				novoToken = new Token(TipoToken.OP_RELACIONAL, buffer,
						inicio_do_lexema);
				reseta();
				return novoToken;

			case 20:/* token IGUAL */
				c = proximoCaractere();
				if (c.equals(";"))
					is_fim_do_comando = true;
				else
					apontador_adiante--;
				novoToken = new Token(TipoToken.OP_RELACIONAL, buffer,
						inicio_do_lexema);
				reseta();
				return novoToken;

			case 30: /* caractere '>' */
				c = proximoCaractere();// c é o caractere lookahead
				if (c.equals("=")) {
					estado = 31;
					buffer += c;
				} else
					estado = 32;
				break;

			case 31: /* token MAIOR OU IGUAL */
				c = proximoCaractere();
				if (c.equals(";"))
					is_fim_do_comando = true;
				else
					apontador_adiante--;
				novoToken = new Token(TipoToken.OP_RELACIONAL, buffer,
						inicio_do_lexema);
				reseta();
				return novoToken;

			case 32:/* token MAIOR QUE */
				retroceder(1);
				c = proximoCaractere();
				if (c.equals(";"))
					is_fim_do_comando = true;
				else
					apontador_adiante--;
				novoToken = new Token(TipoToken.OP_RELACIONAL, buffer,
						inicio_do_lexema);
				reseta();
				return novoToken;

			case 40: // inicio identificadores ou palavras-chave
				c = proximoCaractere();// c é o caractere lookahead
				isDigito = compara(c, "\\d");
				isLetra = compara(c, "[a-zA-Z]");

				if (isLetra) { // é letra??
					estado = 40;
					buffer += c;
				} else if (isDigito) { // é número??
					estado = 40;
					buffer += c;
				} else if (c.equals("_")) { // é "_"??
					estado = 40;
					buffer += c;
				} else if (c.equals(".")) {
					estado = 41;
					buffer += c;
				} else
					estado = 43; // não é nem letra, nem número, nem underline.
				break;

			case 41:
				c = proximoCaractere();// c é o caractere lookahead
				isLetra = compara(c, "[a-zA-Z]");

				if (isLetra) {/* é letra? */
					estado = 42;
					buffer += c;
				} else {

					memorizaErro(TipoErro.Esperava_Letra);
					System.out.println("ERROR PROXIMO A COLUNA "
							+ (apontador_adiante - 1)
							+ ": Esperava uma letra. Encontrou ("
							+ entrada.charAt(apontador_adiante - 1) + ")");
					buffer = "";
					retroceder(1);
					estado = 0;
				}

				break;
			case 42:
				c = proximoCaractere();// c é o caractere lookahead
				isDigito = compara(c, "\\d");
				isLetra = compara(c, "[a-zA-Z]");

				if (isLetra) { // é letra??
					estado = 42;
					buffer += c;
				} else if (isDigito) { // é número??
					estado = 42;
					buffer += c;
				} else if (c.equals("_")) { // é "_"??
					estado = 42;
					buffer += c;
				} else {
					estado = 43; // não é nem letra nem número.
				}

				break;

			case 43: /* token IDENTIFICADOR */
				retroceder(1);
				c = proximoCaractere();
				if (c.equals(";"))
					is_fim_do_comando = true;
				else
					apontador_adiante--;
				novoToken = new Token(TipoToken.ID, buffer, inicio_do_lexema);
				reseta();
				return novoToken;

			case 48:
				c = proximoCaractere();
				isDigito = compara(c, "\\d");

				if (isDigito) {
					estado = 50;
					buffer += c;
				} else if (c.equals(".")) {
					estado = 49;
					buffer += c;
				} else {
					memorizaErro(TipoErro.Esperava_Numero);
					System.out.println("ERROR PROXIMO A COLUNA "
							+ (apontador_adiante - 1)
							+ ":Esperava um numero. Encontrou ("
							+ entrada.charAt(apontador_adiante) + ").");
					buffer = "";
					retroceder(1);
					estado = 0;

				}

				break;
			case 49:
				c = proximoCaractere();
				isDigito = compara(c, "\\d");

				if (isDigito) {
					estado = 50;
					buffer += c;
				} else {

					memorizaErro(TipoErro.Esperava_Letra);
					System.out.println("ERROR PROXIMO A COLUNA "
							+ (apontador_adiante - 1)
							+ ": Esperava uma letra. Encontrou ("
							+ entrada.charAt(apontador_adiante) + ")");
					buffer = "";
					retroceder(1);
					estado = 0;
				}

				break;
			case 50:
				c = proximoCaractere();// c é o caractere lookahead
				isDigito = compara(c, "\\d");
				if (isDigito) {
					estado = 50;
					buffer += c;
				} else if (c.equals(".")) {
					estado = 51;
					buffer += c;
				} else if (c.equals("E")) {
					estado = 54;
					buffer += c;
				} else if ((buffer.length() <= 2) && c.equals(":")) {
					estado = 57;
					buffer += c;
				} else {
					estado = 56;
				}

				break;

			case 51:
				c = proximoCaractere();// c é o caractere lookahead
				isDigito = compara(c, "\\d");

				if (isDigito) { /* é número?? */
					estado = 52;
					buffer += c;
				} else {
					memorizaErro(TipoErro.Esperava_Numero);
					System.out.println("ERROR PROXIMO A COLUNA "
							+ (apontador_adiante - 1)
							+ ": Esperava um numero. Encontrou ("
							+ entrada.charAt(apontador_adiante) + ")");
					buffer = "";
					retroceder(1);
					estado = 0;
				}
				break;

			case 52:
				c = proximoCaractere();// c é o caractere lookahead
				isDigito = compara(c, "\\d");

				if (isDigito) { /* é número?? */
					estado = 52;
					buffer += c;
				} else if (c.equals("E")) {
					estado = 53;
					buffer += c;
				} else {
					estado = 56;
				}
				break;

			case 53:
				c = proximoCaractere();// c é o caractere lookahead
				isDigito = compara(c, "\\d");

				if (c.equals("+") || c.equals("-")) {
					estado = 54;
					buffer += c;
				} else if (isDigito) {
					estado = 55;
					buffer += c;
				} else {
					estado = 56;
				}
				break;

			case 54:
				c = proximoCaractere();// c é o caractere lookahead
				isDigito = compara(c, "\\d");

				if (isDigito) {
					estado = 55;
					buffer += c;
				} else {

					memorizaErro(TipoErro.Esperava_Numero);
					System.out.println("ERROR PROXIMO A COLUNA "
							+ (apontador_adiante - 1)
							+ ": Esperava um numero. Encontrou ("
							+ entrada.charAt(apontador_adiante) + ")");
					buffer = "";
					retroceder(1);
					estado = 0;
				}
				break;

			case 55:
				c = proximoCaractere();// c é o caractere lookahead
				isDigito = compara(c, "\\d");

				if (isDigito) { /* é número?? */
					estado = 55;
					buffer += c;
				} else {
					estado = 56;
				}
				break;

			case 56:
				retroceder(1);
				c = proximoCaractere();

				if (c.equals(";"))
					is_fim_do_comando = true;
				else
					apontador_adiante--;
				novoToken = new Token(TipoToken.NUM, buffer, inicio_do_lexema);
				reseta();
				return novoToken;

			case 57:
				c = proximoCaractere();
				isDigito = compara(c, "\\d");

				if (isDigito) {
					estado = 58;
					buffer += c;
				} else {
					memorizaErro(TipoErro.Esperava_Numero);
					System.out.println("ERROR PROXIMO A COLUNA "
							+ (apontador_adiante - 1)
							+ ": Esperava um numero. Encontrou ("
							+ entrada.charAt(apontador_adiante) + ")");
					buffer = "";
					retroceder(1);
					estado = 0;
				}

				break;

			case 58:
				c = proximoCaractere();
				isDigito = compara(c, "\\d");

				if (isDigito) {
					estado = 59;
					buffer += c;
				} else {
					memorizaErro(TipoErro.Esperava_Numero);
					System.out.println("ERROR PROXIMO A COLUNA "
							+ (apontador_adiante - 1)
							+ ": Esperava um numero. Encontrou ("
							+ entrada.charAt(apontador_adiante) + ")");
					buffer = "";
					retroceder(1);
					estado = 0;
				}
				break;

			case 59: // retorno de HORA
				retroceder(1);
				c = proximoCaractere();

				if (c.equals(";"))
					is_fim_do_comando = true;
				else
					apontador_adiante--;
				novoToken = new Token(TipoToken.HORA, buffer, inicio_do_lexema);
				reseta();
				return novoToken;

			case 60: // início de literal
				c = proximoCaractere();

				if (c.equals("'")) {
					estado = 61;
					buffer += c;
				} else {
					estado = 60;
					buffer += c;
				}
				break;

			case 61: // fim de literal
				c = proximoCaractere();

				if (c.equals(";"))
					is_fim_do_comando = true;
				else
					apontador_adiante--;
				novoToken = new Token(TipoToken.LITERAL, buffer,
						inicio_do_lexema);
				reseta();
				return novoToken;

			case 70: // fim da linha de comando
				c = proximoCaractere();
				if (c.equals(";"))
					is_fim_do_comando = true;
				else
					apontador_adiante--;
				return null;

			case 99:

				memorizaErro(TipoErro.Caracter_Invalido);
				System.out.println("ERROR PROXIMO A COLUNA "
						+ (apontador_adiante - 1)
						+ ": Entrada de caractere invalido. Encontrou ("
						+ entrada.charAt(apontador_adiante - 1) + ").");
				buffer = "";
				estado = 0;
				break;

			default:
				break;
			}// fim switch
		}// fim while
	}// fim proximoToken

	private void memorizaErro(TipoErro tipo) {
		Erro novoErro = new Erro();
		novoErro.setCaractere(entrada.charAt(apontador_adiante));
		novoErro.setColuna(apontador_adiante - 1);
		novoErro.setTipo(tipo);
		novoErro.setMensagem("ERROR(coluna " + novoErro.getColuna()
				+ "): Tipo( " + tipo + " ), Caractere("
				+ novoErro.getCaractere() + ")");
		BaseDeDados.putErroLexico(BaseDeDados.getErrorsLexicos().size(),
				novoErro);
	}

	private void retroceder(int i) {
		apontador_adiante -= i;
	}

	private boolean compara(String c, String padrao) {
		Pattern p = Pattern.compile(padrao);
		Matcher m = p.matcher(c);
		if (m.matches()) {
			return true;
		}
		return false;
	}

	private String proximoCaractere() {
		apontador_adiante++;
		String next = "";
		next = Character.toString(entrada.charAt(apontador_adiante));
		return next;
	}

	private void reseta() {
		num_tokens++;
		inicio_do_lexema = apontador_adiante + 1;
		estado = 0;
		c = "";
		buffer = "";

	}

	private void identificaTokenOperador(Token tkn) {

		if (tkn.getAtributo().equals(TabelaDeSimbolos.OP_ATRIB))
			tkn.setLexema(TipoToken.OP_ATRIB); // é operador de ATRIBUIÇÃO
		else if (tkn.getAtributo().equals(TabelaDeSimbolos.OP_JUN))
			tkn.setLexema(TipoToken.OP_JUN); // é operador de JUNÇÃO NATURAL
		else if (tkn.getAtributo().equals(TabelaDeSimbolos.OP_REN))
			tkn.setLexema(TipoToken.OP_REN); // é operador de RENOMEAÇÃO
		else if (tkn.getAtributo().equals(TabelaDeSimbolos.OP_SEL))
			tkn.setLexema(TipoToken.OP_SEL); // é operador de SELEÇÃO
		else if (tkn.getAtributo().equals(TabelaDeSimbolos.OP_PROJ))
			tkn.setLexema(TipoToken.OP_PROJ); // é operador de PROJEÇÃO
		else if (tkn.getAtributo().equals(TabelaDeSimbolos.OP_PCART))
			tkn.setLexema(TipoToken.OP_PCART); // é operador de PRODUTO
		// CARTESIANO
		else if (tkn.getAtributo().equals(TabelaDeSimbolos.OP_DIV))
			tkn.setLexema(TipoToken.OP_DIV); // é operador de DIVISÃO
		else if (TabelaDeSimbolos.OP_LOGICO.contains(tkn.getAtributo()))
			tkn.setLexema(TipoToken.OP_LOGICO); // é operador LÓGICO

	}

	// public ArrayList<Token> processarComando(String comando) {
	// ArrayList<Token> tokens = new ArrayList<Token>();
	// Token novo = null;
	//
	// while (!is_fim_do_comando) {
	// novo = proximoToken();
	//
	// if (novo != null) {
	// if (novo.getLexema() == TipoToken.ID) {// separar OPERADORES de
	// // IDENTIFICADORES
	// identificaTokenOperador(novo);
	// }
	// tokens.add(num_tokens, novo);
	// }
	// }
	//
	// return tokens;
	// }

	public void processarComando() {
		Token novo = null;
		BaseDeDados.clearTokens();
		BaseDeDados.clearErrorsLexicos();

		while (!is_fim_do_comando) {
			novo = proximoToken();

			if (novo != null) {
				if (novo.getLexema() == TipoToken.ID) {
					// separar OPERADORES de IDENTIFICADORES
					identificaTokenOperador(novo);
				}
				BaseDeDados.putToken(num_tokens, novo);
			}
		}
	}

	public static void main(String[] args) throws NotFoundTokenException {

		String[] sequencia = { "PROJECT [nome , rg] (SELECT [nome = 'Alcemir', hora = 12:00] (Alunos));" };

		entrada = "PROJECT [nome , rg] (Aluno);";// "asdf. 89. SELECT as>6 .3 nome,outronome maisumnome , eumnovonome DIVISION 4- [-9] TIMES 5.5 RENAME -.3 -akk JOIN  _aka AND Tab_ela.atrib_uto >= '-123' AND Nome = 'Alcemir') (tabela1 PROJECT tabela2);";
		AnalisadorLexico a = new AnalisadorLexico();
		for (int i = 0; i < sequencia.length; i++) {
			System.out.println("Seq " + i + "\n====");
			a.setEntrada(sequencia[i]);
			a.processarComando();
			for (int ji = 0; ji < BaseDeDados.getTokens().size(); ji++) {
				Token token = BaseDeDados.getToken(ji);
				System.out.println("~~~~~~");
				System.out.println("TipoToken: " + token.getLexema()
						+ ",\n Atributo: " + token.getAtributo()
						+ ",\n Coluna: " + token.getColunaDeInicio());
			}
		}
		// ArrayList<Token> meustokens = a.processarComando(entrada);
		// a.processarComando();

	}

}
