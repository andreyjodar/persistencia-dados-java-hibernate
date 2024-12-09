package dao;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entidade.Cliente;

public class ClienteDAO extends GenericoDAO<Cliente> {
	
	public ClienteDAO() {
		super(Cliente.class);
	}
	
	@Override
	public Cliente alterar(Cliente cliente) {
		EntityManager em = getEntityManager();
		Cliente clienteBanco = null;
		try {
			em.getTransaction().begin();
			
			clienteBanco = em.find(Cliente.class, cliente.getId());
			if(clienteBanco != null) {
				clienteBanco.setNome(cliente.getNome());
				em.merge(clienteBanco);
			}
			em.getTransaction().commit();
		}
		finally {
			em.close();
		}
		return clienteBanco;
	}
	
	public Cliente buscarPorCpf(String cpf) {
		EntityManager em = getEntityManager();
		try {
			Query query = em.createQuery("from Cliente where cpf = :cpf");
			query.setParameter("cpf", cpf);
			return (Cliente) query.getSingleResult();
		} 
		catch (NoResultException e) {
			return null;
		} 
		finally {
			em.close();
		}
	}
	
	public List<Cliente> listarPorPeriodoNascimento(LocalDate dataInicial, LocalDate dataFinal) {
		EntityManager em = getEntityManager();
		List<Cliente> resultado;
		try {
			Date sqlDataInicial = Date.valueOf(dataInicial);
			Date sqlDataFinal = Date.valueOf(dataFinal);
			
			Query query = em.createQuery("from Cliente where function('DATE', dataNascimento) between :dataInicial and :dataFinal");
			query.setParameter("dataInicial", sqlDataInicial);
			query.setParameter("dataFinal", sqlDataFinal);
			
			resultado = query.getResultList();
		} 
		finally {
			em.close();
		}
		return resultado;
	}
	
}
