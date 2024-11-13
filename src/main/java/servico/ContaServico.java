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
		return daoConta.alterarConta(conta);
	}
	
	public Conta atualizarConta(Conta conta) {
		if(conta.getId() != null) {
			conta.setSaldo(MovimentacaoServico.calcularSaldo(conta.getId()));
			LocalDate hoje = LocalDate.now();
			LocalDate mesAnterior = hoje.minusMonths(1);
			if(conta.getUltimaAtualizacaoCashback() == null || conta.getUltimaAtualizacaoCashback().isBefore(mesAnterior.withDayOfMonth(1))) {
				conta.setSaldo(conta.getSaldo() + conta.getCashBackAcumulado());
				conta.setCashBackAcumulado(0.0);
				conta.setUltimaAtualizaçãoCashback(hoje.withDayOfMonth(1));
			}
		}
		return daoConta.altualizarConta(conta);
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
	
	public Conta buscarPorId(Long idConta) {
		return daoConta.buscarPorId(idConta);
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
	
	public Double calcularRendimentoMensal(Long idConta, Double taxaJuros) {
		Conta conta = daoConta.buscarPorId(idConta);
		if(conta.getContaTipo() == ContaTipo.CONTA_POUPANCA) {
			Double montante = conta.getSaldo() * Math.pow((1 + taxaJuros), 1);
			Double rendimento = montante - conta.getSaldo();
			return rendimento;
		}
		return 0.0;
	}
	
}
