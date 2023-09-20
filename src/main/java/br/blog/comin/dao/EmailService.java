package br.blog.comin.dao;

import br.blog.comin.entidades.Usuario;

public interface EmailService {

	public void  notificarAtraso(Usuario usuario);
}
