package visao;

import java.time.LocalDate;
import java.util.List;

import controle.*;
import entidade.*;

public class MovimentacaoTela {

	public static void main(String[] args) {
		ClienteControle controleCliente = new ClienteControle();
		ContaControle controleConta = new ContaControle();	
		MovimentacaoControle controleMovimentacao = new MovimentacaoControle();
		
		controleCliente.inserir(new Cliente("Andrey Vinícius Jodar", "10162344902", LocalDate.parse("2005-10-14")));
		controleCliente.inserir(new Cliente("Carlinhos Jurandir", "11111111111", LocalDate.parse("2000-07-24")));
		controleCliente.inserir(new Cliente("Darleide Santos", "76664333953", LocalDate.parse("1998-03-01")));
		controleCliente.inserir(new Cliente("Gisele Menezes", "76664333953", LocalDate.parse("1980-02-28")));
		
		controleConta.inserir(new Conta(controleCliente.getClientePorId(1L), ContaTipo.CONTA_CORRENTE));
		controleConta.inserir(new Conta(controleCliente.getClientePorId(1L), ContaTipo.CONTA_POUPANCA));
		controleConta.inserir(new Conta(controleCliente.getClientePorId(1L), ContaTipo.CONTA_CORRENTE));
		controleConta.inserir(new Conta(controleCliente.getClientePorId(1L), ContaTipo.CONTA_POUPANCA));

		Movimentacao mov = controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.DEPOSITO, 10000.0, "Depósito R$ 10000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.PIX, 3000.0, "Pix R$ 3000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.PIX, 200.0, "Pix R$ 200,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.PAGAMENTO, 1000.0, "Pagamento R$ 1000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.SAQUE, 3000.0, "Saque R$ 3000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.SAQUE, 3000.0, "Saque R$ 3000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.DEBITO_CARTAO, 1000.0, "Débito R$ 1000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.DEBITO_CARTAO, 1500.0, "Débito R$ 1500,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.PAGAMENTO, 1500.0, "Pagamento R$ 1500,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.PAGAMENTO, 3000.0, "Pagamento R$ 3000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.SAQUE, 1000.0, "Pagamento R$ 1000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.PAGAMENTO, 1500.0, "Pagamento R$ 1500,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.DEPOSITO, 4000.0, "Depósito R$ 4000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.DEPOSITO, 5000.0, "Depósito R$ 5000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(1L), TransacaoTipo.DEPOSITO, 6000.0, "Depósito R$ 6000,00"));
		
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(2L), TransacaoTipo.DEPOSITO, 5000.0, "Depósito R$ 5000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(2L), TransacaoTipo.PIX, 3000.0, "Pix R$ 3000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(2L), TransacaoTipo.DEPOSITO, 7000.0, "Depósito R$ 7000,00"));	
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(2L), TransacaoTipo.SAQUE, 1000.0, "Depósito R$ 1000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(2L), TransacaoTipo.SAQUE, 5000.0, "Depósito R$ 5000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(2L), TransacaoTipo.PAGAMENTO, 10000.0, "Pagamento R$ 10000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(2L), TransacaoTipo.PAGAMENTO, 1000.0, "Pagamento R$ 1000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(2L), TransacaoTipo.EMPRESTIMO, 7000.0, "Empréstimo R$ 7000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(2L), TransacaoTipo.DEBITO_CARTAO, 3000.0, "Débito R$ 3000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(2L), TransacaoTipo.DEBITO_CARTAO, 1000.0, "Débito R$ 1000,00"));
		controleMovimentacao.inserir(new Movimentacao(controleConta.getContaPorId(2L), TransacaoTipo.DEPOSITO, 8000.0, "Depósito R$ 8000,00"));
		
		imprimirExtrato(controleMovimentacao.getExtratoMensalPorCliente(1L, 12, 2024));
		imprimirExtrato(controleMovimentacao.getExtratoMensalPorConta(1L, 12, 2024));
		imprimirExtrato(controleMovimentacao.getMovimentacoesPorDataTransacao(LocalDate.parse("2024-12-12")));
		
		controleMovimentacao.excluirPorCliente(1L);
	}
	
	public static void imprimirExtrato(List<Movimentacao> extratos) {
		System.out.println("-------------- Lista Extrato -------------");
		for (Movimentacao movimentacao : extratos) {
			System.out.println("Data Transação " + movimentacao.getDataTransacao());
			System.out.println("Id Conta " + movimentacao.getId());
			System.out.println("Valor Operação " + movimentacao.getValorOperacao());
			System.out.println("Tipo Transação " + movimentacao.getTipoTransacao());
			System.out.println("Descrição " + movimentacao.getDescricao());
			System.out.println("Cashback " + movimentacao.getCashback());
			System.out.println("-------------------------------");
		}
	}

}
