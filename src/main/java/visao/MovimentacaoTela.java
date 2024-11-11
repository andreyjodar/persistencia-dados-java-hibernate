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
		
	}

}
