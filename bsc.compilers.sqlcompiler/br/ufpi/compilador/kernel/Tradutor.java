package br.ufpi.compilador.kernel;

import java.util.ArrayList;
import java.util.Collection;

import br.ufpi.compilador.beans.Token;
import br.ufpi.compilador.enums.TipoToken;
import br.ufpi.compilador.exceptions.SintaticoException;

public class Tradutor {

	public Collection<String> traduzir(String comando)
			throws SintaticoException {

		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(
				comando);

		analisadorSintatico.relacao();

		Collection<String> result = new ArrayList<String>();

		NoDaArvoreDeTraducao noTradutor = analisadorSintatico
				.getElementosTradutor();

		result = traduzArvoreDeTraducao(noTradutor);

		return result;
	}

	// sinaliza se � iniciado com PI
	boolean isSelection = true;

	/**
	 * Faz a tradução de fato.
	 * @param raizDaArvoreDeTraducao
	 * @return
	 */
	public Collection<String> traduzArvoreDeTraducao(NoDaArvoreDeTraducao raizDaArvoreDeTraducao) {

		Collection<String> result = new ArrayList<String>();

		if (raizDaArvoreDeTraducao.getTokens().get(0) == null) {

			return traduzArvoreDeTraducao(raizDaArvoreDeTraducao.getNosFilhos()
					.get(0));

		} else if (raizDaArvoreDeTraducao.getTokens().get(0).getLexema().equals(
				TipoToken.OP_PROJ)) {

			isSelection = false;

			result.add("SELECT");
			
			int j = 1;
			while (!raizDaArvoreDeTraducao.getTokens().get(j).getLexema().equals(
					TipoToken.PARENTESE_E)) {
				if(raizDaArvoreDeTraducao.getTokens().get(j).getLexema().equals(TipoToken.COLCHETE_D)||raizDaArvoreDeTraducao.getTokens().get(j).getLexema().equals(TipoToken.COLCHETE_E)){
					j++;
					continue;
				}else{
				result.add(raizDaArvoreDeTraducao.getTokens().get(j).getAtributo());
				}
				j++;
			}

			result.add("FROM");

			result.addAll(traduzArvoreDeTraducao(raizDaArvoreDeTraducao
					.getNosFilhos().get(0)));

			return result;

		} else if (raizDaArvoreDeTraducao.getTokens().get(0).getLexema().equals(
				TipoToken.OP_SEL)) {

			if (isSelection) {
				result.add("SELECT");
				result.add("*");
				result.add("FROM");
			}

			result.addAll(traduzArvoreDeTraducao(raizDaArvoreDeTraducao
					.getNosFilhos().get(0)));

			result.add("WHERE");

			for (int j = 1; j < (raizDaArvoreDeTraducao.getTokens().size() - 3); j++) {
				
				if(raizDaArvoreDeTraducao.getTokens().get(j).getLexema().equals(TipoToken.COLCHETE_D)||raizDaArvoreDeTraducao.getTokens().get(j).getLexema().equals(TipoToken.COLCHETE_E)){
					continue;
				}else{
					if(raizDaArvoreDeTraducao.getTokens().get(j).getLexema().equals(TipoToken.OR)){
						result.add("OR");
					}else if(raizDaArvoreDeTraducao.getTokens().get(j).getLexema().equals(TipoToken.AND)){
						result.add("AND");
					}
					else{
					result.add(raizDaArvoreDeTraducao.getTokens().get(j).getAtributo());
					}
				}
			}

			return result;

		} else if (raizDaArvoreDeTraducao.getTokens().size() == 1) {

			result.add(raizDaArvoreDeTraducao.getTokens().get(0).getAtributo());

			return result;

		} else {

			if (isSelection
					&& raizDaArvoreDeTraducao.getElementoTradutorPai()
							.getElementoTradutorPai() == null) {
				result.add("SELECT");
				result.add("*");
				result.add("FROM");
				isSelection = false;

			}
			int i = 0;
			int lmento = 0;

			for (Token elemento : raizDaArvoreDeTraducao.getTokens()) {
				if (elemento == null) {

					result.addAll(traduzArvoreDeTraducao(raizDaArvoreDeTraducao
							.getNosFilhos().get(i)));
					lmento++;
					i++;

				} else if (elemento.getLexema().equals(TipoToken.ID)) {

					result.add(elemento.getAtributo());
					lmento++;

				} else if (elemento.getLexema().equals(TipoToken.OP_PCART)) {

					lmento++;
					result.add(",");

				} else if (elemento.getLexema().equals(TipoToken.PARENTESE_E)) {

					lmento++;
					result.add(elemento.getAtributo());

								
				} else if (elemento.getLexema()

				.equals(TipoToken.PARENTESE_D)) {
					lmento++;
					result.add(elemento.getAtributo());

				} else if (elemento.getLexema().equals(TipoToken.OP_JUN)) {

					lmento++;
					
					result.addAll(traduzArvoreDeTraducao(raizDaArvoreDeTraducao
							.getNosFilhos().get(i)));
					result.add("JOIN");
					result.addAll(traduzArvoreDeTraducao(raizDaArvoreDeTraducao
							.getNosFilhos().get(i+1)));
					result.add("ON");

					for (int j = lmento; j < (raizDaArvoreDeTraducao.getTokens()
							.size() - 8); j++) {

						if(raizDaArvoreDeTraducao.getTokens().get(j).getLexema().equals(TipoToken.COLCHETE_D)||raizDaArvoreDeTraducao.getTokens().get(j).getLexema().equals(TipoToken.COLCHETE_E)){
							continue;
						}else{
							if(raizDaArvoreDeTraducao.getTokens().get(j).getLexema().equals(TipoToken.OR)){
								result.add("OR");
							
							}	else if(raizDaArvoreDeTraducao.getTokens().get(j).getLexema().equals(TipoToken.AND)){
								result.add("AND");
							}
							
							else  {
							result.add(raizDaArvoreDeTraducao.getTokens().get(j).getAtributo());
							}
						}
					}

					return result;

				}

			}

		}

		return result;

	}
}
