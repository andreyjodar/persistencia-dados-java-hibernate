package dao;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entidade.Conta;
import entidade.ContaTipo;

public class ContaDAO extends GenericoDAO<Conta> {
	
	public ContaDAO() {
		super(Conta.class);
	}
	
	@Override
	public Conta alterar(Conta conta) {
		EntityManager em = getEntityManager();
		Conta contaBanco = null;
		try {
			em.getTransaction().begin();
			
			contaBanco = em.find(Conta.class, conta.getId());
			if(contaBanco != null) {
				contaBanco.setContaTipo(conta.getContaTipo());
				em.merge(contaBanco);
			}
			em.getTransaction().commit();
		}
		finally {
			em.close();
		}
		return contaBanco;
	}
	
	public void excluirPorCliente(Long idCliente) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			
			Query query = em.createQuery("delete from Conta where cliente.id = :idCliente");
			query.setParameter("idCliente", idCliente);
			
			query.executeUpdate();
			em.getTransaction().commit();
		}
		finally {
			em.close();
		}
	}
	
	public List<Conta> listarPorCliente(Long idCliente) {
		EntityManager em = getEntityManager();
		List<Conta> resultado = new ArrayList<>();
		try {
			Query query = em.createQuery("from Conta where cliente.id = :idCliente");
			query.setParameter("idCliente", idCliente);
			resultado = query.getResultList();
		}
		finally {
			em.close();
		}
		return resultado;
	}
	
	public List<Conta> listarPorContaTipo(ContaTipo contaTipo){
		EntityManager em = getEntityManager();
		List<Conta> resultado = new ArrayList<>();
		try {
			Query query = em.createQuery("from Conta where contaTipo = :contaTipo");
			query.setParameter("contaTipo", contaTipo);
			
			resultado = query.getResultList();
		}
		finally {
			em.close();
		}
		return resultado;
	}
	
	public List<Conta> listarPorPeriodoCriacao(LocalDate dataInicial, LocalDate dataFinal) {
		EntityManager em = getEntityManager();
		List<Conta> resultado = new ArrayList<>();
		try {
			Date sqlDataInicial = Date.valueOf(dataInicial);
			Date sqlDataFinal = Date.valueOf(dataFinal);
			
			Query query = em.createQuery("from Conta where function('DATE', dataAbertura) between :dataInicial and :dataFinal");
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
