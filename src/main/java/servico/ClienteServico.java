package servico;

import java.time.LocalDate;
import java.util.List;

import dao.ClienteDAO;
import entidade.Cliente;
import util.Cpf;

public class ClienteServico implements InterfaceServico<Cliente> {
	ClienteDAO daoCliente = new ClienteDAO();
	ContaServico servicoConta = new ContaServico();
	MovimentacaoServico servicoMovimentacao = new MovimentacaoServico();

	@Override
	public Cliente inserir(Cliente cliente) {
		if(verificarCamposNaoNulos(cliente) && Cpf.verificarCpf(cliente.getCpf())) {
			Cliente clienteInsercao = daoCliente.buscarPorCpf(cliente.getCpf());
			if(clienteInsercao == null) {
				return daoCliente.inserir(cliente);
			}
			return null;
		} 
		return null;
	}
	
	@Override
	public Cliente alterar(Cliente cliente) {
		return daoCliente.alterar(cliente);
	}
	
	@Override
	public void excluir(Long idCliente) {
		if(daoCliente.buscarPorId(idCliente) != null) {
			servicoMovimentacao.excluirPorCliente(idCliente);
			servicoConta.excluirPorCliente(idCliente);
			daoCliente.excluir(idCliente);
		}
	}
	
	private static boolean verificarCamposNaoNulos(Cliente cliente) {
		return cliente.getCpf() != null && cliente.getNome() != null && cliente.getDataNascimento() != null;
	}
	
	@Override
	public Cliente buscarPorId(Long idCliente) {
		return daoCliente.buscarPorId(idCliente);
	}
	
	public Cliente buscarPorCpf(String cpf) {
		if(Cpf.verificarCpf(cpf)) {
			return daoCliente.buscarPorCpf(cpf);
		}
		return null;
	}
	
	@Override
	public List<Cliente> listarTodos() {
		return daoCliente.listarTodos();
	}
	
	public List<Cliente> listarPorPeriodoNascimento(LocalDate dataInicial, LocalDate dataFinal) {
		return daoCliente.listarPorPeriodoNascimento(dataInicial, dataFinal);
	}

}
