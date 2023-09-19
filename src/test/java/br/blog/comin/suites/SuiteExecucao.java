package br.blog.comin.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.blog.comin.servicos.CalculoValorLocacaoTest;
import br.blog.comin.servicos.LocacaoServiceTest;

//@RunWith(Suite.class)
@Suite.SuiteClasses({
		CalculoValorLocacaoTest.class,
		LocacaoServiceTest.class
})
public class SuiteExecucao {
	//Remova se puder!
}