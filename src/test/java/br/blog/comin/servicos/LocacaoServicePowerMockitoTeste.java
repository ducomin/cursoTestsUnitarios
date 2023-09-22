package br.blog.comin.servicos;

import static br.blog.comin.builders.FilmeBuilder.umFilme;
import static br.blog.comin.builders.UsuarioBuilder.umUsuario;
import static br.blog.comin.matchers.MatchersProprios.caiNumaSegunda;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import br.blog.comin.dao.EmailService;
import br.blog.comin.dao.LocacaoDAO;
import br.blog.comin.dao.SPCService;
import br.blog.comin.entidades.Filme;
import br.blog.comin.entidades.Locacao;
import br.blog.comin.entidades.Usuario;
import br.blog.comin.utils.DataUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class})
public class LocacaoServicePowerMockitoTeste {

		@InjectMocks
		private LocacaoService service;

		@Mock
		private SPCService spc;
		@Mock
		private LocacaoDAO dao;
		@Mock
		private EmailService email;

		@Rule
		public ErrorCollector error = new ErrorCollector();

		@Rule
		public ExpectedException exception = ExpectedException.none();

		@Before
		public void setup(){
			MockitoAnnotations.initMocks(this);
			service = PowerMockito.spy(service);
		}

		@Test
		public void deveAlugarFilme() throws Exception {
			//cenario
			Usuario usuario = umUsuario().agora();
			List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

			PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28, 4, 2017));

			//acao
			Locacao locacao = service.alugarFilme(usuario, filmes);

			//verificacao
			error.checkThat(locacao.getValor(), is(equalTo(5.0)));
			error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)), is(true));
			error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)), is(true));
		}

		@Test
		public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception{
			//cenario
			Usuario usuario = umUsuario().agora();
			List<Filme> filmes = Arrays.asList(umFilme().agora());

			PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(29, 4, 2017));

			//acao
			Locacao retorno = service.alugarFilme(usuario, filmes);

			//verificacao
			assertThat(retorno.getDataRetorno(), caiNumaSegunda());
		}

		@Test
		public void deveAlugarFilme_SemCalcularValor() throws Exception{
			//cenario
			Usuario usuario = umUsuario().agora();
			List<Filme> filmes = Arrays.asList(umFilme().agora());

			PowerMockito.doReturn(1.0).when(service, "getValorTotal", filmes);

			//acao
			Locacao locacao = service.alugarFilme(usuario, filmes);

			//verificacao
			Assert.assertThat(locacao.getValor(), is(1.0));
			PowerMockito.verifyPrivate(service).invoke("getValorTotal", filmes);
		}

		@Test
		public void deveCalcularValorLocacao() throws Exception{
			//cenario
			List<Filme> filmes = Arrays.asList(umFilme().agora());

			//acao
			Double valor = (Double) Whitebox.invokeMethod(service, "getValorTotal", filmes);

			//verificacao
			Assert.assertThat(valor, is(4.0));
		}

}
