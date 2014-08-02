package br.ufpi.compilador.basededados;

import java.util.ArrayList;

import br.ufpi.compilador.beans.Erro;
import br.ufpi.compilador.beans.ErroSintatico;
import br.ufpi.compilador.beans.Token;
import br.ufpi.compilador.exceptions.NotFoundErroLexicoException;
import br.ufpi.compilador.exceptions.NotFoundErroSintaticoException;
import br.ufpi.compilador.exceptions.NotFoundTokenException;

/**
 * A classe BaseDeDados foi projetada para armazenar todos os possíveis Tokens
 * gerados pelo Analizador Léxico do Compitlador, bem como todos os erros
 * encontrados por este.
 * 
 * @author Alcemir Rodrigues Santos  (06N10340) 
 * @author Janielton de Sousa Veloso (06N10242) 
 * 
 */
public class BaseDeDados {
	
	private static ArrayList<Token> tokens = new ArrayList<Token>();
	private static ArrayList<Erro> errorsLexicos = new ArrayList<Erro>();
	private static ArrayList<ErroSintatico> errorsSintaticos = new ArrayList<ErroSintatico>();
	
	

	/**
	 * Adiciona novo erro à coleção de erros Léxicos;
	 * @param novoErro
	 */
	public static void putErroLexico(int k, Erro novoErro) {		
		getErrorsLexicos().add(k, novoErro);
	}
	
	/**
	 * Adiciona novo erro à coleção de erros Sintáticos;
	 * @param novoErro
	 */
	public static void putErroSintatico(int k, ErroSintatico novoErro) {		
		getErrorsSintaticos().add(k, novoErro);
	}

	/**
	 * Adiciona token à coleção de erros Sintáticos;
	 * @param novoToken
	 */
	public static void putToken(int k,Token novoToken) {
		getTokens().add(k, novoToken);
	}

	public static void clearErrorsLexicos() {
		errorsLexicos.clear();
	}
	
	public static void clearErrorsSintaticos() {
		errorsSintaticos.clear();
	}

	public static void clearTokens() {
		tokens.clear();
	}

	/**
	 * Retorna erro lexico referente a chave passada
	 * @param key
	 * @return
	 * @throws NotFoundErroLexicoException
	 */
	public static Erro getErroLexico(int key) throws NotFoundErroLexicoException {
		//if (getErrorsLexicos().containsKey(key)) {
			try{
				return getErrorsLexicos().get(key);
			}catch (Exception e) {
				throw new NotFoundErroLexicoException();
			}
	}
	
	/**
	 * Retorna erro sintatico referente a chave passada
	 * @param key
	 * @return
	 * @throws NotFoundErroLexicoException
	 */
	public static ErroSintatico getErroSintatico(int key) throws NotFoundErroSintaticoException {
		//if (getErrorsLexicos().containsKey(key)) {
		try{
			return getErrorsSintaticos().get(key);
		}catch (Exception e) {			
			throw new NotFoundErroSintaticoException();
		}
	}

	/**
	 * Retorna token referente a chave passada
	 * @param key
	 * @return
	 * @throws NotFoundErroLexicoException
	 */
	public static Token getToken(int idToken) throws NotFoundTokenException {
		//if (getTokens().containsKey(idToken)) {
		try{
			return getTokens().get(idToken);
		//} else {
		}catch (Exception e) {
			throw new NotFoundTokenException();
		}
	}

	public static void setErrorsLexicos(ArrayList<Erro> errors) {
		BaseDeDados.errorsLexicos = errors;
	}

	public static ArrayList<Erro> getErrorsLexicos() {
		return errorsLexicos;
	}

	public static ArrayList<ErroSintatico> getErrorsSintaticos() {
		return errorsSintaticos;
	}

	public static void setErrorsSintaticos(ArrayList<ErroSintatico> errorsSintaticos) {
		BaseDeDados.errorsSintaticos = errorsSintaticos;
	}

	public static void setTokens(ArrayList<Token> token) {
		BaseDeDados.tokens = token;
	}

	public static ArrayList<Token> getTokens() {
		return tokens;
	}

}
