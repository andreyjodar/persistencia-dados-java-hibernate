package servico;

import java.time.LocalDate;
import java.util.List;

import dao.ClienteDAO;
import dao.ContaDAO;
import dao.MovimentacaoDAO;
import entidade.Cliente;
import util.Cpf;

public class ClienteServico {
	ClienteDAO daoCliente = new ClienteDAO();
	ContaServico servicoConta = new ContaServico();
	MovimentacaoServico servicoMovimentacao = new MovimentacaoServico();

	public Cliente inserirCliente(Cliente cliente) {
		if(verificarCamposNaoNulos(cliente) && Cpf.verificarCpf(cliente.getCpf())) {
			Cliente clienteInsercao = daoCliente.buscarPorCpf(cliente.getCpf());
			if(clienteInsercao == null) {
				clienteInsercao = daoCliente.inserirCliente(cliente);
				return clienteInsercao;
			}
			return null;
		} 
		return null;
	}
	
	public Cliente alterarCliente(Cliente cliente) {
		return daoCliente.alterarCliente(cliente);
	}
	
	public void excluirCliente(Long idCliente) {
		if(daoCliente.buscarPorId(idCliente) != null) {
			servicoMovimentacao.excluirPorCliente(idCliente);
			servicoConta.excluirPorCliente(idCliente);
			daoCliente.excluirCliente(idCliente);
		}
	}
	
	public static boolean verificarCamposNaoNulos(Cliente cliente) {
		return cliente.getCpf() != null && cliente.getNome() != null && cliente.getDataNascimento() != null;
	}
	
	public List<Cliente> listarTodosClientes() {
		return daoCliente.listarTodosClientes();
	}
	
	public List<Cliente> listarPorPeriodoNascimento(LocalDate dataInicial, LocalDate dataFinal){
		return daoCliente.listarPorPeriodoNascimento(dataInicial, dataFinal);
	}
	
	public Cliente buscarPorCpf(String cpf) {
		if(Cpf.verificarCpf(cpf)) {
			return daoCliente.buscarPorCpf(cpf);
		}
		return null;
	}
	
	public Cliente buscarPorId(Long idCliente) {
		return daoCliente.buscarPorId(idCliente);
	}
}
