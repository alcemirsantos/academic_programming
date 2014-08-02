package br.ufpi.compilador.testes;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.ufpi.compilador.exceptions.SintaticoException;
import br.ufpi.compilador.kernel.AnalisadorSintatico;

public class TestesAnaliseSintatica {
	
	//testar sucesso de comandos 
	@DataProvider(name = "testaCOMANDOsuccess")
	public Object[][] testaCOMANDOsuccessDataProvider(){
		
		Object[][] teste = new Object[29][1];
		
		teste[0] = new Object[] {"SELECT [nome = 'Alcemir', sal>1.234] (Developers);"};
		teste[1] = new Object[] {"SELECT [nome = 'Alcemir', sal>1.234, rg = 1234678] (Developers);"};
		teste[2] = new Object[] {"SELECT [nome = 'Alcemir', sal>1234] (PROJECT [nome](Developers));"};
		teste[3] = new Object[] {"SELECT [nome = 'Alcemir', sal>1234] (RENAME [Programadores] (Developers));"};
		teste[4] = new Object[] {"SELECT [nome = 'Alcemir', sal>1234] (JOIN [nome='Alcemir']((Developers) TIMES (Students)));"};
		teste[5] = new Object[] {"SELECT [nome = 'Alcemir', sal>1.234] ((Developers) TIMES (Students));"};
		teste[6] = new Object[] {"SELECT [nome = 'Alcemir', sal>1.234] ((Developers) DIVISION (Students));"};
		
		teste[6] = new Object[] {"PROJECT [nome](Students);"};
		teste[7] = new Object[] {"PROJECT [nome,RG](Developers);"};
		teste[8] = new Object[] {"PROJECT [nome](SELECT [nome='Santos', sal>2345]((Developers) TIMES (Students)) );"};
		teste[9] = new Object[] {"PROJECT [nome, sal](RENAME[Developers](St_ude.nts));"};
		teste[10] = new Object[] {"PROJECT [no.me, sal](JOIN[Developers='Seniors']((Students)TIMES(Alunos)));"};
		teste[11] = new Object[] {"PROJECT [nome, sal]((Developers)TIMES(Students));"};
		teste[12] = new Object[] {"PROJECT [nome, sal]((Developers)DIVISION(Students));"};
		
		teste[13] = new Object[] {"JOIN [nome='Alcemir']((Developers) TIMES (Students));"};
		teste[13] = new Object[] {"JOIN [no_me='Alcemir']((Developers) TIMES (Students));"};
		teste[14] = new Object[] {"JOIN [nome='sal']((SELECT [nome = 'Alcemir', sal>1.234] (Developers))TIMES(PROJECT [nome, sal](JOIN[Developers='Seniors']((Students)TIMES(Alunos)))));"};
		teste[15] = new Object[] {"JOIN [nome='sal']((SELECT [nome = 'Alcemir', sal>1234] (JOIN [nome='Alcemir']((Developers) TIMES (Students))))TIMES(SELECT [nome = 'Alcemir', sal>1234] (JOIN [nome='Alcemir']((Developers) TIMES (Students)))));"};
		teste[16] = new Object[] {"JOIN [nome='sal']((PROJECT [nome, sal](JOIN[Developers='Seniors']((Students)TIMES(Alunos))))TIMES(JOIN [nome='Alcemir']((Developers) TIMES (Students))));"};
		teste[17] = new Object[] {"JOIN [nome='sal']((JOIN [nome='Alcemir']((Developers) TIMES (Students)))TIMES(PROJECT [nome, sal](JOIN[Developers='Seniors']((Students)TIMES(Alunos)))));"};
		
		teste[18] = new Object[] {"RENAME[FerrouFoitudo](JOIN [nome='sal']((JOIN [nome='Alcemir']((Developers) TIMES (Students)))TIMES(PROJECT [nome, sal](JOIN[Developers='Seniors']((Students)TIMES(Alunos))))));"};
		teste[19] = new Object[] {"RENAME[NaoFerrouNada](Joaonielton);"};
		teste[28] = new Object[] {"RENAME[NaoFerrouNada](Joaoni.elton);"};
		teste[19] = new Object[] {"RENAME[NaoFerrouNada](Joao_nielton);"};
		teste[21] = new Object[] {"(RENAME[FerrouFoitudo](JOIN [nome='sal']((JOIN [nome='Alcemir']((Developers) TIMES (Students)))TIMES(PROJECT [nome, sal](JOIN[Developers='Seniors']((Students)TIMES(Alunos)))))))TIMES(RENAME[FerrouFoitudo](JOIN [nome='sal']((JOIN [nome='Alcemir']((Developers) TIMES (Students)))TIMES(PROJECT [nome, sal](JOIN[Developers='Seniors']((Students)TIMES(Alunos)))))));"};
		
		teste[20] = new Object[] {"(assim)TIMES(SAssado);"};
		teste[22] = new Object[] {"(JOIN [nome='Alcemir']((Developers) TIMES (Students)))TIMES(PROJECT [nome, sal](RENAME[Developers](Students)));"};
		teste[23] = new Object[] {"(PROJECT [nome, sal](JOIN[Developers='Seniors']((Students)TIMES(Alunos))))TIMES(PROJECT [nome, sal](JOIN[Developers='Seniors']((Students)TIMES(Alunos))));"};
		
		teste[24] = new Object[] {"(PROJECT [nome, sal](JOIN[Developers='Seniors']((Students)TIMES(Alunos))))DIVISION(PROJECT [nome, sal](JOIN[Developers='Seniors']((Students)TIMES(Alunos))));"};
		teste[25] = new Object[] {"(PROJECT [nome](Students))DIVISION(PROJECT [nome](Students));"};
		teste[26] = new Object[] {"(Students)DIVISION(StudentS);"};
		teste[27] = new Object[] {"(SELECT [nome = 'Alcemir', sal>1.234] (Developers))DIVISION(SELECT [nome = 'Alcemir', sal>1.234] (Developers));"};
		
		return teste;
	}
	
	@Test(dataProvider = ("testaCOMANDOsuccess"), enabled = true, groups = "development")
	public void testaCOMANDOsuccess(String comandoTeste) throws SintaticoException, br.ufpi.compilador.exceptions.SintaticoException{
		AnalisadorSintatico aSintatico = new AnalisadorSintatico(comandoTeste);
		aSintatico.relacao();
	}
	

	//testar sucesso de comandos 
	@DataProvider(name = "testaCOMANDOfaliure")
	public Object[][] testaCOMANDOfaliureDataProvider(){
		
		Object[][] teste = new Object[71][1];
		
		teste[0] = new Object[] {"SELEC [nome = 'Alcemir', sal>1.234] (Developers);"};
		teste[1] = new Object[] {"SELECT nome = 'Alcemir', sal>1.234] (Developers);"};
		teste[2] = new Object[] {"SELECT [nome  'Alcemir', sal>1.234] (Developers);"};
		teste[3] = new Object[] {"SELECT [nome = Alcemir, sal>1.234] (Developers);"};
		teste[4] = new Object[] {"SELECT [nome = 'Alcemir' sal>1.234] (Developers);"};
		teste[5] = new Object[] {"SELECT [nome = 'Alcemir', sal 1.234] (Developers);"};
		teste[6] = new Object[] {"SELECT [nome = 'Alcemir', sal>1.234 (Developers);"};
		teste[7] = new Object[] {"SELECT [nome = 'Alcemir', sal>1.234] Developers);"};
		teste[8] = new Object[] {"SELECT [nome = 'Alcemir', sal>1.234] Developers ;"};
		teste[9] = new Object[] {"SELECT [nome = 'Alcemir', sal>1.234] ('Developers');"};
		teste[10] = new Object[] {"SELECT [nome = 'Alcemir', sal>1.234] (Developers > 2345);"};
		
		teste[11] = new Object[] {"ROJECT [nome](Students);"};
		teste[12] = new Object[] {"PROJECT nome](Students);"};
		teste[13] = new Object[] {"PROJECT [nome(Students);"};
		teste[14] = new Object[] {"PROJECT [nome]Students);"};
		teste[15] = new Object[] {"PROJECT [nome]Students;"};
		teste[16] = new Object[] {"PROJECT [nome]('Students');"};
		teste[17] = new Object[] {"PROJECT [nome](Students <= 876);"};
		teste[18] = new Object[] {"PROJECT [nome rg](Students);"};
		teste[19] = new Object[] {"PROJECT [SELECT](Students);"};
		teste[20] = new Object[] {"PROJECT [nome = 'ASDF'](Students);"};
		
		teste[21] = new Object[] {"JON [nome='Alcemir']((Developers) TIMES (Students));"};
		teste[22] = new Object[] {"JOIN nome='Alcemir']((Developers) TIMES (Students));"};
		teste[23] = new Object[] {"JOIN [nome 'Alcemir']((Developers) TIMES (Students));"};
		teste[24] = new Object[] {"JOIN [nome= Alcemir]((Developers) TIMES (Students));"};
		teste[25] = new Object[] {"JOIN ['nome'= 'Alcemir']((Developers) TIMES (Students));"};
		teste[26] = new Object[] {"JOIN [nome='Alcemir'((Developers) TIMES (Students));"};
		teste[27] = new Object[] {"JOIN [nome='Alcemir'](Developers) TIMES (Students));"};
		teste[28] = new Object[] {"JOIN [nome='Alcemir']((Developers TIMES (Students));"};
		teste[29] = new Object[] {"JOIN [nome='Alcemir']((Developers) IMES (Students));"};
		teste[30] = new Object[] {"JOIN [nome='Alcemir']((Developers) TIMES Students));"};
		teste[31] = new Object[] {"JOIN [nome='Alcemir'](Developers) TIMES (Students);"};
		teste[32] = new Object[] {"JOIN [nome='Alcemir']((Developers) DIVISION (Students));"};
		
		teste[33] = new Object[] {"RENAM[NaoFerrouNada](Joaonielton);"};
		teste[34] = new Object[] {"RENAME NaoFerrouNada](Joaonielton);"};
		teste[35] = new Object[] {"RENAME[NaoFerrouNada (Joaonielton);"};
		teste[36] = new Object[] {"RENAME[NaoFerrouNada] Joaonielton);"};
		teste[37] = new Object[] {"RENAME[NaoFerrouNada] Joaonielton ;"};
		teste[38] = new Object[] {"RENAME['NaoFerrouNada'](Joaonielton);"};
		teste[39] = new Object[] {"RENAME[NaoFerrouNada]('Joaonielton');"};
		teste[40] = new Object[] {"RENAME[NaoFerrouNada](JOIN);"};
		teste[41] = new Object[] {"RENAME[SELECT](Joaonielton);"};
		teste[42] = new Object[] {"RENAME[NaoFerrouNada > 9876](Joaonielton);"};
		teste[43] = new Object[] {"RENAME[NaoFerrouNada =](Joaonielton);"};
		teste[44] = new Object[] {"RENAME[NaoFerrouNada = 'ASDF'](Joaonielton);"};
				
		teste[45] = new Object[] {"assim)TIMES(SAssado);"};
		teste[46] = new Object[] {"(assim TIMES(SAssado);"};
		teste[47] = new Object[] {"(assim) IMES(SAssado);"};
		teste[48] = new Object[] {"(assim)TIMES SAssado);"};
		teste[49] = new Object[] {"(assim)TIMES SAssado;"};
		teste[50] = new Object[] {"(assim)TIMES('SAssado');"};
		teste[51] = new Object[] {"(assim)TIMES(345678);"};
		teste[52] = new Object[] {"(assim)TIMES(SAssado <> 344);"};
		teste[53] = new Object[] {"(234)TIMES(SAssado);"};
		teste[54] = new Object[] {"(SELECT) TIMES(SAssado);"};
		teste[55] = new Object[] {"('assim')TIMES(SAssado);"};
		teste[56] = new Object[] {"(assim = )TIMES(SAssado);"};
		teste[57] = new Object[] {"(assim = 'sdfe')TIMES(SAssado);"};
				
		teste[58] = new Object[] {"Students)DIVISION(StudentS);"};
		teste[59] = new Object[] {"(Students DIVISION(StudentS);"};
		teste[60] = new Object[] {"(Students) IVISION(StudentS);"};
		teste[61] = new Object[] {"(Students)DIVISION StudentS);"};
		teste[62] = new Object[] {"(Students)DIVISION StudentS;"};
		teste[63] = new Object[] {"(Students)DIVISION('StudentS');"};
		teste[64] = new Object[] {"(Students)DIVISION(StudentS <> 456);"};
		teste[65] = new Object[] {"('Students')DIVISION(StudentS);"};
		teste[66] = new Object[] {"(JOIN)DIVISION(StudentS);"};
		teste[67] = new Object[] {"(Students >= 87)DIVISION(StudentS);"};
		teste[68] = new Object[] {"(Students)DIVISION('StudentS');"};
		teste[69] = new Object[] {"(Students)DIVISION(StudentS = );"};
		teste[70] = new Object[] {"(Students)DIVISION(StudentS > 1232);"};
	
		return teste;
	}
	
	
	@Test(dataProvider = ("testaCOMANDOfaliure"), enabled = true, groups = "develoment", expectedExceptions = { SintaticoException.class })
	public void testaCOMANDOfaliure(String comandoTeste) throws SintaticoException, br.ufpi.compilador.exceptions.SintaticoException{
		
		AnalisadorSintatico aSintatico = new AnalisadorSintatico(comandoTeste);		
		
		aSintatico.relacao();
		
	}
}
