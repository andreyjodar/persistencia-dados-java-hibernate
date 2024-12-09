package servico;

import java.time.LocalDate;
import java.util.List;

import dao.MovimentacaoDAO;
import entidade.ContaTipo;
import entidade.Movimentacao;
import entidade.TransacaoTipo;

public class MovimentacaoServico implements InterfaceServico<Movimentacao> {
	static MovimentacaoDAO daoMovimentacao = new MovimentacaoDAO();
	static ContaServico servicoConta = new ContaServico();
	
	@Override
	public Movimentacao inserir(Movimentacao movimentacao) {
		if(verificarCamposNaoNulos(movimentacao)) {
			aplicarTarifaOperacao(movimentacao);
			if(verificarLimitesTransacao(movimentacao)) {
				if(verificarSaldo(movimentacao)) {
					if(verificarFraude(movimentacao)) {
						return daoMovimentacao.inserir(movimentacao);
					}
					return null;
				} 
				return null;
			} 
			return null;
		}
		return null;
	}

	@Override
	public void excluir(Long idMovimentacao) {
		daoMovimentacao.excluir(idMovimentacao);
	}

	public void excluirPorConta(Long idConta) {
		daoMovimentacao.excluirPorConta(idConta);
	}

	public void excluirPorCliente(Long idCliente) {
		daoMovimentacao.excluirPorCliente(idCliente);
	}

	public Movimentacao alterar(Movimentacao movimentacao) {
		return daoMovimentacao.alterar(movimentacao);
	}
	
	private static boolean verificarCamposNaoNulos(Movimentacao movimentacao) {
		return movimentacao.getConta() != null && movimentacao.getConta().getId() != null && movimentacao.getDataTransacao() != null && movimentacao.getTipoTransacao() != null && movimentacao.getValorOperacao() != null;
	}
	
	private static void aplicarTarifaOperacao(Movimentacao movimentacao) {
		if (movimentacao.getTipoTransacao() == TransacaoTipo.PIX || movimentacao.getTipoTransacao() == TransacaoTipo.PAGAMENTO) {
			movimentacao.setValorOperacao(movimentacao.getValorOperacao() + 5);
		} else if (movimentacao.getTipoTransacao() == TransacaoTipo.SAQUE) {
			movimentacao.setValorOperacao(movimentacao.getValorOperacao() + 2);
		} else if (movimentacao.getTipoTransacao() == TransacaoTipo.DEBITO_CARTAO) {
			movimentacao.setCashback(movimentacao.getValorOperacao() * 0.002);
		}
	}
	
	private static boolean verificarLimitesTransacao(Movimentacao movimentacao) {
		if(daoMovimentacao.listarExtratoDiarioConta(movimentacao.getConta().getId(), movimentacao.getDataTransacao().toLocalDate()).size() >= 10) {
			return false;
		} else if(movimentacao.getConta().getContaTipo() == ContaTipo.CONTA_CORRENTE) {
			if(movimentacao.getTipoTransacao() == TransacaoTipo.PIX) {
				if(movimentacao.getValorOperacao() > 300 || movimentacao.getDataTransacao().getHour() > 21 || movimentacao.getDataTransacao().getHour() < 6) {
					return false;
				}
				return true;
			} else if(movimentacao.getTipoTransacao() == TransacaoTipo.SAQUE) {
				if(servicoConta.calcularSaquePorDia(movimentacao.getConta().getId(), movimentacao.getDataTransacao().toLocalDate()) + movimentacao.getValorOperacao() > 5000) {
					return false;
				}
				return true;
			} else if(movimentacao.getTipoTransacao() == TransacaoTipo.EMPRESTIMO) {
				if(verificarLimiteCredito(movimentacao.getConta().getId(), movimentacao.getValorOperacao())) {
					return true;
				}
				return false;
			} else {
				return true;
			}
		} else {
			if(movimentacao.getTipoTransacao() == TransacaoTipo.DEPOSITO) {
				return true;
			} else if(movimentacao.getTipoTransacao() == TransacaoTipo.SAQUE) {
				if(servicoConta.calcularSaquePorDia(movimentacao.getConta().getId(), movimentacao.getDataTransacao().toLocalDate()) + movimentacao.getValorOperacao() > 3000) {
					return false;
				}
				return true;
			} else if(movimentacao.getTipoTransacao() == TransacaoTipo.PAGAMENTO || movimentacao.getTipoTransacao() == TransacaoTipo.DEBITO_CARTAO) {
				if(movimentacao.getValorOperacao() > 1500) {
					return false;
				}
				return true;
			} else {
				return false;
			}
		}
	}
	
	private static boolean verificarLimiteCredito(Long idConta, Double valorMovimentacao) {
		Double limiteCredito = servicoConta.calcularLimiteCredito(idConta);
		if(valorMovimentacao <= limiteCredito) {
			return true;
		}
		return false;
	}

	private static boolean verificarSaldo(Movimentacao movimentacao) {
		if(movimentacao.getTipoTransacao() == TransacaoTipo.SAQUE || movimentacao.getTipoTransacao() == TransacaoTipo.PIX || movimentacao.getTipoTransacao() == TransacaoTipo.PAGAMENTO || movimentacao.getTipoTransacao() == TransacaoTipo.DEBITO_CARTAO) {
			if (servicoConta.calcularSaldo(movimentacao.getConta().getId()) >= movimentacao.getValorOperacao()) {
				return true;
			} else {
				return false;
			}
		}
		return true;

	}
	
	private static boolean verificarFraude(Movimentacao movimentacao) {
		LocalDate dataTransacao = movimentacao.getDataTransacao().toLocalDate();
		LocalDate fimUltimoMes = dataTransacao.minusMonths(1).withDayOfMonth(dataTransacao.minusMonths(1).lengthOfMonth());
		LocalDate inicioUltimoMes = fimUltimoMes.withDayOfMonth(1);
		LocalDate fimPenultimoMes = fimUltimoMes.minusMonths(1).withDayOfMonth(fimUltimoMes.minusMonths(1).lengthOfMonth());
		LocalDate inicioPenultimoMes = fimPenultimoMes.withDayOfMonth(1);
		
		Double saldoUltimoMes = servicoConta.calcularSaldo(movimentacao.getConta().getId(), fimUltimoMes);
		Double saldoPenultimoMes = servicoConta.calcularSaldo(movimentacao.getConta().getId(), fimPenultimoMes);

		Double diferencaSaldo = Math.abs(saldoUltimoMes - saldoPenultimoMes);
		Double limiteDiferenca = saldoPenultimoMes * 0.3;
		if(saldoPenultimoMes != 0 && diferencaSaldo > limiteDiferenca) {
			return false;
		} 
		
	    Double mediaTransacoesUltimoMes = calcularMediaPorTipoTransacao(movimentacao.getConta().getId(), movimentacao.getTipoTransacao(), inicioUltimoMes, fimUltimoMes);
	    Double mediaTransacoesPenultimoMes = calcularMediaPorTipoTransacao(movimentacao.getConta().getId(), movimentacao.getTipoTransacao(), inicioPenultimoMes, fimPenultimoMes);
	    Double diferencaMedia = Math.abs(mediaTransacoesUltimoMes - mediaTransacoesPenultimoMes);
	    Double limiteDiferencaMedia = 0.5 * mediaTransacoesPenultimoMes; 
	    if (movimentacao.getTipoTransacao() == TransacaoTipo.DEPOSITO || movimentacao.getTipoTransacao() == TransacaoTipo.PIX) {
	    	return true;
	    } else if (mediaTransacoesPenultimoMes != 0 && (diferencaMedia < limiteDiferencaMedia || movimentacao.getValorOperacao() > mediaTransacoesUltimoMes * 2)) {
	        return false;
	    }
	    return true;
	}
	
	private static Double calcularMediaPorTipoTransacao(Long idConta, TransacaoTipo tipoTransacao, LocalDate dataInicial, LocalDate dataFinal) {
		List<Movimentacao> transacoes = daoMovimentacao.listarPeriodicoPorTipoTransacao(idConta, tipoTransacao, dataInicial, dataFinal);
		if (transacoes.isEmpty()) {
			return 0.0;
		}
		Double somaTransacoes = 0.0;
		for (Movimentacao movimentacao : transacoes) {
			somaTransacoes += movimentacao.getValorOperacao();
		}
		return somaTransacoes / transacoes.size();
	}
	
	@Override
	public Movimentacao buscarPorId(Long id) {
		return daoMovimentacao.buscarPorId(id);
	}
	
	@Override
	public List<Movimentacao> listarTodos() {
		return daoMovimentacao.listarTodos();
	}

	public List<Movimentacao> listarPorTipoTransacao(TransacaoTipo tipoTransacao) {
		return daoMovimentacao.listarPorTipoTransacao(tipoTransacao);
	}

	public List<Movimentacao> listarPorDataTransacao(LocalDate dataTransacao) {
		return daoMovimentacao.listarPorDataTransacao(dataTransacao);
	}

	public List<Movimentacao> listarPorCliente(Long idCliente) {
		return daoMovimentacao.listarPorCliente(idCliente);
	}

	public List<Movimentacao> listarPorConta(Long idConta) {
		return daoMovimentacao.listarPorConta(idConta);
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
	
	public List<Movimentacao> listarPeriodicoPorTipoTransacao(Long idConta, TransacaoTipo tipoTransacao, LocalDate dataInicial, LocalDate dataFinal){
		return daoMovimentacao.listarPeriodicoPorTipoTransacao(idConta, tipoTransacao, dataInicial, dataFinal);
	}
	
}
