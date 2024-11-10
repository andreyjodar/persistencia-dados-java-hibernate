package dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entidade.Conta;
import entidade.ContaTipo;

public class ContaDAO {
	
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");
	
	public Conta inserirConta(Conta conta) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(conta);
		em.getTransaction().commit();
		em.close();
		return conta;
	}
	
	public Conta alterarConta(Conta conta) {
		Conta contaBanco = null;
		if(conta.getId() != null) {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			
			contaBanco = em.find(Conta.class, conta.getId());
			if(contaBanco != null) {
				contaBanco.setContaTipo(conta.getContaTipo());
				contaBanco.setSaldo(conta.getSaldo());
				em.merge(contaBanco);
			}
			
			em.getTransaction().commit();
			em.close();
		}
		
		return contaBanco;
	}
	
	public void excluirConta(Long idConta) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Conta conta = em.find(Conta.class, idConta);
		if(conta != null) {
			em.remove(conta);
		}
		em.getTransaction().commit();
		em.close();
	}
	
	public void excluirPorCliente(Long idCliente) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Query query = em.createQuery("delete from Conta where cliente.id = " + idCliente);
		query.executeUpdate();
		em.getTransaction().commit();
		em.close();
	}
	
	public List<Conta> listarTodasContas() {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Conta");
		List<Conta> resultado = query.getResultList();
		em.close();
		return resultado;
	}
	
	public List<Conta> listarPorIdCliente(Long idCliente) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Conta where cliente.id = " + idCliente);
		List<Conta> resultado = query.getResultList();
		em.close();
		return resultado;
	}
	
	public List<Conta> listarPorContaTipo(ContaTipo contaTipo){
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Conta where contaTipo = " + contaTipo);
		List<Conta> resultado = query.getResultList();
		em.close();
		return resultado;
	}
	
	public List<Conta> listarPorPeriodoCriacao(LocalDate dataInicial, LocalDate dataFinal) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Conta where FUNCTION('DATE', dataAbertura) >= " + dataInicial + " AND FUNCTION('DATE', dataAbertura) <= " + dataFinal);
		List<Conta> resultado = query.getResultList();
		em.close();
		return resultado;
	}
	
	public Conta buscarPorId(Long idConta) {
		EntityManager em = emf.createEntityManager();
		Conta conta = em.find(Conta.class, idConta);
		em.close();
		return conta;
	}
	
}
