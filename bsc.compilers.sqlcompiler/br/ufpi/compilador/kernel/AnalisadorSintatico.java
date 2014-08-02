package br.ufpi.compilador.kernel;

import java.util.ArrayList;
import java.util.Stack;

import br.ufpi.compilador.basededados.BaseDeDados;
import br.ufpi.compilador.beans.ErroSintatico;
import br.ufpi.compilador.beans.Token;
import br.ufpi.compilador.enums.TipoErro;
import br.ufpi.compilador.enums.TipoToken;
import br.ufpi.compilador.exceptions.SintaticoException;

public class AnalisadorSintatico {

	int lookahead = 0;
	private ArrayList<Token> tokens;
	private Stack<Integer> pilhaDeEstados = new Stack<Integer>();/*
														 * servirá para o
														 * empilhamento de
														 * estados no caso de
														 * rótulo "não terminal"
														 */	
	private NoDaArvoreDeTraducao elementoAtual = new NoDaArvoreDeTraducao();
	private NoDaArvoreDeTraducao elementosParaTraducao = elementoAtual; 

//	public AnalisadorSintatico() {
//
//	}

	public AnalisadorSintatico(String sequencia) {
		AnalisadorLexico analisadorLexico = new AnalisadorLexico(sequencia);
		analisadorLexico.processarComando();

		pilhaDeEstados.add(0);
		tokens = BaseDeDados.getTokens();
	}

	/**
	 * Este método é responsável do reconhecimento ou não de um Token.
	 * 
	 * Caso o tokent seja reconhecido incrementa a variável 'lookahead', mas em
	 * caso contrário lança uma exceção do Analizador Sintático.
	 * 
	 * @param classe
	 * @throws SintaticoException 
	 */
	private void reconhecer(TipoToken classe) throws SintaticoException {
		if ((lookahead >= tokens.size())) {

			BaseDeDados
					.putErroSintatico(BaseDeDados.getErrorsSintaticos().size(), new ErroSintatico(tokens.get(
							lookahead - 1).getColunaDeInicio(),
							TipoErro.LOOKAHEAD_OUT_OF_BOUND, " "+TipoErro.LOOKAHEAD_OUT_OF_BOUND));
			throw new SintaticoException(""+ TipoErro.LOOKAHEAD_OUT_OF_BOUND );
			

		} else if (classe.equals(tokens.get(lookahead).getLexema())) {
			if(lookahead>0 && lookahead<(tokens.size()-1)){
				if(tokens.get(lookahead-1).getLexema().equals(TipoToken.PARENTESE_E) && tokens.get(lookahead+1).getLexema().equals(TipoToken.PARENTESE_D)){
					desceUmNivel();
					elementoAtual.getTokens().add(tokens.get(lookahead));
					sobeUmNivel();
				}else{
					elementoAtual.getTokens().add(tokens.get(lookahead));
				}
			}else{			
			elementoAtual.getTokens().add(tokens.get(lookahead));
			}
			lookahead++;
		} else {

			BaseDeDados
					.putErroSintatico(BaseDeDados.getErrorsSintaticos().size(),new ErroSintatico(tokens.get(
							lookahead - 1).getColunaDeInicio(),
							TipoErro.ERRO_AO_RECONHECER_TOKEN, "Na coluna "
									+ (tokens.get(lookahead - 1)
											.getColunaDeInicio() + 1)
									+ " deveria ser um " + classe.toString()));
			
			throw new SintaticoException(""+ TipoErro.ERRO_AO_RECONHECER_TOKEN );

		}
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	
	public void relacao() throws SintaticoException{

		int estado = 0;
		
		desceUmNivel();
		
		while (true) {
			switch (estado) {
			case 0:
				if (tokens.get(lookahead).getLexema().equals(TipoToken.OP_PROJ)) {
					// leu operador PROJECT
					reconhecer(TipoToken.OP_PROJ);
					estado = 10;

				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.OP_SEL)) {
					// leu operador SELECT
					reconhecer(TipoToken.OP_SEL);
					estado = 20;

				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.OP_JUN)) {
					// leu operador JOIN
					reconhecer(TipoToken.OP_JUN);
					estado = 30;

				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.OP_REN)) {
					// leu operador RENAME
					reconhecer(TipoToken.OP_REN);
					estado = 50;

				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_E)) {
					// leu PARENTESE_ESQUERDA
					reconhecer(TipoToken.PARENTESE_E);
					estado = 60;
				} else {
					// leu outro Token
					BaseDeDados.putErroSintatico(BaseDeDados.getErrorsSintaticos().size(),new ErroSintatico(tokens.get(
							lookahead).getColunaDeInicio(),
							TipoErro.TOKEN_INVALIDO_PARA_INICIO_DE_RELACAO,
							"Na coluna "
									+ (tokens.get(lookahead)
											.getColunaDeInicio())
									+ " deveria ser um " + TipoToken.OP_PROJ
									+ " ou um " + TipoToken.OP_SEL + " ou um "
									+ TipoToken.OP_JUN + " ou um "
									+ TipoToken.OP_REN + " ou mesmo um "
									+ TipoToken.PARENTESE_E));
					
					throw new SintaticoException(									
									"Esperava um " + TipoToken.OP_PROJ
									+ " ou um " + TipoToken.OP_SEL + " ou um "
									+ TipoToken.OP_JUN + " ou um "
									+ TipoToken.OP_REN + " ou mesmo um "
									+ TipoToken.PARENTESE_E
									+ " na coluna " 									
									+ (tokens.get(lookahead)
											.getColunaDeInicio()));

				}
				break;

			/*
			 * Início do reconhecimento de PROJEÇÃO
			 */
			case 10:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.COLCHETE_E)) {
					reconhecer(TipoToken.COLCHETE_E);
					estado = 11;
				} else {

					erroEsperavaCOLCHETE_E();
					throw new SintaticoException("Esperava um "
							+ TipoToken.COLCHETE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
										
				}

				break;

			case 11:
				if (tokens.get(lookahead).getLexema().equals(TipoToken.ID)) {
					reconhecer(TipoToken.ID);
					estado = 12;
				} else {

					erroEsperavaID();
					throw new SintaticoException("Esperava um "
							+ TipoToken.ID
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
					
				}

				break;

			case 12:
				if (tokens.get(lookahead).getLexema().equals(TipoToken.VIRGULA)) {
					reconhecer(TipoToken.VIRGULA);
					estado = 13;
				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.COLCHETE_D)) {
					reconhecer(TipoToken.COLCHETE_D);
					estado = 14;
				} else {

					erroEsperavaCOLCHETE_D();
					throw new SintaticoException("Esperava um "
							+ TipoToken.VIRGULA
							+ " ou "+ TipoToken.COLCHETE_D+" na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
					
				}

				break;

			case 13:
				if (tokens.get(lookahead).getLexema().equals(TipoToken.ID)) {
					reconhecer(TipoToken.ID);
					estado = 12;
				} else {
					
					erroEsperavaID();
					throw new SintaticoException("Esperava um "
							+ TipoToken.ID
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
					
				}

				break;

			case 14:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_E)) {
					reconhecer(TipoToken.PARENTESE_E);
					estado = 15;
				} else {
					
					erroEsperavaPARENTESE_E();
					throw new SintaticoException("Esperava um "
							+ TipoToken.PARENTESE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
					
				}

				break;

			case 15:
				if (tokens.get(lookahead).getLexema().equals(TipoToken.ID)) {
					reconhecer(TipoToken.ID);
					estado = 16;
					
				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.OP_PROJ)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_SEL)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_JUN)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_REN)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.PARENTESE_E)) {

					relacao();
					lookahead = pilhaDeEstados.peek();
					pilhaDeEstados.pop();
					estado = 16;
				} else {
					
					erroAoEntrarEmNovaRELACAO();
					throw new SintaticoException("Esperava um "
							+ TipoToken.ID
							+ " ou "+TipoToken.OP_PROJ
							+ " ou "+TipoToken.OP_SEL
							+ " ou "+TipoToken.OP_JUN
							+ " ou "+TipoToken.OP_REN
							+ " ou mesmo um "+TipoToken.PARENTESE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
					
				}

				break;

			case 16:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_D)) {
					reconhecer(TipoToken.PARENTESE_D);
					estado = 17;
				} else {
					
					erroEsperavaPARENTESE_D();
					throw new SintaticoException( "Esperava um "
							+ TipoToken.PARENTESE_D
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
					
				}

				break;

			case 17:
				pilhaDeEstados.push(lookahead); // empilha estado
				lookahead = tokens.size() + 1; // força saída do 'while(true);'
				break;
			/*
			 * Início do reconhecimento de SELEÇÃO
			 */
			case 20:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.COLCHETE_E)) {
					reconhecer(TipoToken.COLCHETE_E);
					estado = 21;
				} else {
					
					erroEsperavaCOLCHETE_E();
					throw new SintaticoException("Esperava um "
							+ TipoToken.COLCHETE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 21:
				if (tokens.get(lookahead).getLexema().equals(TipoToken.ID)) {
					reconhecer(TipoToken.ID);
					estado = 22;
				} else {
					
					erroEsperavaID();
					throw new SintaticoException("Esperava um "
							+ TipoToken.ID
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 22:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.OP_RELACIONAL)) {
					reconhecer(TipoToken.OP_RELACIONAL);
					estado = 23;
				} else {
					
					erroEsperavaOP_RELACIONAL();
					throw new SintaticoException( "Esperava um "
							+ TipoToken.OP_RELACIONAL
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 23:
				if (tokens.get(lookahead).getLexema().equals(TipoToken.HORA)) {
					reconhecer(TipoToken.HORA);
					estado = 24;
				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.NUM)) {
					reconhecer(TipoToken.NUM);
					estado = 24;
				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.LITERAL)) {
					reconhecer(TipoToken.LITERAL);
					estado = 24;
				} else {
					
					erroEsperavaHORA_NUM_LITERAL();
					throw new SintaticoException("Esperava um "
							+ TipoToken.HORA
							+ " ou "+ TipoToken.NUM
							+ " ou mesmo um "+ TipoToken.LITERAL
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 24:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.COLCHETE_D)) {
					reconhecer(TipoToken.COLCHETE_D);
					estado = 25;
				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.AND)) {
					reconhecer(TipoToken.AND);
					estado = 21;
				}else if(tokens.get(lookahead).getLexema().equals(
						TipoToken.OR)){
					reconhecer(TipoToken.OR);
					estado = 21;
				} else {
					
					erroEsperavaCOLCHETE_D_OR_VIRGULA();
					throw new SintaticoException("Esperava um "
							+ TipoToken.COLCHETE_D
							+ " ou um" + TipoToken.OR
							+ " ou mesmo uma "+ TipoToken.VIRGULA
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 25:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_E)) {
					reconhecer(TipoToken.PARENTESE_E);
					estado = 26;
				} else {
					
					erroEsperavaPARENTESE_E();
					throw new SintaticoException("Esperava um "
							+ TipoToken.PARENTESE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 26:
				if (tokens.get(lookahead).getLexema().equals(TipoToken.ID)) {
					reconhecer(TipoToken.ID);
					estado = 27;
				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.OP_PROJ)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_SEL)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_JUN)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_REN)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.PARENTESE_E)) {

					relacao();
					lookahead = pilhaDeEstados.peek();
					pilhaDeEstados.pop();
					estado = 27;
				} else {
					
					erroAoEntrarEmNovaRELACAO();
					throw new SintaticoException("Esperava um "
							+ TipoToken.ID
							+ " ou "+TipoToken.OP_PROJ
							+ " ou "+TipoToken.OP_SEL
							+ " ou "+TipoToken.OP_JUN
							+ " ou "+TipoToken.OP_REN
							+ " ou mesmo um "+TipoToken.PARENTESE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
					
				}

				break;

			case 27:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_D)) {
					reconhecer(TipoToken.PARENTESE_D);
					estado = 28;
				} else {

					erroEsperavaPARENTESE_D();
					throw new SintaticoException( "Esperava um "
							+ TipoToken.PARENTESE_D
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
					
				}

				break;

			case 28:
				pilhaDeEstados.push(lookahead); // desempilha estado
				lookahead = tokens.size() + 1; // força saída do 'while(true);'

				break;
			/*
			 * Início do reconhecimento de JUNÇÃO
			 */
			case 30:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.COLCHETE_E)) {
					reconhecer(TipoToken.COLCHETE_E);
					estado = 31;
				} else {
					
					erroEsperavaCOLCHETE_E();
					throw new SintaticoException("Esperava um "
							+ TipoToken.COLCHETE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 31:
				if (tokens.get(lookahead).getLexema().equals(TipoToken.ID)) {
					reconhecer(TipoToken.ID);
					estado = 32;
				} else {
					
					erroEsperavaID();
					throw new SintaticoException("Esperava um "
							+ TipoToken.ID
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 32:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.OP_RELACIONAL)) {
					reconhecer(TipoToken.OP_RELACIONAL);
					estado = 33;
				} else {
					
					erroEsperavaOP_RELACIONAL();
					throw new SintaticoException( "Esperava um "
							+ TipoToken.OP_RELACIONAL
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
					
				}

				break;

			case 33:
				if (tokens.get(lookahead).getLexema().equals(TipoToken.ID)) {
					reconhecer(TipoToken.ID);
					estado = 34;
				} else {
					
					erroEsperavaID();
					throw new SintaticoException("Esperava um "
							+ TipoToken.ID
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 34:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.COLCHETE_D)) {
					reconhecer(TipoToken.COLCHETE_D);
					estado = 35;
				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.AND)) {
					reconhecer(TipoToken.AND);
					estado = 31;
				}else if(tokens.get(lookahead).getLexema().equals(TipoToken.OR)){
					reconhecer(TipoToken.OR);
					estado = 31;
				} else {
					
					erroEsperavaCOLCHETE_D_OR_VIRGULA();
					throw new SintaticoException("Esperava um "
							+ TipoToken.COLCHETE_D
							+ " ou um" + TipoToken.OR
							+ " ou mesmo uma "+ TipoToken.VIRGULA
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 35:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_E)) {
					reconhecer(TipoToken.PARENTESE_E);
					estado = 351;
				} else {
					
					erroEsperavaPARENTESE_E();
					throw new SintaticoException("Esperava um "
							+ TipoToken.PARENTESE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;
				
			case 351:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_E)) {
					reconhecer(TipoToken.PARENTESE_E);
					estado = 36;
				} else {
					
					erroEsperavaPARENTESE_E();
					throw new SintaticoException("Esperava um "
							+ TipoToken.PARENTESE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 36:
				if (tokens.get(lookahead).getLexema().equals(TipoToken.ID)) {
					reconhecer(TipoToken.ID);
					estado = 37;
				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.OP_PROJ)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_SEL)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_JUN)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_REN)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.PARENTESE_E)) {

					relacao();
					lookahead = pilhaDeEstados.peek();
					pilhaDeEstados.pop();
					estado = 37;
				} else {
					
					erroAoEntrarEmNovaRELACAO();
					throw new SintaticoException("Esperava um "
							+ TipoToken.ID
							+ " ou "+TipoToken.OP_PROJ
							+ " ou "+TipoToken.OP_SEL
							+ " ou "+TipoToken.OP_JUN
							+ " ou "+TipoToken.OP_REN
							+ " ou mesmo um "+TipoToken.PARENTESE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
					
				}

				break;

			case 37:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_D)) {
					reconhecer(TipoToken.PARENTESE_D);
					estado = 39;
				} else {
					
					erroEsperavaPARENTESE_D();
					throw new SintaticoException( "Esperava um "
							+ TipoToken.PARENTESE_D
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
					
				}

				break;

			case 39:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_E)) {
					reconhecer(TipoToken.PARENTESE_E);
					estado = 40;
				} else {

					erroEsperavaPARENTESE_E();
					throw new SintaticoException("Esperava um "
							+ TipoToken.PARENTESE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 40:
				if (tokens.get(lookahead).getLexema().equals(TipoToken.ID)) {
					reconhecer(TipoToken.ID);
					estado = 41;
				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.OP_PROJ)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_SEL)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_JUN)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_REN)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.PARENTESE_E)) {

					relacao();
					lookahead = pilhaDeEstados.peek();
					pilhaDeEstados.pop();
					estado = 41;
				} else {
					
					erroAoEntrarEmNovaRELACAO();
					throw new SintaticoException("Esperava um "
							+ TipoToken.ID
							+ " ou "+TipoToken.OP_PROJ
							+ " ou "+TipoToken.OP_SEL
							+ " ou "+TipoToken.OP_JUN
							+ " ou "+TipoToken.OP_REN
							+ " ou mesmo um "+TipoToken.PARENTESE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 41:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_D)) {
					reconhecer(TipoToken.PARENTESE_D);
					estado = 411;
				} else {
					
					erroEsperavaPARENTESE_D();
					throw new SintaticoException( "Esperava um "
							+ TipoToken.PARENTESE_D
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;
			case 411:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_D)) {
					reconhecer(TipoToken.PARENTESE_D);
					estado = 42;
				} else {
					
					erroEsperavaPARENTESE_D();
					throw new SintaticoException( "Esperava um "
							+ TipoToken.PARENTESE_D
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 42:
				pilhaDeEstados.push(lookahead); // desempilha estado
				lookahead = tokens.size() + 1; // força saída do 'while(true);'

				break;

			/*
			 * Início do reconhecimento de RENOMEAÇÃO
			 */
			case 50:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.COLCHETE_E)) {
					reconhecer(TipoToken.COLCHETE_E);
					estado = 51;
				} else {
					
					erroEsperavaCOLCHETE_E();
					throw new SintaticoException("Esperava um "
							+ TipoToken.COLCHETE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 51:
				if (tokens.get(lookahead).getLexema().equals(TipoToken.ID)) {
					reconhecer(TipoToken.ID);
					estado = 52;
				} else {
					
					erroEsperavaID();
					throw new SintaticoException("Esperava um "
							+ TipoToken.ID
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 52:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.COLCHETE_D)) {
					reconhecer(TipoToken.COLCHETE_D);
					estado = 53;
				} else {
					
					erroEsperavaCOLCHETE_D();
					throw new SintaticoException("Esperava um "
							+ TipoToken.VIRGULA
							+ " ou "+ TipoToken.COLCHETE_D+" na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 53:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_E)) {
					reconhecer(TipoToken.PARENTESE_E);
					estado = 54;
				} else {
					
					erroEsperavaPARENTESE_E();
					throw new SintaticoException("Esperava um "
							+ TipoToken.PARENTESE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 54:
				if (tokens.get(lookahead).getLexema().equals(TipoToken.ID)) {
					reconhecer(TipoToken.ID);
					estado = 55;
				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.OP_PROJ)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_SEL)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_JUN)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_REN)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.PARENTESE_E)) {

					relacao();
					lookahead = pilhaDeEstados.peek();
					pilhaDeEstados.pop();
					estado = 55;
				} else {
					
					erroAoEntrarEmNovaRELACAO();
					throw new SintaticoException("Esperava um "
							+ TipoToken.ID
							+ " ou "+TipoToken.OP_PROJ
							+ " ou "+TipoToken.OP_SEL
							+ " ou "+TipoToken.OP_JUN
							+ " ou "+TipoToken.OP_REN
							+ " ou mesmo um "+TipoToken.PARENTESE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 55:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_D)) {
					reconhecer(TipoToken.PARENTESE_D);
					estado = 56;
				} else {
					
					erroEsperavaPARENTESE_D();
					throw new SintaticoException( "Esperava um "
							+ TipoToken.PARENTESE_D
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 56:
				pilhaDeEstados.push(lookahead); // empilha estado
				lookahead = tokens.size() + 1; // força saída do 'while(true);'

				break;

			/*
			 * Início do reconhecimento de PRODUTO CARTESEANO OU DIVISÃO
			 */

			case 60:
				if (tokens.get(lookahead).getLexema().equals(TipoToken.ID)) {
					reconhecer(TipoToken.ID);
					estado = 61;
				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.OP_PROJ)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_SEL)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_JUN)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_REN)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.PARENTESE_E)) {

					relacao();
					lookahead = pilhaDeEstados.peek();
					pilhaDeEstados.pop();
					estado = 61;
				} else {
					
					erroAoEntrarEmNovaRELACAO();
					throw new SintaticoException("Esperava um "
							+ TipoToken.ID
							+ " ou "+TipoToken.OP_PROJ
							+ " ou "+TipoToken.OP_SEL
							+ " ou "+TipoToken.OP_JUN
							+ " ou "+TipoToken.OP_REN
							+ " ou mesmo um "+TipoToken.PARENTESE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 61:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_D)) {
					reconhecer(TipoToken.PARENTESE_D);
					estado = 62;
				} else {
					
					erroEsperavaPARENTESE_D();
					throw new SintaticoException( "Esperava um "
							+ TipoToken.PARENTESE_D
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 62:
				if (tokens.get(lookahead).getLexema()
						.equals(TipoToken.OP_PCART)) {
					reconhecer(TipoToken.OP_PCART);
					estado = 63;
				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.OP_DIV)) {
					reconhecer(TipoToken.OP_DIV);
					estado = 67;
				} else {
					
					erroEsperavaOP_PCART_OP_DIV();
					throw new SintaticoException("Esperava um "
							+ TipoToken.OP_PCART
							+ " ou mesmo um "+TipoToken.OP_DIV
							+ " na coluna "
							+ tokens.get(lookahead)
									.getColunaDeInicio());
				}

				break;

			case 63:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_E)) {
					reconhecer(TipoToken.PARENTESE_E);
					estado = 64;
				} else {
					
					erroEsperavaPARENTESE_E();
					throw new SintaticoException("Esperava um "
							+ TipoToken.PARENTESE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 64:
				if (tokens.get(lookahead).getLexema().equals(TipoToken.ID)) {
					reconhecer(TipoToken.ID);
					estado = 65;
				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.OP_PROJ)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_SEL)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_JUN)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_REN)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.PARENTESE_E)) {

					relacao();
					lookahead = pilhaDeEstados.peek();
					pilhaDeEstados.pop();
					estado = 65;
				} else {
					
					erroAoEntrarEmNovaRELACAO();
					throw new SintaticoException("Esperava um "
							+ TipoToken.ID
							+ " ou "+TipoToken.OP_PROJ
							+ " ou "+TipoToken.OP_SEL
							+ " ou "+TipoToken.OP_JUN
							+ " ou "+TipoToken.OP_REN
							+ " ou mesmo um "+TipoToken.PARENTESE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 65:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_D)) {
					reconhecer(TipoToken.PARENTESE_D);
					estado = 66;
				} else {
					
					erroEsperavaPARENTESE_D();
					throw new SintaticoException( "Esperava um "
							+ TipoToken.PARENTESE_D
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 66:
				pilhaDeEstados.push(lookahead); // empilha estado
				lookahead = tokens.size() + 1; // força saída do 'while(true);'

				break;

			case 67:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_E)) {
					reconhecer(TipoToken.PARENTESE_E);
					estado = 68;
				} else {
					
					erroEsperavaPARENTESE_E();
					throw new SintaticoException("Esperava um "
							+ TipoToken.PARENTESE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 68:
				if (tokens.get(lookahead).getLexema().equals(TipoToken.ID)) {
					reconhecer(TipoToken.ID);
					estado = 69;
				} else if (tokens.get(lookahead).getLexema().equals(
						TipoToken.OP_PROJ)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_SEL)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_JUN)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.OP_REN)
						|| tokens.get(lookahead).getLexema().equals(
								TipoToken.PARENTESE_E)) {

					relacao();
					lookahead = pilhaDeEstados.peek();
					pilhaDeEstados.pop();
					estado = 69;
				} else {
					
					erroAoEntrarEmNovaRELACAO();
					throw new SintaticoException("Esperava um "
							+ TipoToken.ID
							+ " ou "+TipoToken.OP_PROJ
							+ " ou "+TipoToken.OP_SEL
							+ " ou "+TipoToken.OP_JUN
							+ " ou "+TipoToken.OP_REN
							+ " ou mesmo um "+TipoToken.PARENTESE_E
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
					
					}

				break;

			case 69:
				if (tokens.get(lookahead).getLexema().equals(
						TipoToken.PARENTESE_D)) {
					reconhecer(TipoToken.PARENTESE_D);
					estado = 70;
				} else {
					
					erroEsperavaPARENTESE_D();
					throw new SintaticoException( "Esperava um "
							+ TipoToken.PARENTESE_D
							+ " na coluna "
							+ (tokens.get(lookahead)
									.getColunaDeInicio()));
				}

				break;

			case 70:
				pilhaDeEstados.push(lookahead); // empilha estado
				lookahead = tokens.size() + 1; // força saída do 'while(true);'

				break;

			default:
				break;
			}

			//TESTA SE É HORA DE SAIR DO LOOP OU NÃO
			if (lookahead > tokens.size()){
				break;
			}
				
		}//fim do switch
		
		sobeUmNivel();
	}//fim relacao()
	
	private void desceUmNivel(){
		elementoAtual.getTokens().add(null); 
		NoDaArvoreDeTraducao elementoAUX = new NoDaArvoreDeTraducao();
		elementoAtual.getNosFilhos().add(elementoAUX);
		elementoAUX.setElementoTradutorPai(elementoAtual);
		elementoAtual = elementoAtual.getNosFilhos()
				.get(elementoAtual.getNosFilhos().size() - 1);
	}
	
	private void sobeUmNivel(){
		elementoAtual = elementoAtual.getElementoTradutorPai();
	}
	
	/**
	 * 
	 */
	private void erroEsperavaOP_PCART_OP_DIV() {
		BaseDeDados.putErroSintatico(BaseDeDados.getErrorsSintaticos().size(),new ErroSintatico(tokens.get(
				lookahead).getColunaDeInicio(),
				TipoErro.TOKEN_INVALIDO, "Esperava um "
						+ TipoToken.OP_PCART
						+ " ou mesmo um "+TipoToken.OP_DIV
						+ " na coluna "
						+ (tokens.get(lookahead)
								.getColunaDeInicio())));		
	}



	/**
	 * 
	 */
	private void erroEsperavaCOLCHETE_D_OR_VIRGULA() {
		BaseDeDados.putErroSintatico(BaseDeDados.getErrorsSintaticos().size(),new ErroSintatico(tokens.get(
				lookahead).getColunaDeInicio(),
				TipoErro.TOKEN_INVALIDO, "Esperava um "
						+ TipoToken.COLCHETE_D
						+ " ou um" + TipoToken.OR
						+ " ou mesmo uma "+ TipoToken.VIRGULA
						+ " na coluna "
						+ (tokens.get(lookahead)
								.getColunaDeInicio())));
	}

	/**
	 * 
	 */
	private void erroEsperavaHORA_NUM_LITERAL() {
		BaseDeDados.putErroSintatico(BaseDeDados.getErrorsSintaticos().size(),new ErroSintatico(tokens.get(
				lookahead).getColunaDeInicio(),
				TipoErro.TOKEN_INVALIDO, "Esperava um "
						+ TipoToken.HORA
						+ " ou "+ TipoToken.NUM
						+ " ou mesmo um "+ TipoToken.LITERAL
						+ " na coluna "
						+ (tokens.get(lookahead)
								.getColunaDeInicio())));
	}

	/**
	 * 
	 */
	private void erroEsperavaOP_RELACIONAL() {
		BaseDeDados.putErroSintatico(BaseDeDados.getErrorsSintaticos().size(),new ErroSintatico(tokens.get(
				lookahead).getColunaDeInicio(),
				TipoErro.TOKEN_INVALIDO, "Esperava um "
						+ TipoToken.OP_RELACIONAL
						+ " na coluna "
						+ (tokens.get(lookahead)
								.getColunaDeInicio())));
	}

	/**
	 * 
	 */
	private void erroEsperavaPARENTESE_D() {
		BaseDeDados.putErroSintatico(BaseDeDados.getErrorsSintaticos().size(),new ErroSintatico(tokens.get(
				lookahead).getColunaDeInicio(),
				TipoErro.TOKEN_INVALIDO, "Esperava um "
				+ TipoToken.PARENTESE_D
				+ " na coluna "
				+ (tokens.get(lookahead)
						.getColunaDeInicio())));
	}

	/**
	 * 
	 */
	private void erroEsperavaPARENTESE_E() {
		BaseDeDados.putErroSintatico(BaseDeDados.getErrorsSintaticos().size(),new ErroSintatico(tokens.get(
				lookahead).getColunaDeInicio(),
				TipoErro.TOKEN_INVALIDO, "Esperava um "
						+ TipoToken.PARENTESE_E
						+ " na coluna "
						+ (tokens.get(lookahead)
								.getColunaDeInicio())));
	}

	/**
	 * 
	 */
	private void erroEsperavaCOLCHETE_D() {
		BaseDeDados.putErroSintatico(BaseDeDados.getErrorsSintaticos().size(),new ErroSintatico(tokens.get(
				lookahead).getColunaDeInicio(),
				TipoErro.TOKEN_INVALIDO, "Esperava um "
						+ TipoToken.VIRGULA
						+ " ou "+ TipoToken.COLCHETE_D+" na coluna "
						+ (tokens.get(lookahead)
								.getColunaDeInicio())));		
	}

	/**
	 * 
	 */
	private void erroEsperavaID() {
		BaseDeDados.putErroSintatico(BaseDeDados.getErrorsSintaticos().size(),new ErroSintatico(tokens.get(
				lookahead).getColunaDeInicio(),
				TipoErro.TOKEN_INVALIDO, "Esperava um "
						+ TipoToken.ID
						+ " na coluna "
						+ (tokens.get(lookahead)
								.getColunaDeInicio())));			
	}

	/**
	 * 
	 */
	private void erroEsperavaCOLCHETE_E() {
		BaseDeDados.putErroSintatico(BaseDeDados.getErrorsSintaticos().size(),new ErroSintatico(tokens.get(
				lookahead).getColunaDeInicio(),
				TipoErro.TOKEN_INVALIDO, "Esperava um "
						+ TipoToken.COLCHETE_E
						+ " na coluna "
						+ (tokens.get(lookahead)
								.getColunaDeInicio())));
	}

	/**
	 * 
	 */
	private void erroAoEntrarEmNovaRELACAO(){
		
		BaseDeDados.putErroSintatico(BaseDeDados.getErrorsSintaticos().size(),new ErroSintatico(tokens.get(
				lookahead).getColunaDeInicio(),
				TipoErro.TOKEN_INVALIDO, "Esperava um "
						+ TipoToken.ID
						+ " ou "+TipoToken.OP_PROJ
						+ " ou "+TipoToken.OP_SEL
						+ " ou "+TipoToken.OP_JUN
						+ " ou "+TipoToken.OP_REN
						+ " ou mesmo um "+TipoToken.PARENTESE_E
						+ " na coluna "
						+ (tokens.get(lookahead)
								.getColunaDeInicio())));
	}
	
	public NoDaArvoreDeTraducao getElementosTradutor() {
		return elementosParaTraducao;
	}

	public void setElementosTradutor(NoDaArvoreDeTraducao elementosTradutor) {
		this.elementosParaTraducao = elementosTradutor;
	}

}
