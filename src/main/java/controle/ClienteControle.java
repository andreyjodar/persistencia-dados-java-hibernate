package controle;

import java.time.LocalDate;
import java.util.List;

import entidade.Cliente;
import servico.ClienteServico;

public class ClienteControle {

	ClienteServico servicoCliente = new ClienteServico();
	
	public Cliente inserir(Cliente cliente) {
		return servicoCliente.inserir(cliente);
	}
	
	public Cliente alterar(Cliente cliente) {
		return servicoCliente.alterar(cliente);
	}
	
	public void excluir(Long idCliente) {
		servicoCliente.excluir(idCliente);
	}
	
	public Cliente getClientePorId(Long idCliente) {
		return servicoCliente.buscarPorId(idCliente);
	}
	
	public Cliente getClientePorCpf(String cpf) {
		return servicoCliente.buscarPorCpf(cpf);
	}
	
	public List<Cliente> getTodosClientes() {
		return servicoCliente.listarTodos();
	}
	
	public List<Cliente> getClientesPorIntervaloNascimento(LocalDate dataInicial, LocalDate dataFinal) {
		return servicoCliente.listarPorPeriodoNascimento(dataInicial, dataFinal);
	}

}
