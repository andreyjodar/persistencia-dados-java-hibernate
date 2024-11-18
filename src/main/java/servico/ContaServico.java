package servico;

import java.time.LocalDate;
import java.util.List;

import dao.ContaDAO;
import entidade.Conta;
import entidade.ContaTipo;
import entidade.Movimentacao;
import entidade.TransacaoTipo;
import util.JurosComposto;

public class ContaServico {
	static ContaDAO daoConta = new ContaDAO();
	static MovimentacaoServico servicoMovimentacao = new MovimentacaoServico();
	static ClienteServico servicoCliente = new ClienteServico(); 
	
	public Conta inserirConta(Conta conta) {
		if(verificarCamposNaoNulos(conta)) {
			if(daoConta.listarPorCliente(conta.getCliente().getId()).size() < 3) {
				return daoConta.inserirConta(conta);
			}
			return null;
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
		return calcularSaldo(idConta, LocalDate.now());
	}
	
	public static Double calcularSaldo(Long idConta, LocalDate dataLimite) {
		Conta conta = daoConta.buscarPorId(idConta);
		Double saldo = 0.0;
		List<Movimentacao> transacoes = servicoMovimentacao.listarExtratoPeriodicoConta(idConta, conta.getDataAbertura().toLocalDate(), dataLimite);
		for (Movimentacao movimentacao : transacoes) {
			if(movimentacao.getTipoTransacao() == TransacaoTipo.DEPOSITO) {
				saldo += movimentacao.getValorOperacao();
			} else {
				saldo -= movimentacao.getValorOperacao();
			}
		}
		return saldo + calcularCashbackAcumulado(idConta, dataLimite);
	}
	
	public static void notificarSaldoBaixo(Long idConta) {
		if(calcularSaldo(idConta) < 100) {
			System.out.println("Saldo Baixo: R$" + calcularSaldo(idConta));
		}
	}
	
	public static Double calcularSaquePorDia(Long idConta, LocalDate dia) {
		List<Movimentacao> transacoes = servicoMovimentacao.listarPeriodicoPorTipoTransacao(idConta, TransacaoTipo.SAQUE, dia, dia);
		Double saques = 0.0;
		for(Movimentacao movimentacao : transacoes) {
			saques += movimentacao.getValorOperacao();
		}
		return saques;
	}
	
	public static Double calcularCashbackAcumulado(Long idConta, LocalDate dataLimite) {
		LocalDate dataAberturaConta = daoConta.buscarPorId(idConta).getDataAbertura().toLocalDate();
		LocalDate ultimoDiaMesAnterior = dataLimite.minusMonths(1).withDayOfMonth(dataLimite.minusMonths(1).lengthOfMonth());
		List<Movimentacao> transacoes = servicoMovimentacao.listarPeriodicoPorTipoTransacao(idConta, TransacaoTipo.DEBITO_CARTAO, dataAberturaConta, ultimoDiaMesAnterior);
		Double cashback = 0.0;
		for (Movimentacao movimentacao : transacoes) {
			cashback += movimentacao.getCashback();
		}
		return cashback;
	}
	
	public static Double calcularRendimentoMensal(Long idConta, Double taxaJuros, Integer meses) {
		Conta conta = daoConta.buscarPorId(idConta);
		if(conta.getContaTipo() == ContaTipo.CONTA_POUPANCA) {
			return JurosComposto.calcularRendimento(calcularSaldo(idConta), taxaJuros, meses);
		}
		return 0.0;
	}
	
	public static Double calcularLimiteCredito(Long idConta) {
		LocalDate dataAtual = LocalDate.now();
		LocalDate fimUltimoMes = dataAtual.minusMonths(1).withDayOfMonth(dataAtual.minusMonths(1).lengthOfMonth());
		LocalDate fimPenultimoMes = dataAtual.minusMonths(2).withDayOfMonth(dataAtual.minusMonths(2).lengthOfMonth());
		LocalDate fimAntepenultimoMes = dataAtual.minusMonths(3).withDayOfMonth(dataAtual.minusMonths(3).lengthOfMonth());
		
		Double saldoUltimoMes = calcularSaldo(idConta, fimUltimoMes);
		Double saldoPenultimoMes = calcularSaldo(idConta, fimPenultimoMes);
		Double saldoAntepenultimoMes = calcularSaldo(idConta, fimAntepenultimoMes);
		return (saldoUltimoMes + saldoPenultimoMes + saldoAntepenultimoMes) / 3;
	}
	
	public Conta buscarPorId(Long idConta) {
		return daoConta.buscarPorId(idConta);
	}
	
	public List<Conta> listarTodasContas() {
		return daoConta.listarTodasContas();
	}
	
	public List<Conta> listarPorCliente(Long idCliente) {
		return daoConta.listarPorCliente(idCliente);
	}
	
	public List<Conta> listarPorContaTipo(ContaTipo contaTipo) {
		return daoConta.listarPorContaTipo(contaTipo);
	}
	
	public List<Conta> listarPorPeriodoCriacao(LocalDate dataInicial, LocalDate dataFinal) {
		return daoConta.listarPorPeriodoCriacao(dataInicial, dataFinal);
	}
	
}
