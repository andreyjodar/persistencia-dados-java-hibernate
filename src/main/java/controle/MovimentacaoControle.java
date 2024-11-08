package controle;

import java.time.*;
import java.util.List;

import entidade.Movimentacao;
import servico.MovimentacaoServico;

public class MovimentacaoControle {
	
	MovimentacaoServico servico = new MovimentacaoServico();
		
	public Movimentacao inserir(Movimentacao conta) {
		return servico.inserir(conta);
	}
	
	public void excluir(Long idConta) {
		servico.excluir(idConta);
	}
	
	public Movimentacao alterar(Movimentacao conta) {
		return servico.alterar(conta);
	}
	
	public List<Movimentacao> listarTodos() {
		return servico.listarTodos();
	}
	
	public List<Movimentacao> listarPorCpf(String cpfCorrentista) {
		return servico.listarPorCpf(cpfCorrentista);
	}
	
	public List<Movimentacao> listarPorDataTransacao(LocalDate dataTransacao) {
		return servico.listarPorDataTransacao(dataTransacao);
	}
	
	public List<Movimentacao> listarPorCpfEDataTransacao(String cpfCorrentista, LocalDate dataTransacao) {
		return servico.listarPorCpfEDataTransacao(cpfCorrentista, dataTransacao);
	}
	
	public List<Movimentacao> listarExtratoPeriodico(String cpfCorrentista, LocalDate dataInicial, LocalDate dataFinal) {
		return servico.listarExtratoPeriodico(cpfCorrentista, dataInicial, dataFinal);
	}
	public Movimentacao buscarPorId(Long id) {
		return servico.buscarPorId(id);
	}

}
