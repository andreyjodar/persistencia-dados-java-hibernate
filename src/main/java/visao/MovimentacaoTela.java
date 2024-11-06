package visao;

import java.time.*;


import controle.MovimentacaoControle;
import entidade.Movimentacao;

public class MovimentacaoTela {

	public static void main(String[] args) {
		MovimentacaoControle controle = new MovimentacaoControle();
		
		Movimentacao conta = new Movimentacao();
		conta.setCpfCorrentista("04425225112");
		conta.setDescricao("Depósito de 500,00 no dia 03/10/24");
		conta.setNomeCorrentista("José");
		conta.setTipoTransacao("Depósito");
		conta.setValorOperacao(500.);
		
		controle.inserir(conta);
	}

}
