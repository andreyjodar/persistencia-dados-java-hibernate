package visao;

import java.time.*;

import controle.ClienteControle;
import controle.ContaControle;
import controle.MovimentacaoControle;
import entidade.Cliente;
import entidade.Conta;
import entidade.ContaTipo;
import entidade.Movimentacao;
import entidade.TransacaoTipo;

public class MovimentacaoTela {

	public static void main(String[] args) {
		ClienteControle controleCliente = new ClienteControle();
		ContaControle controleConta = new ContaControle();	
		MovimentacaoControle controleMovimentacao = new MovimentacaoControle();
		
		Cliente cliente = new Cliente("Andrey Vinícius Jodar", "10162344902", LocalDate.parse("2005-10-14"));
		cliente = controleCliente.inserirCliente(cliente);
		
		Conta conta = new Conta(cliente, ContaTipo.CONTA_CORRENTE, LocalDateTime.now());
		conta = controleConta.inserirConta(conta);
		
		Movimentacao movimentacao = new Movimentacao(conta, TransacaoTipo.DEPOSITO, 4000.0, "Depósito de R$200 07/11/2024");
		movimentacao = controleMovimentacao.inserirMovimentacao(movimentacao);
		
		Movimentacao movimentacao2 = new Movimentacao(controleConta.buscarPorId(1L), TransacaoTipo.DEPOSITO, 3000.0, "Depósito de R$200 07/11/2024");
		movimentacao2 = controleMovimentacao.inserirMovimentacao(movimentacao2);
		
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.buscarPorId(1L), TransacaoTipo.PIX, 300.0, "Teste"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.buscarPorId(1L), TransacaoTipo.PIX, 200.0, "Teste"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.buscarPorId(1L), TransacaoTipo.DEBITO_CARTAO, 3000.0, "Teste"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.buscarPorId(1L), TransacaoTipo.PAGAMENTO, 300.0, "Teste"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.buscarPorId(1L), TransacaoTipo.SAQUE, 4000.0, "Teste"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.buscarPorId(1L), TransacaoTipo.SAQUE, 1000.0, "Teste"));
		
	}

}
