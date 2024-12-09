package dao;

import java.util.List;

public interface InterfaceDAO<T> {
	public T inserir(T objeto);
	public T alterar(T objeto);
	public void excluir(Long id);
	public T buscarPorId(Long id);
	public List<T> listarTodos();
}
