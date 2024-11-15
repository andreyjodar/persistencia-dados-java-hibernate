package controle;

import java.time.LocalDate;
import java.util.List;

import entidade.Conta;
import entidade.ContaTipo;
import servico.ContaServico;

public class ContaControle {
	ContaServico servicoConta = new ContaServico();
	
	public Conta inserirConta(Conta conta) {
		return servicoConta.inserirConta(conta);
	}
	
	public Conta alterarConta(Conta conta) {
		return servicoConta.inserirConta(conta);
	}
	
	public void excluirConta(Long idConta) {
		servicoConta.excluirConta(idConta);
	}
	
	public Conta getContaPorId(Long idConta) {
		return servicoConta.buscarPorId(idConta);
	}
	
	public List<Conta> getTodasContas() {
		return servicoConta.listarTodasContas();
	}
	
	public List<Conta> getContasPorIdCliente(Long idCliente) {
		return servicoConta.listarPorIdCliente(idCliente);
	}
	
	public List<Conta> getContasPorTipoConta(ContaTipo contaTipo) {
		return servicoConta.listarPorContaTipo(contaTipo);
	}
	
	public List<Conta> getContasPorPeriodoCriacao(LocalDate dataInicial, LocalDate dataFinal) {
		return servicoConta.listarPorPeriodoCriacao(dataInicial, dataFinal);
	}
}
