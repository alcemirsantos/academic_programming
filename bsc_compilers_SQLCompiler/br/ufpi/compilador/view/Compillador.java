package br.ufpi.compilador.view;

import javax.swing.JFrame;


public class Compillador {

	/**
	 * Função Principal do Compilador.
	 * 
	 * @param args
	 *
	 * @author Alcemir Rodrigues Santos  (06N10340)
	 * @author Janielton de Sousa Veloso (06N10242)
	 * 
	 */
	public static void main(String[] args) {
		
		Tela telaDoCompilador = new Tela();		
		
		// mostrar na tela
		telaDoCompilador.setVisible(true);		
		// modo padrão de fechar a janela 
		telaDoCompilador.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		// posicionar ao centro da tela
		telaDoCompilador.setLocationRelativeTo(null);

	}
}