package br.ufpi.compilador.exceptions;


import br.ufpi.compilador.beans.Token;
import br.ufpi.compilador.enums.TipoToken;

@SuppressWarnings("serial")
public class SintaticoException extends Exception {

	public SintaticoException(String s) {
		super(s);

	}

	public SintaticoException(Token token, TipoToken classe)
			throws SintaticoException {
		String mensagem = "Na coluna "
				+ token.getColunaDeInicio() + " deveria ser uma classe "
				+ classe.toString();

		throw new SintaticoException(mensagem);
	}
}
