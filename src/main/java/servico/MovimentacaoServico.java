package servico;

import java.util.Date;
import java.time.*;
import java.util.List;

import dao.MovimentacaoDAO;
import entidade.Movimentacao;

public class MovimentacaoServico {
	static MovimentacaoDAO dao = new MovimentacaoDAO();
	
	
	public Movimentacao inserir(Movimentacao conta) {
		conta.setDataTransacao(LocalDateTime.now());
		if(verificarLimites(conta)) {
			if(verificacarSaldo(conta)) {
				if(verificarFraude(conta)) {
					aplicarTarifaOperacao(conta);
					Movimentacao contaBanco = dao.inserir(conta);
					notificarSaldoBaixo(conta.getCpfCorrentista());
					return contaBanco;
				}
				return null;
			}
			return null;
		} 
		return null;
	}
	
	public boolean verificarFraude(Movimentacao conta) {

		return false;
	}

	public void excluir(Long idConta) {
		dao.excluir(idConta);
	}
	
	public static boolean verificarLimites(Movimentacao conta) {
		if(verificarCpf(conta.getCpfCorrentista())) {
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
		return false;
	}

	public static boolean verificacarSaldo(Movimentacao conta) {
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
	
}
