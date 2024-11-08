package servico;

import java.time.*;
import java.util.List;

import dao.MovimentacaoDAO;
import entidade.Movimentacao;

public class MovimentacaoServico {
	static MovimentacaoDAO dao = new MovimentacaoDAO();
	
	public Movimentacao inserir(Movimentacao movimentacao) {
		movimentacao.setDataTransacao(LocalDateTime.now());
		if(movimentacao.getConta() != null) {
			aplicarTarifaOperacao(movimentacao);
			if(verificarLimites(movimentacao)) {
				if(verificarSaldo(movimentacao)) {
					if(verificarFraude(movimentacao)) {
						Movimentacao contaBanco = dao.inserir(movimentacao);
						notificarSaldoBaixo(movimentacao.getCpfCorrentista());
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
	public boolean verificarFraude(Movimentacao conta) {
		List<Movimentacao> transacoes = listarPorCpf(conta.getCpfCorrentista());
		Double somaSaida = 0.0;
		int numeroSaidas = 0;
		if(transacoes == null) { 
			return true;
		}
		for (Movimentacao movimentacao : transacoes) {
			if(movimentacao.getTipoTransacao().equals("Saque") || movimentacao.getTipoTransacao().equals("Pix") || movimentacao.getTipoTransacao().equals("Pagamento")) {
				somaSaida += movimentacao.getValorOperacao();
				numeroSaidas += 1;
			} else {
				
			}
		}
		Double mediaSaidas = somaSaida/numeroSaidas;
		if((conta.getTipoTransacao().equals("Saque") || conta.getTipoTransacao().equals("Pix") || conta.getTipoTransacao().equals("Pagamento")) && conta.getValorOperacao() > mediaSaidas * 2){
			
		}
		return false;
	}

	public void excluir(Long idConta) {
		dao.excluir(idConta);
	}
	
	public Movimentacao alterar(Movimentacao conta) {
		return dao.alterar(conta);
	}
	
	public static boolean verificarLimites(Movimentacao conta) {
		List<Movimentacao> transacoes = dao.listarPorCpfEDataTransacao(conta.getCpfCorrentista(), conta.getDataTransacao().toLocalDate());
		if(transacoes.size() >= 10) {
			return false;
		} else if (conta.getTipoTransacao().equals("Pix")) {
			if (conta.getValorOperacao() > 300 || conta.getDataTransacao().getHour() > 22 || conta.getDataTransacao().getHour() < 6) {
				return false;
			} 
			return true;
		} else if (conta.getTipoTransacao().equals("Saque")) {
			if (calcularSaquesDiarios(conta.getCpfCorrentista(), conta.getDataTransacao().toLocalDate()) >= 5000) {
				return false;
			} 
			return true;
		} 
		return true;
	}

	public static boolean verificarSaldo(Movimentacao conta) {
		if(conta.getTipoTransacao().equals("Saque") || conta.getTipoTransacao().equals("Pix")|| conta.getTipoTransacao().equals("Pagamento")) {
			if (calcularSaldo(conta.getCpfCorrentista()) >= conta.getValorOperacao()) {
				return true;
			} else {
				return false;
			}
		}
		return true;

	}
	
	public static boolean verificarCpf(String cpfCorrentista) {
		if(cpfCorrentista.length() != 11) {
			return false;
		}
		
		String numBase = cpfCorrentista.substring(0,9);
		int primeiroDigito = calcularDigitoVerificador(numBase, 10);
		int segundoDigito = calcularDigitoVerificador(numBase + primeiroDigito, 11);
		return cpfCorrentista.equals(numBase + primeiroDigito + segundoDigito);
	}
	
	public static int calcularDigitoVerificador(String numBase, int pesoInicial) {
		int soma = 0;
		for (int i = 0; i < numBase.length(); i++) {
			int numero = Character.getNumericValue(numBase.charAt(i));
			soma += numero * pesoInicial--;
		}
		
		if (soma % 11 > 2) {
			return 11 - (soma % 11);
		} 
		return 0;
	}

	public static Double calcularSaldo(String cpfCorrentista) {
		List<Movimentacao> transacoes = dao.listarPorCpf(cpfCorrentista);
		Double saldo = 0.0;
		for (Movimentacao movimentacao : transacoes) {
			if(movimentacao.getTipoTransacao().equals("Depósito")) {
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
		List<Movimentacao> transacoes = dao.listarPorCpf(cpfCorrentista);
		Double saques = 0.0;
		for(Movimentacao movimentacao : transacoes) {
			if (movimentacao.getTipoTransacao().equals("Saques") && movimentacao.getDataTransacao().toLocalDate().equals(dataTransacao)) {
				saques += movimentacao.getValorOperacao();
			}
		}
		return saques;
	}
	
	public static void aplicarTarifaOperacao(Movimentacao conta) {
		if (conta.getTipoTransacao().equals("Pix") || conta.getTipoTransacao().equals("Pagamento")) {
			conta.setValorOperacao(conta.getValorOperacao() + 5);
		} else if (conta.getTipoTransacao().equals("Saque")) {
			conta.setValorOperacao(conta.getValorOperacao() + 2);
		}
	}
	
	public List<Movimentacao> listarTodos() {
		return dao.listarTodos();
	}
	
	public List<Movimentacao> listarPorCpf(String cpfCorrentista) {
		return dao.listarPorCpf(cpfCorrentista);
	}
	
	public List<Movimentacao> listarPorDataTransacao(LocalDate dataTransacao) {
		return dao.listarPorDataTransacao(dataTransacao);
	}
	
	public List<Movimentacao> listarPorCpfEDataTransacao(String cpfCorrentista, LocalDate dataTransacao){
		return dao.listarPorCpfEDataTransacao(cpfCorrentista, dataTransacao);
	}
	
	public Movimentacao buscarPorId(Long id) {
		return dao.buscarPorId(id);
	}
	
	public List<Movimentacao> listarExtratoPeriodico(String cpfCorrentista, LocalDate dataInicial, LocalDate dataFinal) {
		return dao.listarExtratoPeriodico(cpfCorrentista, dataInicial, dataFinal);
	}
}
