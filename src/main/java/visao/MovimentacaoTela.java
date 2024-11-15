package visao;

import java.time.*;
import controle.*;
import entidade.*;

public class MovimentacaoTela {

	public static void main(String[] args) {
		ClienteControle controleCliente = new ClienteControle();
		ContaControle controleConta = new ContaControle();	
		MovimentacaoControle controleMovimentacao = new MovimentacaoControle();
		
		/* Terminar
		 *   - Análise de Fraude
		 *   - Análise de Crédito e Empréstimo
		 *   - Conexão DAO-Servico-Controle
		 * Validar
		 *   - Lógica com LocalDate.now()
		 *   - Diagrama de Classes
		 *   - Métodos de Cashback
		 * */
		
		controleCliente.inserirCliente(new Cliente("Andrey Vinícius Jodar", "10162344902", LocalDate.parse("2005-10-14")));
		controleCliente.inserirCliente(new Cliente("Carlinhos Jurandir", "11111111111", LocalDate.parse("2000-07-24")));
		controleCliente.inserirCliente(new Cliente("Darleide Santos", "76664333953", LocalDate.parse("1998-03-01")));
		controleCliente.inserirCliente(new Cliente("Gisele Menezes", "76664333953", LocalDate.parse("1980-02-28")));
		
		controleConta.inserirConta(new Conta(controleCliente.getClientePorId(1L), ContaTipo.CONTA_CORRENTE));
		controleConta.inserirConta(new Conta(controleCliente.getClientePorId(1L), ContaTipo.CONTA_POUPANCA));
		controleConta.inserirConta(new Conta(controleCliente.getClientePorId(1L), ContaTipo.CONTA_CORRENTE));
		controleConta.inserirConta(new Conta(controleCliente.getClientePorId(1L), ContaTipo.CONTA_POUPANCA));

		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.DEPOSITO, 10000.0, "Depósito R$ 10000,00"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.PIX, 3000.0, "Pix R$ 3000,00"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.PIX, 200.0, "Pix R$ 200,00"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.PAGAMENTO, 1000.0, "Pagamento R$ 1000,00"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.SAQUE, 3000.0, "Saque R$ 3000,00"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.SAQUE, 3000.0, "Saque R$ 3000,00"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.DEBITO_CARTAO, 1000.0, "Débito R$ 1000,00"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.DEBITO_CARTAO, 1500.0, "Débito R$ 1500,00"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.PAGAMENTO, 1500.0, "Pagamento R$ 1500,00"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.PAGAMENTO, 3000.0, "Pagamento R$ 3000,00"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.SAQUE, 1000.0, "Pagamento R$ 1000,00"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.PAGAMENTO, 1500.0, "Pagamento R$ 1500,00"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.DEPOSITO, 4000.0, "Depósito R$ 4000,00"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.DEPOSITO, 5000.0, "Depósito R$ 5000,00"));
		controleMovimentacao.inserirMovimentacao(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.DEPOSITO, 6000.0, "Depósito R$ 6000,00"));

	}

}
