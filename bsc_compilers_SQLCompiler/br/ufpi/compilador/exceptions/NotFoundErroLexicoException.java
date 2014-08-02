package br.ufpi.compilador.exceptions;

/**
 * Erro não encontrado.
 * 
 * @author Alcemir Rodrigues Santos  (06N10340) 
 * @author Janielton de Sousa Veloso (06N10242)
 * 
 *
 */
@SuppressWarnings("serial")
public class NotFoundErroLexicoException extends Exception {

    @Override
    public String toString() {
        return "Não há erros léxicos!";
    }
}
