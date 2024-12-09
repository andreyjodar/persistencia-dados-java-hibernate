package controle;

import java.time.LocalDate;
import java.util.List;

import entidade.Movimentacao;
import servico.MovimentacaoServico;

public class MovimentacaoControle {
	
	MovimentacaoServico servicoMovimentacao = new MovimentacaoServico();
		
	public Movimentacao inserir(Movimentacao movimentacao) {
		return servicoMovimentacao.inserir(movimentacao);
	}
	
	public void excluir(Long idMovimentacao) {
		servicoMovimentacao.excluir(idMovimentacao);
	}
	
	public void excluirPorConta(Long idConta) {
		servicoMovimentacao.excluirPorConta(idConta);
	}
	
	public void excluirPorCliente(Long idCliente) {
		servicoMovimentacao.excluirPorConta(idCliente);
	}
	
	public Movimentacao alterar(Movimentacao movimentacao) {
		return servicoMovimentacao.alterar(movimentacao);
	}
	
	public Movimentacao getMovimentacaoPorId(Long idMovimentacao) {
		return servicoMovimentacao.buscarPorId(idMovimentacao);
	}
	
	public List<Movimentacao> getTodasMovimentacoes() {
		return servicoMovimentacao.listarTodos();
	}
	
	public List<Movimentacao> getMovimentacoesPorCliente(Long idCliente) {
		return servicoMovimentacao.listarPorCliente(idCliente);
	}
	
	public List<Movimentacao> getMovimentacoesPorConta(Long idConta) {
		return servicoMovimentacao.listarPorConta(idConta);
	}
	
	public List<Movimentacao> getMovimentacoesPorDataTransacao(LocalDate dataTransacao) {
		return servicoMovimentacao.listarPorDataTransacao(dataTransacao);
	}
	
	public List<Movimentacao> getExtratoMensalPorConta(Long idConta, int mes, int ano) {
		return servicoMovimentacao.listarExtratoMensalConta(idConta, mes, ano);
	}
	
	public List<Movimentacao> getExtratoMensalPorCliente(Long idCliente, int mes, int ano) {
		return servicoMovimentacao.listarExtratoMensalCliente(idCliente, mes, ano);
	}
	
	public List<Movimentacao> getExtratoPeriodicoPorConta(Long idConta, LocalDate dataInicial, LocalDate dataFinal) {
		return servicoMovimentacao.listarExtratoPeriodicoConta(idConta, dataInicial, dataFinal);
	}
	
	public List<Movimentacao> getExtratoPeriodicoPorCliente(Long idCliente, LocalDate dataInicial, LocalDate dataFinal) {
		return servicoMovimentacao.listarExtratoPeriodicoCliente(idCliente, dataInicial, dataFinal);
	}

}
