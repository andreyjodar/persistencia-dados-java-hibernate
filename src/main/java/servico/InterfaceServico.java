package servico;

import java.util.List;

public interface InterfaceServico<T> {
	T inserir(T objeto);
	T alterar(T objeto);
	void excluir(Long id);
	T buscarPorId(Long id);
	List<T> listarTodos();
}
