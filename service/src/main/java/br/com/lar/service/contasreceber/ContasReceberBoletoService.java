package br.com.lar.service.contasreceber;

import java.util.List;

import br.com.lar.repository.dao.ContasReceberBoletoDAO;
import br.com.lar.repository.model.ContasReceberBoleto;

public class ContasReceberBoletoService {

    private ContasReceberBoletoDAO contasReceberBoletoDAO = new ContasReceberBoletoDAO();

    public void salvar(List<ContasReceberBoleto> contasReceberBoletos) {

        contasReceberBoletoDAO.salvar(contasReceberBoletos);
    }

}
