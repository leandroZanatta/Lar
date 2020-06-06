package br.com.lar.server.repository.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_usuario")
@SequenceGenerator(name = "GEN_USUARIO", sequenceName = "GEN_USUARIO", allocationSize = 1)
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_USUARIO")
	@Column(name = "id_usuario")
	private Long idUsuario;

	@Column(name = "tx_senha")
	private String senha;

	@Column(name = "tx_usuario")
	private String nomeUsuario;

	@Column(name = "cd_cliente")
	private Long codigoCliente;

	@OneToMany(mappedBy = "usuario")
	private List<PermissaoPrograma> permissaoProgramas;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	private List<PerfilUsuario> perfilUsuarios = new ArrayList<>();

	public void addPerfilUsuario(Perfil perfil) {

		PerfilUsuario perfilUsuario = new PerfilUsuario();

		perfilUsuario.setPerfil(perfil);
		perfilUsuario.setUsuario(this);

		perfilUsuarios.add(perfilUsuario);
	}

}