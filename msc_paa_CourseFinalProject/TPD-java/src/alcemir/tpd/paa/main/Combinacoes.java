package alcemir.tpd.paa.main;

import java.util.Set;
import java.util.TreeSet;

public class Combinacoes {


	private Set<String> possiveis = new TreeSet<String>();
	private int[] numeros;
	private int quantidade;
	private int[] resultado;

	private int count = 0;
	/**
	 * Contrutor da classe.
	 * @param de
	 * @param a
	 */
	public Combinacoes(int de, int a){
		quantidade = a;
		resultado = new int[a];
		numeros = new int[de];
		for (int i = 0; i < numeros.length; i++) {
			numeros[i] = i+1;			
		}
	}
	
	public Set<String> getPossiveis() {
		return possiveis;
	}

	public int getCount() {
		return count;
	}

	public void possiveis(int inicio, int fim, int profundidade) {
		if ((profundidade + 1) >= quantidade)
			for (int x = inicio; x <= fim; x++) {
				resultado[profundidade] = numeros[x];
				// faz alguma coisa com um dos resultados possiveis
				count++;
				String s = "";
				for (int i = 0; i < resultado.length; i++) {
					s += resultado[i]+" ";					               
				}
				possiveis.add(s);
			}
		else
			for (int x = inicio; x <= fim; x++) {
				resultado[profundidade] = numeros[x];
				possiveis(x + 1, fim + 1, profundidade + 1);
			}
	}


}