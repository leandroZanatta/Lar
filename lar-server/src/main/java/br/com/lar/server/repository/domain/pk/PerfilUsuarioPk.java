package br.com.lar.server.repository.domain.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfilUsuarioPk implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "cd_usuario")
	private Long codigoUsuario;

	@Column(name = "cd_perfil")
	private Long codigoPerfil;

}
