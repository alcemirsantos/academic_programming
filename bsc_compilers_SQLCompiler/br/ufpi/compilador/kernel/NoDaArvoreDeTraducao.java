package br.ufpi.compilador.kernel;

import java.util.ArrayList;

import br.ufpi.compilador.beans.Token;

public class NoDaArvoreDeTraducao {

	private ArrayList<Token> Tokens = new ArrayList<Token>();

	private ArrayList<NoDaArvoreDeTraducao> nosFilhosDeTraducao = new ArrayList<NoDaArvoreDeTraducao>();

	private NoDaArvoreDeTraducao noPaiDesteNoDeTraducao;

	public NoDaArvoreDeTraducao getElementoTradutorPai() {
		return noPaiDesteNoDeTraducao;
	}

	public void setElementoTradutorPai(NoDaArvoreDeTraducao elementoTradutorPai) {
		this.noPaiDesteNoDeTraducao = elementoTradutorPai;
	}

	public ArrayList<Token> getTokens() {
		return Tokens;
	}

	public void setTokens(ArrayList<Token> tokens) {
		Tokens = tokens;
	}

	public ArrayList<NoDaArvoreDeTraducao> getNosFilhos() {
		return nosFilhosDeTraducao;
	}

	public void setElementosTradutor(
			ArrayList<NoDaArvoreDeTraducao> elementosTradutor) {
		this.nosFilhosDeTraducao = elementosTradutor;
	}

}
