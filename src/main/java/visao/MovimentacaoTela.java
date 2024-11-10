package visao;

import java.time.*;

import controle.MovimentacaoControle;
import dao.ClienteDAO;
import dao.ContaDAO;
import dao.MovimentacaoDAO;
import entidade.Cliente;
import entidade.Conta;
import entidade.ContaTipo;
import entidade.Movimentacao;
import entidade.TransacaoTipo;

public class MovimentacaoTela {

	public static void main(String[] args) {
		ClienteDAO daoCliente = new ClienteDAO();
		ContaDAO daoConta = new ContaDAO();	
		MovimentacaoDAO daoMovimentacao = new MovimentacaoDAO();
		
		Cliente cliente = new Cliente();
		cliente.setCpf("10162344902");
		cliente.setNome("Andrey Jodar");
		cliente.setDataNascimento(LocalDate.parse("2005-10-14"));
		
		Conta conta = new Conta();
		conta.setCliente(daoCliente.inserirCliente(cliente));
		conta.setContaTipo(ContaTipo.CONTA_CORRENTE);
		conta.setDataAbertura(LocalDateTime.now());
		
		
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setConta(daoConta.inserirConta(conta));
		movimentacao.setDataTransacao(LocalDateTime.now());
		movimentacao.setTipoTransacao(TransacaoTipo.DEPOSITO);
		movimentacao.setValorOperacao(200.0);
		movimentacao.setDescricao("Depósito de R$200 07/11/2024");
		
		movimentacao = daoMovimentacao.inserirMovimentacao(movimentacao);
	}

}
