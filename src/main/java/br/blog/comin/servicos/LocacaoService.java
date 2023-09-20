package br.blog.comin.servicos;

import static br.blog.comin.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.blog.comin.dao.EmailService;
import br.blog.comin.dao.LocacaoDAO;
import br.blog.comin.dao.SPCService;
import br.blog.comin.entidades.Filme;
import br.blog.comin.entidades.Locacao;
import br.blog.comin.entidades.Usuario;
import br.blog.comin.exceptions.FilmeSemEstoqueException;
import br.blog.comin.exceptions.LocadoraException;
import br.blog.comin.utils.DataUtils;

public class LocacaoService {

	private LocacaoDAO dao;
	private SPCService spcService;

	private EmailService emailService;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {

		if(usuario == null) {
			throw new LocadoraException("Usuario vazio");
		}

		if(filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");
		}

		for (Filme filme: filmes) {
			if(filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
			}
		}

		if(spcService.possuiNegativacao(usuario)) {
			throw new LocadoraException("Usuário Negativado");
		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());

		Double valorTotal = 0d;
		for(int i = 0; i < filmes.size(); i++) {
			Filme filme = filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();
			switch (i) {
				case 2: valorFilme = valorFilme * 0.75; break;
				case 3: valorFilme = valorFilme * 0.5; break;
				case 4: valorFilme = valorFilme * 0.25; break;
				case 5: valorFilme = 0d; break;
			}
			valorTotal += valorFilme;
		}

		locacao.setValor(valorTotal);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		dao.salvar(locacao);
		
		return locacao;
	}

	public void notificarAtrasos(){
		List<Locacao> locacoes = dao.obterLocacoesPendentes();
		for(Locacao locacao: locacoes) {
			if(locacao.getDataRetorno().before(new Date())) {
				emailService.notificarAtraso(locacao.getUsuario());
			}
		}
	}

	public void setLocacaoDAO(LocacaoDAO dao) {
		this.dao = dao;
	}

	public void setSPCService(SPCService spc) {
		spcService = spc;
	}

	public void setEmailService(EmailService email) {
		emailService = email;
	}
}