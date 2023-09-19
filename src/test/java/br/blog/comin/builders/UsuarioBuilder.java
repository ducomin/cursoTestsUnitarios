package br.blog.comin.builders;

import br.blog.comin.entidades.Usuario;

public class UsuarioBuilder {
	private Usuario usuario;

	private UsuarioBuilder() {}

	public static UsuarioBuilder umUsuario(){
		UsuarioBuilder builder = new UsuarioBuilder();
		builder.usuario = new Usuario();
		builder.usuario.setNome("Usuario 1");
		return builder;
	}

	public Usuario agora(){
		return usuario;
	}
}
