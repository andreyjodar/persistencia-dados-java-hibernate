package controle;

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
	

}
