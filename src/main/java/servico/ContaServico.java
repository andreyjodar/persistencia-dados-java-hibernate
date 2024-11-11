package servico;

import java.time.LocalDate;
import java.util.List;

import dao.ClienteDAO;
import dao.ContaDAO;
import dao.MovimentacaoDAO;
import entidade.Cliente;
import entidade.Conta;
import entidade.ContaTipo;

public class ContaServico {
	ContaDAO daoConta = new ContaDAO();
	ClienteDAO daoCliente = new ClienteDAO();
	MovimentacaoDAO daoMovimentacao = new MovimentacaoDAO();
	ClienteServico servicoCliente = new ClienteServico(); 
	
	public Conta inserirConta(Conta conta) {
		if(verificarCamposNaoNulos(conta) && conta.getCliente().getId() != null && daoConta.listarPorIdCliente(conta.getCliente().getId()).size() < 3) {
			Conta contaValida = daoConta.inserirConta(conta);
			return contaValida;
		}
		return null;
	}
	
	public Conta alterarConta(Conta conta) {
		if(conta.getCliente().getId() != null) {
			conta.setSaldo(MovimentacaoServico.calcularSaldo(conta.getCliente().getId()));
		}
		return daoConta.alterarConta(conta);
	}
	
	public void excluirConta(Long idConta) {
		if(daoConta.buscarPorId(idConta) != null) {
			daoMovimentacao.excluirPorConta(idConta);
			daoConta.excluirConta(idConta);
		}
	}
	
	public void excluirPorCliente(Long idCliente) {
		daoConta.excluirPorCliente(idCliente);
	}
	
	public static boolean verificarCamposNaoNulos(Conta conta) {
		return conta != null && conta.getCliente() != null && conta.getContaTipo() != null && conta.getDataAbertura() != null && conta.getSaldo() != null; 
	}
	
	public List<Conta> listarTodasContas() {
		return daoConta.listarTodasContas();
	}
	
	public List<Conta> listarPorIdCliente(Long idCliente) {
		return daoConta.listarPorIdCliente(idCliente);
	}
	
	public List<Conta> listarPorContaTipo(ContaTipo contaTipo) {
		return daoConta.listarPorContaTipo(contaTipo);
	}
	
	public List<Conta> listarPorPeriodoCriacao(LocalDate dataInicial, LocalDate dataFinal) {
		return daoConta.listarPorPeriodoCriacao(dataInicial, dataFinal);
	}
}
