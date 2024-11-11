package controle;

import java.time.*;
import java.util.List;

import entidade.Movimentacao;
import servico.MovimentacaoServico;

public class MovimentacaoControle {
	
	MovimentacaoServico servicoMovimentacao = new MovimentacaoServico();
		
	public Movimentacao inserirMovimentacao(Movimentacao movimentacao) {
		return servicoMovimentacao.inserirMovimentacao(movimentacao);
	}
	
	public void excluirMovimentacao(Long idMovimentacao) {
		servicoMovimentacao.excluirMovimentacao(idMovimentacao);
	}
	
	public void excluirPorConta(Long idConta) {
		servicoMovimentacao.excluirPorConta(idConta);
	}
	
	public void excluirPorCliente(Long idCliente) {
		servicoMovimentacao.excluirPorConta(idCliente);
	}
	
	public Movimentacao alterarMovimentacao(Movimentacao movimentacao) {
		return servicoMovimentacao.alterarMovimentacao(movimentacao);
	}
	
	public List<Movimentacao> listarTodos() {
		return servicoMovimentacao.listarTodos();
	}
	
	public List<Movimentacao> listarPorCliente(Long idCliente) {
		return servicoMovimentacao.listarPorCliente(idCliente);
	}
	
	public List<Movimentacao> listarPorConta(Long idConta) {
		return servicoMovimentacao.listarPorConta(idConta);
	}
	
	public List<Movimentacao> listarPorDataTransacao(LocalDate dataTransacao) {
		return servicoMovimentacao.listarPorDataTransacao(dataTransacao);
	}
	
	public List<Movimentacao> listarExtratoMensalConta(Long idConta, int mes, int ano) {
		return servicoMovimentacao.listarExtratoMensalConta(idConta, mes, ano);
	}
	
	public List<Movimentacao> listarExtratoMensalCliente(Long idCliente, int mes, int ano) {
		return servicoMovimentacao.listarExtratoMensalCliente(idCliente, mes, ano);
	}
	
	public List<Movimentacao> listarExtratoPeriodicoConta(Long idConta, LocalDate dataInicial, LocalDate dataFinal) {
		return servicoMovimentacao.listarExtratoPeriodicoConta(idConta, dataInicial, dataFinal);
	}
	
	public List<Movimentacao> listarExtratoPeriodicoCliente(Long idCliente, LocalDate dataInicial, LocalDate dataFinal) {
		return servicoMovimentacao.listarExtratoPeriodicoCliente(idCliente, dataInicial, dataFinal);
	}
	
	public Movimentacao buscarPorId(Long id) {
		return servicoMovimentacao.buscarPorId(id);
	}

}
