package servico;

import java.time.*;
import java.util.List;

import dao.MovimentacaoDAO;
import entidade.Conta;
import entidade.Movimentacao;
import entidade.TransacaoTipo;

public class MovimentacaoServico {
	static MovimentacaoDAO daoMovimentacao = new MovimentacaoDAO();
	static ContaServico servicoConta = new ContaServico();
	
	// Verificar Fraude
	public Movimentacao inserirMovimentacao(Movimentacao movimentacao) {
		if(verificarCamposNaoNulos(movimentacao) && movimentacao.getConta().getId() != null && movimentacao.getConta().getCliente() != null && movimentacao.getConta().getCliente().getId() != null) {
			aplicarTarifaOperacao(movimentacao);
			if(verificarLimites(movimentacao)) {
				if(verificarSaldo(movimentacao)) {
					Movimentacao movimentacaoBanco = daoMovimentacao.inserirMovimentacao(movimentacao);
					servicoConta.atualizarConta(movimentacaoBanco.getConta());
					notificarSaldoBaixo(movimentacao.getConta().getId());
					return movimentacaoBanco;
				} 
				return null;
			} 
			return null;
		}
		return null;
	}

	public void excluirMovimentacao(Long idMovimentacao) {
		daoMovimentacao.excluirMovimentacao(idMovimentacao);
	}
	
	public void excluirPorConta(Long idConta) {
		daoMovimentacao.excluirPorConta(idConta);
	}
	
	public void excluirPorCliente(Long idCliente) {
		daoMovimentacao.excluirPorCliente(idCliente);
	}
	
	public Movimentacao alterarMovimentacao(Movimentacao movimentacao) {
		return daoMovimentacao.alterarMovimentacao(movimentacao);
	}
	
	public static boolean verificarLimites(Movimentacao movimentacao) {
		if(daoMovimentacao.listarExtratoDiarioConta(movimentacao.getConta().getId(), movimentacao.getDataTransacao().toLocalDate()).size() >= 10) {
			return false;
		} else if (movimentacao.getTipoTransacao() == TransacaoTipo.PIX) {
			if (movimentacao.getValorOperacao() > 300 || movimentacao.getDataTransacao().getHour() > 22 || movimentacao.getDataTransacao().getHour() < 6) {
				return false;
			} 
			return true;
		} else if (movimentacao.getTipoTransacao() == TransacaoTipo.SAQUE) {
			if (calcularSaquesDiarios(movimentacao.getConta().getId(), movimentacao.getDataTransacao().toLocalDate()) + movimentacao.getValorOperacao() >= 5000) {
				return false;
			} 
			return true;
		} 
		return true;
	}

	public static boolean verificarSaldo(Movimentacao movimentacao) {
		if(movimentacao.getTipoTransacao() == TransacaoTipo.SAQUE || movimentacao.getTipoTransacao() == TransacaoTipo.PIX || movimentacao.getTipoTransacao() == TransacaoTipo.PAGAMENTO) {
			if (calcularSaldo(movimentacao.getConta().getId()) >= movimentacao.getValorOperacao()) {
				return true;
			} else {
				return false;
			}
		}
		return true;

	}
	
	public static boolean verificarCamposNaoNulos(Movimentacao movimentacao) {
		return movimentacao.getConta() != null && movimentacao.getDataTransacao() != null && movimentacao.getTipoTransacao() != null && movimentacao.getValorOperacao() != null;
	}
	
	// Definir verificação de Fraude
	public boolean verificarFraude(Movimentacao movimentacao) {
		List<Movimentacao> transacoes = listarPorCliente(movimentacao.getConta().getCliente().getId());
		Double somaSaida = 0.0;
		int numeroSaidas = 0;
		if(transacoes == null) { 
			return true;
		}
		for (Movimentacao movimento : transacoes) {
			if(movimento.getTipoTransacao() == TransacaoTipo.SAQUE || movimento.getTipoTransacao() == TransacaoTipo.PIX || movimento.getTipoTransacao() == TransacaoTipo.PAGAMENTO) {
				somaSaida += movimento.getValorOperacao();
				numeroSaidas += 1;
			} else {
				
			}
		}
		Double mediaSaidas = somaSaida/numeroSaidas;
		if((movimentacao.getTipoTransacao() == TransacaoTipo.SAQUE || movimentacao.getTipoTransacao() == TransacaoTipo.PIX || movimentacao.getTipoTransacao() == TransacaoTipo.PAGAMENTO) && movimentacao.getValorOperacao() > mediaSaidas * 2){
			
		}
		return false;
	}

	public static Double calcularSaldo(Long idConta) {
		List<Movimentacao> transacoes = daoMovimentacao.listarPorConta(idConta);
		Double saldo = 0.0;
		for (Movimentacao movimentacao : transacoes) {
			if(movimentacao.getTipoTransacao() == TransacaoTipo.DEPOSITO) {
				saldo += movimentacao.getValorOperacao();
			} else {
				saldo -= movimentacao.getValorOperacao();
			}
		}
		return saldo;
	}
	
	public static void notificarSaldoBaixo(Long idConta) {
		if (calcularSaldo(idConta) < 100) {
			System.out.println("Saldo Baixo: R$" + calcularSaldo(idConta));
		}
	}
	
	public static Double calcularSaquesDiarios(Long idConta, LocalDate dataTransacao) {
		List<Movimentacao> transacoes = daoMovimentacao.listarExtratoDiarioConta(idConta, dataTransacao);
		Double saques = 0.0;
		for(Movimentacao movimentacao : transacoes) {
			if (movimentacao.getTipoTransacao() == TransacaoTipo.SAQUE && movimentacao.getDataTransacao().toLocalDate().equals(dataTransacao)) {
				saques += movimentacao.getValorOperacao();
			}
		}
		return saques;
	}
	
	public static void aplicarTarifaOperacao(Movimentacao movimentacao) {
		if (movimentacao.getTipoTransacao() == TransacaoTipo.PIX || movimentacao.getTipoTransacao() == TransacaoTipo.PAGAMENTO) {
			movimentacao.setValorOperacao(movimentacao.getValorOperacao() + 5);
		} else if (movimentacao.getTipoTransacao() == TransacaoTipo.SAQUE) {
			movimentacao.setValorOperacao(movimentacao.getValorOperacao() + 2);
		}
	}
	
	public static void aplicarCashback(Movimentacao movimentacao) {
		if(movimentacao.getTipoTransacao() == TransacaoTipo.DEBITO_CARTAO) {
			Double valorCashBack = movimentacao.getValorOperacao() * 0.002;
			Conta conta = movimentacao.getConta();
			conta.setCashBackAcumulado(conta.getCashBackAcumulado() + valorCashBack);
			servicoConta.atualizarConta(conta);
		}
	}
	
	public List<Movimentacao> listarTodos() {
		return daoMovimentacao.listarTodos();
	}
	
	public List<Movimentacao> listarPorCliente(Long idCliente) {
		return daoMovimentacao.listarPorCliente(idCliente);
	}
	
	public List<Movimentacao> listarPorConta(Long idConta) {
		return daoMovimentacao.listarPorConta(idConta);
	}
	
	public List<Movimentacao> listarPorDataTransacao(LocalDate dataTransacao) {
		return daoMovimentacao.listarPorDataTransacao(dataTransacao);
	}
	
	public List<Movimentacao> listarExtratoDiarioCliente(Long idCliente, LocalDate dataTransacao) {
		return daoMovimentacao.listarExtratoDiarioCliente(idCliente, dataTransacao);
	}
	
	public List<Movimentacao> listarExtratoDiarioConta(Long idConta, LocalDate dataTransacao) {
		return daoMovimentacao.listarExtratoDiarioConta(idConta, dataTransacao);
	}
	
	public List<Movimentacao> listarExtratoMensalCliente(Long idCliente, int mes, int ano) {
		return daoMovimentacao.listarExtratoMensalCliente(idCliente, mes, ano);
	}
	
	public List<Movimentacao> listarExtratoMensalConta(Long idConta, int mes, int ano) {
		return daoMovimentacao.listarExtratoMensalConta(idConta, mes, ano);
	}
	
	public List<Movimentacao> listarExtratoPeriodicoCliente(Long idCliente, LocalDate dataInicial, LocalDate dataFinal) {
		return daoMovimentacao.listarExtratoPeriodicoCliente(idCliente, dataInicial, dataFinal);
	}
	
	public List<Movimentacao> listarExtratoPeriodicoConta(Long idConta, LocalDate dataInicial, LocalDate dataFinal) {
		return daoMovimentacao.listarExtratoPeriodicoConta(idConta, dataInicial, dataFinal);
	}
	
	public Movimentacao buscarPorId(Long id) {
		return daoMovimentacao.buscarPorId(id);
	}
}
