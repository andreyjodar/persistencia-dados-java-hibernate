package controle;

import java.time.LocalDate;
import java.util.List;

import entidade.Conta;
import entidade.ContaTipo;
import servico.ContaServico;

public class ContaControle {
	ContaServico servicoConta = new ContaServico();
	
	public Conta inserir(Conta conta) {
		return servicoConta.inserir(conta);
	}
	
	public Conta alterar(Conta conta) {
		return servicoConta.inserir(conta);
	}
	
	public void excluir(Long idConta) {
		servicoConta.excluir(idConta);
	}
	
	public Conta getContaPorId(Long idConta) {
		return servicoConta.buscarPorId(idConta);
	}
	
	public List<Conta> getTodasContas() {
		return servicoConta.listarTodos();
	}
	
	public List<Conta> getContasPorCliente(Long idCliente) {
		return servicoConta.listarPorCliente(idCliente);
	}
	
	public List<Conta> getContasPorTipoConta(ContaTipo contaTipo) {
		return servicoConta.listarPorContaTipo(contaTipo);
	}
	
	public List<Conta> getContasPorPeriodoCriacao(LocalDate dataInicial, LocalDate dataFinal) {
		return servicoConta.listarPorPeriodoCriacao(dataInicial, dataFinal);
	}
}
