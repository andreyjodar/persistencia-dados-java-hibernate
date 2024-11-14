package servico;

import java.time.LocalDate;
import java.util.List;

import dao.ClienteDAO;
import dao.ContaDAO;
import dao.MovimentacaoDAO;
import entidade.Cliente;
import entidade.Conta;
import entidade.ContaTipo;
import entidade.Movimentacao;
import entidade.TransacaoTipo;

public class ContaServico {
	static ContaDAO daoConta = new ContaDAO();
	static MovimentacaoServico servicoMovimentacao = new MovimentacaoServico();
	static ClienteServico servicoCliente = new ClienteServico(); 
	
	public Conta inserirConta(Conta conta) {
		if(verificarCamposNaoNulos(conta) && daoConta.listarPorIdCliente(conta.getCliente().getId()).size() < 3) {
			Conta contaValida = daoConta.inserirConta(conta);
			return contaValida;
		}
		return null;
	}
	
	public Conta alterarConta(Conta conta) {
		return daoConta.alterarConta(conta);
	}
	
	public void excluirConta(Long idConta) {
		if(daoConta.buscarPorId(idConta) != null) {
			servicoMovimentacao.excluirPorConta(idConta);
			daoConta.excluirConta(idConta);
		}
	}
	
	public void excluirPorCliente(Long idCliente) {
		daoConta.excluirPorCliente(idCliente);
	}
	
	public static boolean verificarCamposNaoNulos(Conta conta) {
		return conta != null && conta.getCliente() != null && conta.getCliente().getId() != null && conta.getContaTipo() != null && conta.getDataAbertura() != null; 
	}
	
	public static Double calcularSaldo(Long idConta) {
		List<Movimentacao> transacoes = servicoMovimentacao.listarPorConta(idConta);
		Double saldo = 0.0;
		for (Movimentacao movimentacao : transacoes) {
			if(movimentacao.getTipoTransacao() == TransacaoTipo.DEPOSITO) {
				saldo += movimentacao.getValorOperacao();
			} else {
				saldo -= movimentacao.getValorOperacao();
			}
		}
		return saldo + calcularCashbackAcumulado(idConta);
	}
	
	public static Double calcularSaquePorDia(Long idConta, LocalDate dia) {
		List<Movimentacao> transacoes = servicoMovimentacao.listarPeriodicoPorTipoTransacao(idConta, TransacaoTipo.SAQUE, dia, dia);
		Double saques = 0.0;
		for(Movimentacao movimentacao : transacoes) {
			saques += movimentacao.getValorOperacao();
		}
		return saques;
	}
	
	public static void notificarSaldoBaixo(Long idConta) {
		if(calcularSaldo(idConta) < 100) {
			System.out.println("Saldo Baixo: R$" + calcularSaldo(idConta));
		}
	}
	
	public static Double calcularCashbackAcumulado(Long idConta) {
		LocalDate dataAberturaConta = daoConta.buscarPorId(idConta).getDataAbertura().toLocalDate();
		LocalDate dataAtual = LocalDate.now();
		LocalDate ultimoDiaMesAnterior = dataAtual.minusMonths(1).withDayOfMonth(dataAtual.minusMonths(1).lengthOfMonth());
		List<Movimentacao> transacoes = servicoMovimentacao.listarPeriodicoPorTipoTransacao(idConta, TransacaoTipo.DEBITO_CARTAO, dataAberturaConta, ultimoDiaMesAnterior);
		Double cashback = 0.0;
		for (Movimentacao movimentacao : transacoes) {
			cashback += movimentacao.getCashback();
		}
		return cashback;
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
			Double montante = calcularSaldo(conta.getId()) * Math.pow((1 + taxaJuros), 1);
			Double rendimento = montante - calcularSaldo(conta.getId());
			return rendimento;
		}
		return 0.0;
	}
	
}
