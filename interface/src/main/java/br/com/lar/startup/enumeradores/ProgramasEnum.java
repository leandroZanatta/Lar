package br.com.lar.startup.enumeradores;

import java.util.HashMap;
import java.util.Map;

import br.com.lar.ui.FrmBanco;
import br.com.lar.ui.FrmCadastroPesquisa;
import br.com.lar.ui.FrmCidade;
import br.com.lar.ui.FrmCliente;
import br.com.lar.ui.FrmConsultarContasReceber;
import br.com.lar.ui.FrmEstado;
import br.com.lar.ui.FrmFormasPagamento;
import br.com.lar.ui.FrmFuncionario;
import br.com.lar.ui.FrmGerarContasReceber;
import br.com.lar.ui.FrmGerenciadorBoletos;
import br.com.lar.ui.FrmGrupo;
import br.com.lar.ui.FrmHistoricoEvolucoes;
import br.com.lar.ui.FrmMensalidadePaciente;
import br.com.lar.ui.FrmPaciente;
import br.com.lar.ui.FrmParametros;
import br.com.lar.ui.FrmPerfil;
import br.com.lar.ui.FrmPermissoes;
import br.com.lar.ui.FrmPlanoContas;
import br.com.lar.ui.FrmProcedencia;
import br.com.lar.ui.FrmUsuario;
import br.com.sysdesc.components.AbstractInternalFrame;

public enum ProgramasEnum {

    CADASTRO_PERFIS(2L, FrmPerfil.class),

    CADASTRO_ESTADOS(3L, FrmEstado.class),

    CADASTRO_CIDADE(4L, FrmCidade.class),

    CADASTRO_CLIENTES(5L, FrmCliente.class),

    CADASTRO_PESQUISA(8L, FrmCadastroPesquisa.class),

    CADASTRO_USUARIOS(9L, FrmUsuario.class),

    CADASTRO_PERMISSOES(10L, FrmPermissoes.class),

    CADASTRO_PROCEDENCIAS(11L, FrmProcedencia.class),

    CADASTRO_PACIENTES(12L, FrmPaciente.class),

    CADASTRO_FUNCIONARIOS(13L, FrmFuncionario.class),

    CADASTRO_GRUPOS(14L, FrmGrupo.class),

    CADASTRO_HISTORICOS(15L, FrmHistoricoEvolucoes.class),

    CADASTRO_BANCOS(18L, FrmBanco.class),

    CADASTRO_FORMAS_PAGAMENTO(19L, FrmFormasPagamento.class),

    CADASTRO_MENSALIDADE_PACIENTES(20L, FrmMensalidadePaciente.class),

    CADASTRO_PARAMETROS(21L, FrmParametros.class),

    CADASTRO_PLANO_CONTAS(23L, FrmPlanoContas.class),

    GERENCIADOR_BOLETOS(25L, FrmGerenciadorBoletos.class),

    CADASTRO_CONTAS_RECEBER(26L, FrmGerarContasReceber.class),

    CONSULTA_CONTAS_RECEBER(27L, FrmConsultarContasReceber.class);

    private static Map<Long, ProgramasEnum> mapa = new HashMap<>();

    static {

        for (ProgramasEnum programa : ProgramasEnum.values()) {
            mapa.put(programa.getCodigo(), programa);
        }
    }

    private final Long codigo;

    private final Class<? extends AbstractInternalFrame> internalFrame;

    ProgramasEnum(Long codigo, Class<? extends AbstractInternalFrame> internalFrame) {
        this.codigo = codigo;
        this.internalFrame = internalFrame;
    }

    public Long getCodigo() {
        return codigo;
    }

    public Class<? extends AbstractInternalFrame> getInternalFrame() {
        return internalFrame;
    }

    public static ProgramasEnum findByCodigo(Long codigoPrograma) {
        return mapa.get(codigoPrograma);
    }
}
