package controle;

import java.time.LocalDate;
import java.util.List;

import entidade.Cliente;
import servico.ClienteServico;

public class ClienteControle {

	ClienteServico servicoCliente = new ClienteServico();
	
	public Cliente inserir(Cliente cliente) {
		return servicoCliente.inserirCliente(cliente);
	}
	
	public Cliente alterarCliente(Cliente cliente) {
		return servicoCliente.alterarCliente(cliente);
	}
	
	public void excluirCliente(Long idCliente) {
		servicoCliente.excluirCliente(idCliente);
	}
	
	public List<Cliente> getTodosClientes() {
		return servicoCliente.listarTodosClientes();
	}
	
	public List<Cliente> getClientesPorIntervaloNascimento(LocalDate dataInicial, LocalDate dataFinal){
		return servicoCliente.listarPorPeriodoNascimento(dataInicial, dataFinal);
	}
	
	public Cliente getClientePorCpf(String cpf) {
		return servicoCliente.buscarPorCpf(cpf);
	}
}
