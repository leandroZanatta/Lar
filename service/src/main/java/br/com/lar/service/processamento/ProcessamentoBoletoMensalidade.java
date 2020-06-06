package br.com.lar.service.processamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import br.com.lar.repository.model.Cliente;
import br.com.lar.repository.model.ContasReceber;
import br.com.lar.repository.model.ContasReceberBoleto;
import br.com.lar.repository.model.MensalidadePaciente;
import br.com.lar.repository.model.pk.ContasReceberBoletoPk;
import br.com.lar.service.contasreceber.ContasReceberBoletoService;
import br.com.lar.service.contasreceber.ContasReceberService;
import br.com.lar.service.email.EmailService;
import br.com.lar.service.mensalidade.MensalidadePacienteService;
import br.com.syscesc.boleto.service.boleto.BoletoService;
import br.com.sysdesc.boleto.repository.model.Boleto;
import br.com.sysdesc.boleto.repository.model.BoletoDadosCliente;
import br.com.sysdesc.boleto.repository.model.BoletoDadosSacadorAvalista;
import br.com.sysdesc.boletos.util.vo.PagamentoBoletoVO;
import br.com.sysdesc.util.classes.DateUtil;
import br.com.sysdesc.util.classes.ListUtil;
import br.com.sysdesc.util.enumeradores.FormaPagamentoEnum;
import br.com.sysdesc.util.enumeradores.TipoProgramaContasReceberEnum;
import br.com.sysdesc.util.enumeradores.TipoTituloEnum;
import br.com.sysdesc.util.vo.ConfiguracaoMensalidadeVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessamentoBoletoMensalidade {

    private MensalidadePacienteService mensalidadePacienteService = new MensalidadePacienteService();

    private ContasReceberService contasReceberService = new ContasReceberService();

    private BoletoService boletoService = new BoletoService();

    private EmailService emailService = new EmailService();

    private ContasReceberBoletoService contasReceberBoletoService = new ContasReceberBoletoService();

    public void gerarBoleto(Date dataProcessar, ConfiguracaoMensalidadeVO configuracaoMensalidadeVO) {

        if (configuracaoMensalidadeVO.getConfiguracaoBoletoVO().isGerarboletos()) {

            List<MensalidadePaciente> mensalidadesBoleto = mensalidadePacienteService.buscarMensalidadesPorBoleto();

            if (!ListUtil.isNullOrEmpty(mensalidadesBoleto)) {

                mensalidadesBoleto.stream().filter(paciente -> this.validarDiaGeracaoBoleto(dataProcessar, paciente))
                        .forEach(mensalidadeDia -> this.gerarBoletoPaciente(mensalidadeDia, dataProcessar, configuracaoMensalidadeVO));

            }
        }

    }

    private void gerarBoletoPaciente(MensalidadePaciente mensalidadeDia, Date dataProcessar, ConfiguracaoMensalidadeVO configuracaoMensalidadeVO) {

        Cliente cliente = getCliente(mensalidadeDia);

        List<ContasReceber> contasReceber = contasReceberService.buscarContasReceberPacienteTipoBoleto(cliente.getIdCliente(),
                mensalidadeDia.isAgruparContas());

        Map<Date, List<ContasReceber>> mapaContasReceber = contasReceber.stream().collect(Collectors.groupingBy(ContasReceber::getDataVencimento));

        for (Entry<Date, List<ContasReceber>> entry : mapaContasReceber.entrySet()) {

            if (DateUtil.menorOuIgual(entry.getKey(), dataProcessar)) {

                log.warn(String.format(
                        "Não é possivel gerar boletos com menos de um dia para vencimento, por favor verifique os códigos de contas a receber %s para a data %s",
                        entry.getValue().stream().mapToLong(ContasReceber::getIdContasReceber).boxed().collect(Collectors.toList()),
                        DateUtil.format(DateUtil.FORMATO_DD_MM_YYY, entry.getKey())));

                continue;
            }

            BigDecimal valorMensalidade = entry.getValue().stream().map(ContasReceber::getValorParcela).reduce(BigDecimal.ZERO, BigDecimal::add);

            ContasReceber contaReceberMensalidade = entry.getValue().stream()
                    .filter(contaReceber -> contaReceber.getPrograma().equals(TipoProgramaContasReceberEnum.MENSALIDADE.getCodigo())).findFirst()
                    .orElse(entry.getValue().get(0));

            PagamentoBoletoVO pagamentoBoletoVO = new PagamentoBoletoVO(contaReceberMensalidade.getIdContasReceber(), valorMensalidade,
                    entry.getKey());

            Boleto boleto = boletoService.gerarBoletoSemAceite(montarClienteBoleto(cliente), montarSacadorAvalista(mensalidadeDia), pagamentoBoletoVO,
                    mensalidadeDia.getFormasPagamento().getBanco(), TipoTituloEnum.DS_DUPLICATA_DE_SERVICO);

            if (configuracaoMensalidadeVO.getConfiguracaoBoletoVO().isEnviarEmail()) {

                emailService.gerarEmailBoleto(mensalidadeDia.getPaciente(), entry.getValue(), boleto);
            }

            this.salvarBoletoContasReceber(entry.getValue(), boleto);

        }

    }

    private void salvarBoletoContasReceber(List<ContasReceber> contasRecebers, Boleto boleto) {

        List<ContasReceberBoleto> contasReceberBoletos = new ArrayList<>();

        contasRecebers.forEach(conta -> {

            ContasReceberBoleto contasReceberBoleto = new ContasReceberBoleto();
            contasReceberBoleto.setId(new ContasReceberBoletoPk(boleto.getIdBoleto(), conta.getIdContasReceber()));

            contasReceberBoletos.add(contasReceberBoleto);
        });

        contasReceberBoletoService.salvar(contasReceberBoletos);
    }

    private boolean validarDiaGeracaoBoleto(Date dataProcessar, MensalidadePaciente paciente) {

        FormaPagamentoEnum formaPagamentoEnum = FormaPagamentoEnum.forCodigo(paciente.getFormasPagamento().getCodigoFormaPagamento());

        return formaPagamentoEnum.equals(FormaPagamentoEnum.BOLETO)
                && DateUtil.getDayOfMonth(DateUtil.addDays(dataProcessar, paciente.getDiasAntecedencia())) == paciente.getDiaVencimento().intValue();
    }

    private BoletoDadosCliente montarClienteBoleto(Cliente cliente) {

        BoletoDadosCliente clienteBoletoVO = new BoletoDadosCliente();
        clienteBoletoVO.setCgc(cliente.getCgc());
        clienteBoletoVO.setFlagTipoCliente(cliente.getFlagTipoCliente());
        clienteBoletoVO.setNome(cliente.getNome());
        clienteBoletoVO.setCidade(cliente.getCidade().getDescricao());
        clienteBoletoVO.setUF(cliente.getCidade().getEstado().getUf());
        clienteBoletoVO.setEndereco(cliente.getEndereco());
        clienteBoletoVO.setNumero(cliente.getNumero());
        clienteBoletoVO.setBairro(cliente.getBairro());
        clienteBoletoVO.setCep(cliente.getCep());

        return clienteBoletoVO;
    }

    private BoletoDadosSacadorAvalista montarSacadorAvalista(MensalidadePaciente mensalidade) {

        if (mensalidade.getPaciente().getResponsavel() != null) {
            Cliente paciente = mensalidade.getPaciente().getCliente();

            BoletoDadosSacadorAvalista boletoDadosSacadorAvalista = new BoletoDadosSacadorAvalista();
            boletoDadosSacadorAvalista.setCgc(paciente.getCgc());
            boletoDadosSacadorAvalista.setFlagTipoCliente(paciente.getFlagTipoCliente());
            boletoDadosSacadorAvalista.setNome(paciente.getNome());

            return boletoDadosSacadorAvalista;
        }

        return null;
    }

    private Cliente getCliente(MensalidadePaciente mensalidadeDia) {

        if (mensalidadeDia.getPaciente().getResponsavel() != null) {
            return mensalidadeDia.getPaciente().getResponsavel();
        }

        return mensalidadeDia.getPaciente().getCliente();
    }
}
