package br.ufpi.compilador.view;

import javax.swing.JPanel;
import javax.swing.JFrame;


import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import br.ufpi.compilador.basededados.BaseDeDados;
import br.ufpi.compilador.beans.Erro;
import br.ufpi.compilador.beans.ErroSintatico;
import br.ufpi.compilador.beans.Token;
import br.ufpi.compilador.exceptions.NotFoundErroLexicoException;
import br.ufpi.compilador.exceptions.NotFoundErroSintaticoException;
import br.ufpi.compilador.exceptions.NotFoundTokenException;
import br.ufpi.compilador.exceptions.SintaticoException;
import br.ufpi.compilador.kernel.Tradutor;

import java.awt.Color;
import javax.swing.border.SoftBevelBorder;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import java.awt.event.KeyEvent;
import java.awt.Font;
import javax.swing.JTextPane;

public class Tela extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel jPanel = null;
	private JLabel lblExpressaoAlgebraLinear = null;
	private JTextField txtEntrada = null;
	private JTabbedPane lexico = null;
	private JPanel jPanel1 = null;
	private JPanel jPanel2 = null;
	private JPanel jPanel3 = null;
	private JTable tblTokens = null;
	private JScrollPane scrpnlExibeTokens = null;
	private JButton btnCompila = null;
	private JScrollPane scrpnlErrosLexico = null;
	private JTable tblErrosLexicos = null;
	private JLabel lblErros = null;
	private JLabel lblStatus = null;
	private JLabel lblMensagemStatus = null;

	/**
	 * This is the default constructor
	 */
	public Tela() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(712, 443);
		this.setContentPane(getJContentPane());
		this.setTitle("Compilador - Alcemir e Janielton");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			lblErros = new JLabel();
			lblErros.setText("Possíveis Erros Léxicos:");
			lblErros.setBounds(new Rectangle(20, 164, 155, 16));
			lblExpressaoAlgebraLinear = new JLabel();
			lblExpressaoAlgebraLinear.setBounds(new Rectangle(12, 8, 293, 26));
			lblExpressaoAlgebraLinear
					.setText("Digite sua Expressão em ÁLGEBRA RELACIONAL:");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.setBackground(new Color(135, 135, 135));
			jContentPane.add(getJPanel(), null);
			jContentPane.add(lblExpressaoAlgebraLinear, null);
			jContentPane.add(getTxtEntrada(), null);
			jContentPane.add(getLexico(), null);
			jContentPane.add(getBtnCompila(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			jPanel = new JPanel();
			jPanel.setBounds(new Rectangle(0, 0, 0, 0));
			jPanel.setLayout(gridLayout);
		}
		return jPanel;
	}

	/**
	 * This method initializes txtEntrada
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtEntrada() {
		if (txtEntrada == null) {
			txtEntrada = new JTextField();
			txtEntrada.setBounds(new Rectangle(4, 36, 693, 26));
		}
		return txtEntrada;
	}

	/**
	 * This method initializes lexico
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getLexico() {
		if (lexico == null) {
			lexico = new JTabbedPane();
			lexico.setBounds(new Rectangle(6, 106, 693, 306));
			lexico.setName("lexico");
			lexico.setTabPlacement(JTabbedPane.TOP);
			lexico.setToolTipText("");
			lexico.addTab("1. Análise Léxica", null, getJPanel1(), null);
			lexico.addTab("2. Análise Sintática", null, getJPanel2(), null);
			lexico.addTab("3. Tradução Dirigida", null, getJPanel3(), null);
		}
		return lexico;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			lblTokens = new JLabel();
			lblTokens.setBounds(new Rectangle(19, 6, 129, 16));
			lblTokens.setText("Tokens Obtidos:");
			jPanel1 = new JPanel();
			jPanel1.setLayout(null);
			jPanel1.setName("");
			jPanel1.setBackground(new Color(135, 135, 135));
			jPanel1.add(getScrpnlExibeTokens(), null);
			jPanel1.add(getScrpnlErrosLexico(), null);
			jPanel1.add(lblErros, null);
			jPanel1.add(lblTokens, null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			lblErrosSintaticos = new JLabel();
			lblErrosSintaticos.setBounds(new Rectangle(18, 79, 158, 16));
			lblErrosSintaticos.setText("Possíveis Erros Sintáticos:");
			lblMensagemStatus = new JLabel();
			lblMensagemStatus.setBounds(new Rectangle(103, 15, 567, 44));
			lblMensagemStatus.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
			lblMensagemStatus.setForeground(new Color(241, 0, 0));
			lblMensagemStatus.setFont(new Font("Dialog", Font.BOLD
					| Font.ITALIC, 18));
			lblMensagemStatus.setBackground(Color.white);
			lblMensagemStatus.setText("");
			lblStatus = new JLabel();
			lblStatus.setBounds(new Rectangle(17, 15, 80, 43));
			lblStatus.setText("Status:");
			jPanel2 = new JPanel();
			jPanel2.setLayout(null);
			jPanel2.setBackground(new Color(135, 135, 135));
			jPanel2.add(lblStatus, null);
			jPanel2.add(lblMensagemStatus, null);
			jPanel2.add(getScrpnlErrosSintaticos(), null);
			jPanel2.add(lblErrosSintaticos, null);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jPanel3
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			lblTituloSQL = new JLabel();
			lblTituloSQL.setBounds(new Rectangle(16, 29, 432, 16));
			lblTituloSQL.setFont(new Font("Dialog", Font.BOLD, 14));
			lblTituloSQL.setText("Expressão traduzida em SQL:");
			jPanel3 = new JPanel();
			jPanel3.setLayout(null);
			jPanel3.setBackground(new Color(135, 135, 135));
			jPanel3.add(lblTituloSQL, null);
			jPanel3.add(getTxtpSQL(), null);
		}
		return jPanel3;
	}

	/**
	 * This method initializes tblTokens
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable gettblTokens(String[] columnNames, Object[][] data) {

		if (tblTokens == null) {
			tblTokens = new JTable(data, columnNames);
		}
		return tblTokens;
	}

	/**
	 * This method initializes scrpnlExibeTokens
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScrpnlExibeTokens() {
		if (scrpnlExibeTokens == null) {
			scrpnlExibeTokens = new JScrollPane();
			scrpnlExibeTokens.setBounds(new Rectangle(3, 24, 681, 135));
			// scrpnlExibeTokens.setViewportView(gettblTokens());
			scrpnlExibeTokens.setBorder(BorderFactory
					.createBevelBorder(BevelBorder.RAISED));
		}
		return scrpnlExibeTokens;
	}

	/**
	 * This method initializes scrpnlErrosLexico
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScrpnlErrosLexico() {
		if (scrpnlErrosLexico == null) {
			scrpnlErrosLexico = new JScrollPane();
			scrpnlErrosLexico.setBounds(new Rectangle(2, 183, 682, 90));
			// scrpnlErrosLexico.setViewportView(getTblErrosLexicos(null,null));
			scrpnlErrosLexico.setBorder(new SoftBevelBorder(
					SoftBevelBorder.LOWERED));
		}
		return scrpnlErrosLexico;
	}

	/**
	 * This method initializes tblErrosLexicos1
	 * 
	 * @param data
	 * @param columnNames
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getTblErrosLexicos(String[] columnNames, Object[][] data) {
		if (tblErrosLexicos == null) {
			tblErrosLexicos = new JTable(data, columnNames);
			tblErrosLexicos.setForeground(Color.red);
		}
		return tblErrosLexicos;
	}

	private Object[][] dataTokens = null;
	private Object[][] dataErrosLexicos = null;
	private Object[][] dataErrosSintaticos = null;
	private String[] columnNamesTokens = { "Atributo", "Coluna", "Lexema" };
	private String[] columnNamesErros = { "Mensagem" };
	private JScrollPane scrpnlErrosSintaticos = null;
	private JTable tblErrosSintaticos = null;
	private JLabel lblTokens = null;
	private JLabel lblErrosSintaticos = null;
	private JLabel lblTituloSQL = null;
	private JTextPane txtpSQL = null;
	/**
	 * Preenche tabela de Tokens
	 * 
	 * @param tokensObtidos
	 * @throws NotFoundTokenException 
	 */
	public void preencheTabelaTokens(ArrayList<Token> tokensObtidos) throws NotFoundTokenException {
		tblTokens = null;

		

		Token maisUmToken;
		if(tokensObtidos.size()>0) {
			dataTokens = new Object[tokensObtidos.size()][3];
			for (int j = 0; j < tokensObtidos.size(); j++) {
				maisUmToken = BaseDeDados.getToken(j);
				dataTokens[j][0] = maisUmToken.getAtributo();
				dataTokens[j][1] = maisUmToken.getColunaDeInicio();
				dataTokens[j][2] = maisUmToken.getLexema();
			}
		} else {
			dataTokens = new Object[1][1];
			dataTokens[0][0] = new NotFoundTokenException().toString();
		}

		scrpnlExibeTokens.setViewportView(gettblTokens(columnNamesTokens,
				dataTokens));

	}

	/**
	 * 
	 * @param errosLexicos
	 * @throws NotFoundErroLexicoException 
	 */
	private void preencheTabelaErrosLexicos(ArrayList<Erro> errosLexicos) throws NotFoundErroLexicoException {
		tblErrosLexicos = null;

		
		if(errosLexicos.size()>0){
			dataErrosLexicos = new Object[errosLexicos.size()][1];
			for (int i = 0; i < errosLexicos.size(); i++) {

				Erro maisUmErro = BaseDeDados.getErroLexico(i);
				dataErrosLexicos[i][0] = maisUmErro.getMensagem();
			}
		}else{
			dataErrosLexicos = new Object[1][1];
			dataErrosLexicos[0][0] = new NotFoundErroLexicoException().toString();
		}
		

		scrpnlErrosLexico.setViewportView(getTblErrosLexicos(columnNamesErros,
				dataErrosLexicos));

	}

	/**
	 * 
	 * @param errosSintaticos
	 * @throws NotFoundErroSintaticoException
	 */
	private void preencheTabelaErrosSintaticos(
			ArrayList<ErroSintatico> errosSintaticos)
			throws NotFoundErroSintaticoException {
		tblErrosSintaticos = null;

		
		if(errosSintaticos.size()>0) {
			dataErrosSintaticos = new Object[errosSintaticos.size()][1];
			for (int i = 0; i < errosSintaticos.size(); i++) {

				ErroSintatico maisUmErro = BaseDeDados.getErroSintatico(i);
				dataErrosSintaticos[i][0] = maisUmErro.getMensagem();
			}
		} else {
			dataErrosSintaticos = new Object[1][1];
			dataErrosSintaticos[0][0] = new NotFoundErroSintaticoException().toString();
		}

		scrpnlErrosSintaticos.setViewportView(getTblErrosSintaticos(
				columnNamesErros, dataErrosSintaticos));

	}

	/**
	 * This method initializes btnCompila
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnCompila() {
		if (btnCompila == null) {
			btnCompila = new JButton();
			btnCompila.setBounds(new Rectangle(610, 73, 87, 29));
			btnCompila.setText("Analisa");
			btnCompila.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Tradutor tradutor = new Tradutor();
					String comando = txtEntrada.getText();
					limpaTabelas();
					txtpSQL.setText("");

					if (txtEntrada.getText().equals("")) {
						JOptionPane
								.showMessageDialog(
										null,
										"CUIDADO: você não deve deixar o campo em branco",
										"Erro", JOptionPane.INFORMATION_MESSAGE);
					} else if (comando.charAt(comando.length() - 1) != ';') {
						JOptionPane
								.showMessageDialog(
										null,
										"CUIDADO: Você deve sempre digitar um ;(ponto e virgula) ao final do comando para indicar o término deste.",
										"Erro", JOptionPane.INFORMATION_MESSAGE);
					} else {
						
						try {
							// chama o Tradutor Dirigido pela sintaxe.						
							ArrayList<String> a = (ArrayList<String>) tradutor.traduzir(comando);
							
							for (String itemTraduzido : a) {
								txtpSQL.setText(txtpSQL.getText()+" "+itemTraduzido);								
							}
							// preenche a tabela de Tokens encontrados
							preencheTabelaTokens(BaseDeDados.getTokens());
							// preenche a tabela de possíveis erros Léxicos
							preencheTabelaErrosLexicos(BaseDeDados
									.getErrorsLexicos());
							// prenche a tabela de erros sintáticos
							preencheTabelaErrosSintaticos(BaseDeDados
									.getErrorsSintaticos());						
						} catch (SintaticoException e1) {
							e1.printStackTrace();
							txtpSQL.setText("Erro sintático. Favor verificar.");
						}catch (NotFoundTokenException e2) {
							e2.printStackTrace();
						} catch (NotFoundErroLexicoException e3) {							
							e3.printStackTrace();
						} catch (NotFoundErroSintaticoException e4) {
							e4.printStackTrace();
						}finally{
							// preenche a tabela de Tokens encontrados
							try {
								preencheTabelaTokens(BaseDeDados.getTokens());
								// preenche a tabela de possíveis erros Léxicos
								preencheTabelaErrosLexicos(BaseDeDados
										.getErrorsLexicos());
								// prenche a tabela de erros sintáticos
								preencheTabelaErrosSintaticos(BaseDeDados
									.getErrorsSintaticos());	
						
							} catch (NotFoundTokenException e1) {							
								e1.printStackTrace();
							} catch (NotFoundErroLexicoException e2) {								
								e2.printStackTrace();
							} catch (NotFoundErroSintaticoException e3) {
								e3.printStackTrace();
							}
							
						}	
						
						// tratamento da tela de Analise Sintática
						if (BaseDeDados.getErrorsSintaticos().size() != 0) {
							lblMensagemStatus.setText("Existe ERRO SINTÁTICO em sua expressão agébrica.");
							JOptionPane
									.showMessageDialog(
											null,
											"Foram encontrados alguns erros sintáticos em sua expressão de álgebra relacional. Favor verificar a tabela de \"Possíveis Erros Sintáticos\".",
											"Erros Encontrados!",
											JOptionPane.ERROR_MESSAGE);
						} else {
							lblMensagemStatus
									.setText("Análise Sintática executada com sucesso.");
						}

						BaseDeDados.clearTokens();
						BaseDeDados.clearErrorsLexicos();
						BaseDeDados.clearErrorsSintaticos();

					}

				}

				
			});
		}
		return btnCompila;
	}
	
	/**
	 * 
	 */
	private void limpaTabelas() {		
		
		Object[][] data = new Object[1][1];
		Object[][] dataTokenss = new Object[1][3];
		
		data[0][0] = " ";
		dataTokenss[0][0] = " ";
		dataTokenss[0][1] = " ";
		dataTokenss[0][2] = " ";
		
		scrpnlExibeTokens.setViewportView(getTblErrosSintaticos(
				columnNamesTokens, dataTokenss));

		scrpnlErrosLexico.setViewportView(getTblErrosSintaticos(
				columnNamesErros, data));

		scrpnlErrosSintaticos.setViewportView(getTblErrosSintaticos(
				columnNamesErros, data));
						
	}
	
	/**
	 * This method initializes scrpnlErrosSintaticos
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScrpnlErrosSintaticos() {
		if (scrpnlErrosSintaticos == null) {
			scrpnlErrosSintaticos = new JScrollPane();
			scrpnlErrosSintaticos.setBounds(new Rectangle(3, 100, 682, 171));
			// scrpnlErrosSintaticos.setViewportView(getTblErrosSintaticos1());
			scrpnlErrosSintaticos.setBorder(new SoftBevelBorder(
					SoftBevelBorder.LOWERED));
		}
		return scrpnlErrosSintaticos;
	}

	/**
	 * This method initializes tblErrosSintaticos1
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getTblErrosSintaticos(String[] columnNames, Object[][] data) {
		if (tblErrosSintaticos == null) {
			tblErrosSintaticos = new JTable(data, columnNames);
			tblErrosSintaticos.setForeground(Color.red);
		}
		return tblErrosSintaticos;
	}

	/**
	 * This method initializes txtpSQL	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextPane getTxtpSQL() {
		if (txtpSQL == null) {
			txtpSQL = new JTextPane();
			txtpSQL.setBounds(new Rectangle(15, 56, 660, 90));
			txtpSQL.setFont(new Font("Dialog", Font.PLAIN, 18));
			txtpSQL.setEditable(false);
			txtpSQL.setForeground(new Color(51, 51, 187));
			txtpSQL.setBackground(Color.lightGray);
			txtpSQL.setText("");
		}
		return txtpSQL;
	}

} // @jve:decl-index=0:visual-constraint="21,8"
