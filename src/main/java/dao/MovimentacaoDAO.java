package dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entidade.Movimentacao;
import entidade.TransacaoTipo;

public class MovimentacaoDAO extends GenericoDAO<Movimentacao> {

	public MovimentacaoDAO() {
		super(Movimentacao.class);
	}

	@Override
	public Movimentacao alterar(Movimentacao movimentacao) {
		EntityManager em = getEntityManager();
		Movimentacao MovimentacaoBanco = null;
		try {
			if(movimentacao.getId() != null) {
				em.getTransaction().begin();
				
				MovimentacaoBanco = em.find(Movimentacao.class, movimentacao.getId());
				if(MovimentacaoBanco != null) {
					MovimentacaoBanco.setDescricao(movimentacao.getDescricao());
					em.merge(MovimentacaoBanco);
				}
				em.getTransaction().commit();
			}
		}
		finally {
			em.close();
		}
		return MovimentacaoBanco;
	}
	
	public void excluirPorConta(Long idConta) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			
			Query query = em.createQuery("delete from Movimentacao m where m.conta.id = :idConta");
			query.setParameter("idConta", idConta);
			query.executeUpdate();
			
			em.getTransaction().commit();
		} 
		finally {
			em.close();
		}
	}
	
	public void excluirPorCliente(Long idCliente) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			
			Query query = em.createQuery("delete from Movimentacao m where m.conta.id in (select c.id from Conta c where c.cliente.id = :idCliente)");
			query.setParameter("idCliente", idCliente);
			query.executeUpdate();
			
			em.getTransaction().commit();
		}
		finally {
			em.close();
		}
	}
	
	public List<Movimentacao> listarPorTipoTransacao(TransacaoTipo tipoTransacao) {
		EntityManager em = getEntityManager(); 
		List<Movimentacao> resultado = new ArrayList<>();
		try {
			Query query = em.createQuery("from Movimentacao where tipoTransacao = :tipoTransacao");
			query.setParameter("tipoTransacao", tipoTransacao);
			
			resultado = query.getResultList();
		}
		finally {
			em.close();
		}
		return resultado; 
	}
	
	public List<Movimentacao> listarPorDataTransacao(LocalDate dataTransacao) {
		EntityManager em = getEntityManager();
		List<Movimentacao> resultado = new ArrayList<>();
		try {
			Date sqlDataTransacao = Date.valueOf(dataTransacao);
			
			Query query = em.createQuery("from Movimentacao m where function('DATE', dataTransacao) = :dataTransacao");
			query.setParameter("dataTransacao", sqlDataTransacao);
			
			resultado = query.getResultList();
		}
		finally {
			em.close();
		}
		return resultado;
	}

	public List<Movimentacao> listarPorCliente(Long idCliente) {
		EntityManager em = getEntityManager();
		List<Movimentacao> resultado = new ArrayList<>();
		try {
			Query query = em.createQuery("from Movimentacao m where m.conta.id in (select c.id from Conta c where c.cliente.id = :idCliente)");
			query.setParameter("idCliente", idCliente);
			
			resultado = query.getResultList();
		}
		finally {
			em.close();
		}
		return resultado;
	}
	
	public List<Movimentacao> listarPorConta(Long idConta) {
		EntityManager em = getEntityManager();
		List<Movimentacao> resultado = new ArrayList<>();
		try {
			Query query = em.createQuery("from Movimentacao m where m.conta.id = :idConta");
			query.setParameter("idConta", idConta);
			
			resultado = query.getResultList();
		}
		finally {
			em.close();
		}
		return resultado;
	}
	
	public List<Movimentacao> listarExtratoDiarioCliente(Long idCliente, LocalDate dataTransacao) {
		EntityManager em = getEntityManager();
		List<Movimentacao> resultado = new ArrayList<>();
		try {
			Date sqlDataTransacao = Date.valueOf(dataTransacao);
			
			Query query = em.createQuery("from Movimentacao m where function('DATE', m.dataTransacao) = :dataTransacao and m.conta.id in (select c.id from Conta c where c.cliente.id = :idCliente)");
			query.setParameter("idCliente", idCliente);
			query.setParameter("dataTransacao", dataTransacao);
			
			resultado = query.getResultList();
		} 
		finally {
			em.close();
		}
		return resultado;
	}
	
	public List<Movimentacao> listarExtratoDiarioConta(Long idConta, LocalDate dataTransacao) {
		EntityManager em = getEntityManager();
		List<Movimentacao> resultado = new ArrayList<>();
		try {
			Date sqlDataTransacao = Date.valueOf(dataTransacao);
			
			Query query = em.createQuery("from Movimentacao m where where function('DATE', m.dataTransacao) = :dataTransacao and m.conta.id = :idConta");
			query.setParameter("idConta", idConta);
			query.setParameter("dataTransacao", dataTransacao);
			
			resultado = query.getResultList();
		}
		finally {
			em.close();
		}
		return resultado;
	}
	
	public List<Movimentacao> listarExtratoMensalCliente(Long idCliente, int mes, int ano) {
		LocalDate dataInicial = LocalDate.of(ano, mes, 1);
		LocalDate dataFinal = dataInicial.withDayOfMonth(dataInicial.lengthOfMonth());
		return listarExtratoPeriodicoCliente(idCliente, dataInicial, dataFinal);
	}
	
	public List<Movimentacao> listarExtratoMensalConta(Long idConta, int mes, int ano) {
		LocalDate dataInicial = LocalDate.of(ano, mes, 1);
		LocalDate dataFinal = dataInicial.withDayOfMonth(dataInicial.lengthOfMonth());
		return listarExtratoPeriodicoConta(idConta, dataInicial, dataFinal);
	}
	
	public List<Movimentacao> listarExtratoPeriodicoCliente(Long idCliente, LocalDate dataInicial, LocalDate dataFinal) {
		EntityManager em = getEntityManager();
		List<Movimentacao> resultado = new ArrayList<>();
		try {
			Date sqlDataInicial = Date.valueOf(dataInicial);
			Date sqlDataFinal = Date.valueOf(dataFinal);
			
			Query query = em.createQuery("from Movimentacao m where function('DATE', m.dataTransacao) between :dataInicial and :dataFinal and m.conta.id in (select c.id from Conta c where c.cliente.id = :idCliente)");
			query.setParameter("idCliente", idCliente);
			query.setParameter("dataInicial", sqlDataInicial);
			query.setParameter("dataFinal", sqlDataFinal);
			
			resultado = query.getResultList();
		}
		finally {
			em.close();
		}
		return resultado;
	}
	
	public List<Movimentacao> listarExtratoPeriodicoConta(Long idConta, LocalDate dataInicial, LocalDate dataFinal) {
		EntityManager em = getEntityManager();
		List<Movimentacao> resultado = new ArrayList<>();
		try {
			Date sqlDataInicial = Date.valueOf(dataInicial);
			Date sqlDataFinal = Date.valueOf(dataFinal);
			
			Query query = em.createQuery("from Movimentacao m where m.conta.id = :idConta and function('DATE', m.dataTransacao) between :dataInicial and :dataFinal");
			query.setParameter("idConta", idConta);
			query.setParameter("dataInicial", sqlDataInicial);
			query.setParameter("dataFinal", sqlDataFinal);
			
			resultado = query.getResultList();
		}
		finally {
			em.close();
		}
		return resultado;
	}
	
	public List<Movimentacao> listarPeriodicoPorTipoTransacao(Long idConta, TransacaoTipo tipoTransacao, LocalDate dataInicial, LocalDate dataFinal) {
		EntityManager em = getEntityManager();
		List<Movimentacao> resultado = new ArrayList<>();
		try {
			Date sqlDataInicial = Date.valueOf(dataInicial);
			Date sqlDataFinal = Date.valueOf(dataFinal);
			
			Query query = em.createQuery("from Movimentacao m where m.tipoTransacao = :tipoTransacao and m.conta.id= :idConta and function('DATE', m.dataTransacao) between :dataInicial and :dataFinal");
			query.setParameter("idConta", idConta);
			query.setParameter("tipoTransacao", tipoTransacao);
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
