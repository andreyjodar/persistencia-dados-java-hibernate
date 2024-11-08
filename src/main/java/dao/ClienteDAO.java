package dao;

import javax.persistence.*;

import entidade.Cliente;

public class ClienteDAO {
	
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");
	
	public Cliente inserir(Cliente cliente) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(cliente);
		em.getTransaction().commit();
		em.close();
		return cliente;
	}
	
	public Cliente alterar(Cliente cliente) {
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
	
	public void excluir(Long idCliente) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Cliente clienteBanco = em.find(Cliente.class, idCliente);
		if(clienteBanco != null) {
			em.remove(clienteBanco);
		}
		em.getTransaction().commit();
		em.close();
		
	}
	
}
