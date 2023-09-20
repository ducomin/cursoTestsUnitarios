package br.blog.comin.dao;

import java.util.List;

import br.blog.comin.entidades.Locacao;

public interface LocacaoDAO {
	public void salvar(Locacao locacao);

	public List<Locacao> obterLocacoesPendentes();
}
