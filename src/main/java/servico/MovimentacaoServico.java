package servico;

import java.time.*;
import java.util.List;

import dao.MovimentacaoDAO;
import entidade.Movimentacao;
import entidade.TransacaoTipo;

public class MovimentacaoServico {
	static MovimentacaoDAO daoMovimentacao = new MovimentacaoDAO();
	
	public Movimentacao inserirMovimentacao(Movimentacao movimentacao) {
		movimentacao.setDataTransacao(LocalDateTime.now());
		if(verificarCamposNaoNulos(movimentacao) && movimentacao.getConta().getId() != null && movimentacao.getConta().getCliente() != null && movimentacao.getConta().getCliente().getId() != null) {
			aplicarTarifaOperacao(movimentacao);
			if(verificarLimites(movimentacao)) {
				if(verificarSaldo(movimentacao)) {
					if(verificarFraude(movimentacao)) {
						Movimentacao contaBanco = daoMovimentacao.inserirMovimentacao(movimentacao);
						notificarSaldoBaixo(movimentacao.getConta().getCliente().getCpf());
						return contaBanco;
					} 
					return null;
				} 
				return null;
			} 
			return null;
		}
		return null;
	}
	
	// Definir verificação de Fraude
	public boolean verificarFraude(Movimentacao movimentacao) {
		List<Movimentacao> transacoes = listarPorCpf(movimentacao.getConta().getCliente().getCpf());
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
		if(daoMovimentacao.listarPorCpfEDataTransacao(movimentacao.getConta().getCliente().getCpf(), movimentacao.getDataTransacao().toLocalDate()).size() >= 10) {
			return false;
		} else if (movimentacao.getTipoTransacao() == TransacaoTipo.PIX) {
			if (movimentacao.getValorOperacao() > 300 || movimentacao.getDataTransacao().getHour() > 22 || movimentacao.getDataTransacao().getHour() < 6) {
				return false;
			} 
			return true;
		} else if (movimentacao.getTipoTransacao() == TransacaoTipo.SAQUE) {
			if (calcularSaquesDiarios(movimentacao.getConta().getCliente().getCpf(), movimentacao.getDataTransacao().toLocalDate()) >= 5000) {
				return false;
			} 
			return true;
		} 
		return true;
	}

	public static boolean verificarSaldo(Movimentacao movimentacao) {
		if(movimentacao.getTipoTransacao() == TransacaoTipo.SAQUE|| movimentacao.getTipoTransacao() == TransacaoTipo.PIX || movimentacao.getTipoTransacao() == TransacaoTipo.PAGAMENTO) {
			if (calcularSaldo(movimentacao.getConta().getCliente().getCpf()) >= movimentacao.getValorOperacao()) {
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

	public static Double calcularSaldo(String cpfCorrentista) {
		List<Movimentacao> transacoes = daoMovimentacao.listarPorCpf(cpfCorrentista);
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
	
	public static void notificarSaldoBaixo(String cpfCorrentista) {
		if (calcularSaldo(cpfCorrentista) < 100) {
			System.out.println("Saldo Baixo: R$" + calcularSaldo(cpfCorrentista));
		}
	}
	
	public static Double calcularSaquesDiarios(String cpfCorrentista, LocalDate dataTransacao) {
		List<Movimentacao> transacoes = daoMovimentacao.listarPorCpf(cpfCorrentista);
		Double saques = 0.0;
		for(Movimentacao movimentacao : transacoes) {
			if (movimentacao.getTipoTransacao() == TransacaoTipo.SAQUE && movimentacao.getDataTransacao().toLocalDate().equals(dataTransacao)) {
				saques += movimentacao.getValorOperacao();
			}
		}
		return saques;
	}
	
	public static void aplicarTarifaOperacao(Movimentacao conta) {
		if (conta.getTipoTransacao() == TransacaoTipo.PIX || conta.getTipoTransacao() == TransacaoTipo.PAGAMENTO) {
			conta.setValorOperacao(conta.getValorOperacao() + 5);
		} else if (conta.getTipoTransacao() == TransacaoTipo.SAQUE) {
			conta.setValorOperacao(conta.getValorOperacao() + 2);
		}
	}
	
	public List<Movimentacao> listarTodos() {
		return daoMovimentacao.listarTodos();
	}
	
	public List<Movimentacao> listarPorCpf(String cpfCliente) {
		return daoMovimentacao.listarPorCpf(cpfCliente);
	}
	
	public List<Movimentacao> listarPorDataTransacao(LocalDate dataTransacao) {
		return daoMovimentacao.listarPorDataTransacao(dataTransacao);
	}
	
	public List<Movimentacao> listarPorCpfEDataTransacao(String cpfCliente, LocalDate dataTransacao){
		return daoMovimentacao.listarPorCpfEDataTransacao(cpfCliente, dataTransacao);
	}
	
	public Movimentacao buscarPorId(Long id) {
		return daoMovimentacao.buscarPorId(id);
	}
	
	public List<Movimentacao> listarExtratoPeriodico(String cpfCliente, LocalDate dataInicial, LocalDate dataFinal) {
		return daoMovimentacao.listarExtratoPeriodico(cpfCliente, dataInicial, dataFinal);
	}
}
