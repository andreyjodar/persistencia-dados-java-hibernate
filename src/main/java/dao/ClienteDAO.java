package dao;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import entidade.Cliente;

public class ClienteDAO {
	
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");
	
	public Cliente inserirCliente(Cliente cliente) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		em.persist(cliente);
		
		em.getTransaction().commit();
		em.close();
		return cliente;
	}
	
	public Cliente alterarCliente(Cliente cliente) {
		Cliente clienteBanco = null;
		if(cliente.getId() != null) {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			
			clienteBanco = em.find(Cliente.class, cliente.getId());
			
			if(clienteBanco != null) {
				cliente.setNome(cliente.getNome());
				em.merge(clienteBanco);
			}
			
			em.getTransaction().commit();
			em.close();
		}
		
		return clienteBanco;
	}
	
	public void excluirCliente(Long idCliente) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Cliente clienteBanco = em.find(Cliente.class, idCliente);
		if(clienteBanco != null) {
			em.remove(clienteBanco);
		}
		
		em.getTransaction().commit();
		em.close();
		
	}
	
	public List<Cliente> listarTodosClientes() {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Cliente");
		
		List<Cliente> resultado = query.getResultList();
		em.close();
		return resultado;
	}
	
	public Cliente buscarPorCpf(String cpf) {
		EntityManager em = emf.createEntityManager();
		try {
			Query query = em.createQuery("from Cliente where cpf = :cpf");
			query.setParameter("cpf", cpf);
			return (Cliente) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}
	
	public Cliente buscarPorId(Long idCliente) {
		EntityManager em = emf.createEntityManager();
		Cliente cliente = em.find(Cliente.class, idCliente);
		em.close();
		return cliente;
	}
	
	public List<Cliente> listarPorPeriodoNascimento(LocalDate dataInicial, LocalDate dataFinal) {
		EntityManager em = emf.createEntityManager();
		Date sqlDataInicial = Date.valueOf(dataInicial);
		Date sqlDataFinal = Date.valueOf(dataFinal);
		
		Query query = em.createQuery("from Cliente where function('DATE', dataNascimento) between :dataInicial and :dataFinal");
		query.setParameter("dataInicial", sqlDataInicial);
		query.setParameter("dataFinal", sqlDataFinal);
		
		List<Cliente> resultado = query.getResultList();
		em.close();
		return resultado;
	}
	
}
