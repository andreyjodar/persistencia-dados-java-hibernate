package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import entidade.Conta;

public class ContaDAO {
	
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");
	
	public Conta inserir(Conta conta) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(conta);
		em.getTransaction().commit();
		em.close();
		return conta;
	}
	
	public Conta alterar(Conta conta) {
		Conta contaBanco = null;
		if(conta.getId() != null && conta.getCliente() != null) {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			
			contaBanco = em.find(Conta.class, conta.getId());
			if(contaBanco != null) {
				contaBanco.setContaTipo(conta.getContaTipo());
				em.merge(contaBanco);
			}
			
			em.getTransaction().commit();
			em.close();
		}
		
		return contaBanco;
	}
	
	public void excluir(Long idConta) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Conta conta = em.find(Conta.class, idConta);
		if(conta != null) {
			em.remove(conta);
		}
		em.getTransaction().commit();
		em.close();
	}
}
