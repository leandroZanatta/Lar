package br.com.lar.server.repository.domain;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.lar.server.repository.domain.pk.PerfilUsuarioPk;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_perfilusuario")
public class PerfilUsuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PerfilUsuarioPk id;

	@ManyToOne
	@JoinColumn(name = "cd_usuario", insertable = false, updatable = false)
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "cd_perfil", insertable = false, updatable = false)
	private Perfil perfil;

}
