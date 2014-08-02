package br.ufpi.compilador.testes;

import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.ufpi.compilador.exceptions.SintaticoException;
import br.ufpi.compilador.kernel.Tradutor;

public class TesteTradutor {

	@DataProvider(name = "testaTradutor")
	public Object[][] testaTradutorDataProvider() {

		Object[][] result = new Object[6][2];
		
		result[0] = new Object[] {
				"PROJECT [nomes, salarios](RENAME [pega.Porra](SELECT [sal>123,nome='alcemir'](JOIN [tabela = tabela]((tabelinha)(outra)))));",
				new MyArrayList(new String[] { "SELECT", "[", "nomes", ",",
						"salarios", "]", "FROM", "pega.Porra", "(", "tabelinha",
						"JOIN", "outra", "ON", "[", "tabela", "=", "tabela",
						"]", "WHERE", "[", "sal", ">", "123", ",", "nome", "=",
						"'alcemir'", "]", ")" }) };
		
		result[1] = new Object[] {"PROJECT [nome,sal](empregados);", new MyArrayList(new String[]{"SELECT","[","nome",",","sal","]","FROM","empregados"})};
		result[2] = new Object[] {"SELECT [nome = 'Alcemir'](fuckingDevelopers);",new MyArrayList(new String[]{"SELECT", "*", "FROM", "fuckingDevelopers", "WHERE", "[","nome","=","'Alcemir'","]"})};
		result[3] = new Object[] {"RENAME [alcemir](ALCEMIR);",new MyArrayList(new String[]{"SELECT","*","FROM","alcemir","(","ALCEMIR",")"})};
		result[4] = new Object[] {"JOIN [tabelinha = tabelao]((tabela1)(tabela2));",new MyArrayList(new String[]{"SELECT","*","FROM","tabela1","JOIN","tabela2","ON","[","tabelinha","=","tabelao","]"})};
		result[5] = new Object[] {"(tabelinha)TIMES(tabelao);",new MyArrayList(new String[]{"SELECT","*","FROM","(","tabelinha",")",",","(","tabelao",")"})};
		//result[6] = new Object[] {"(tabelinha)DIVISION(tabelao);",new MyArrayList(new String[]{""})};
		
//		result[1] = new Object[] {"PROJECT [nome,sal](empregados);",new MyArrayList(new String[]{"SELECT","[","nome",",","sal","]","FROM","empregados"})};
//		result[2] = new Object[] {"SELECT [nome = 'Alcemir'](fuckingDevelopers);",new MyArrayList(new String[]{"SELECT", "*", "FROM", "fuckingDevelopers", "WHERE", "[","nome","=","'Alcemir'","]"})};
//		result[3] = new Object[] {"RENAME [alcemir](ALCEMIR);",new MyArrayList(new String[]{"SELECT","*","FROM","alcemir","(","ALCEMIR",")"})};
//		result[4] = new Object[] {"JOIN [tabelinha = tabelao]((tabela1)(tabela2));",new MyArrayList(new String[]{"SELECT","*","FROM","tabela1","JOIN","tabela2","ON","[","tabelinha","=","tabelao","]"})};
//		result[5] = new Object[] {"(tabelinha)TIMES(tabelao);",new MyArrayList(new String[]{"SELECT","*","FROM","(","tabelinha",")",",","(","tabelao",")"})};
		
		
		
		
		
		return result;

	}

	@Test(dataProvider = ("testaTradutor"), enabled = true, groups = "develoment")
	public void testaTradutor(String sequencia, ArrayList<String> result)
			throws SintaticoException {

		Tradutor tradutor = new Tradutor();

		ArrayList<String> a = (ArrayList<String>) tradutor.traduzir(sequencia);

		Assert.assertEquals(a, result);

	}

	class MyArrayList extends ArrayList<String> {

		private static final long serialVersionUID = 1L;

		public MyArrayList(String o[]) {
			super(o.length);
			for (int i = 0; i < o.length; i++)
				super.add(o[i]);
		}
	}

}
