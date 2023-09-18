package br.blog.comin.servicos;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import br.blog.comin.entidades.Filme;
import br.blog.comin.entidades.Locacao;
import br.blog.comin.entidades.Usuario;
import br.blog.comin.utils.DataUtils;

public class LocacaoServiceTest {
	@Test
	public void teste() {
		//cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);

		//acao
		Locacao locacao = service.alugarFilme(usuario, filme);

		//verificacao
		Assert.assertTrue(locacao.getValor() == 5.0);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}
}
