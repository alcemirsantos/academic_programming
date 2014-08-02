package br.ufpi.compilador.exceptions;

/**
 * Token não encontrado.
 * 
 * @author Alcemir Rodrigues Santos  (06N10340) 
 * @author Janielton de Sousa Veloso (06N10242)
 * 
 */
@SuppressWarnings("serial")
public class NotFoundTokenException extends Exception {
	
	@Override
    public String toString() {
        return "Não há tokens!";
    }
}
