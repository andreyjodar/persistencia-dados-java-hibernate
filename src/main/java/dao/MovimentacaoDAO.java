package dao;

import java.time.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entidade.Movimentacao;

public class MovimentacaoDAO {

	EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");

	public Movimentacao inserir(Movimentacao conta) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(conta);
		em.getTransaction().commit();
		em.close();
		return conta;
	}

	public Movimentacao alterar(Movimentacao conta) {
		Movimentacao contaBanco = null;
		if (conta.getId() != null) {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();

			contaBanco = buscarPorId(conta.getId());

			if (contaBanco != null) {
				contaBanco.setDescricao(conta.getDescricao());
				em.merge(contaBanco);
			}

			em.getTransaction().commit();
			em.close();
		}
		return contaBanco;
	}

	public void excluir(Long id) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Movimentacao conta = em.find(Movimentacao.class, id);
		if (conta != null) {
			em.remove(conta);
		}
		em.getTransaction().commit();
		em.close();
	}

	public List<Movimentacao> listarTodos() {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Movimentacao");
		List<Movimentacao> resultado = query.getResultList();
		em.close();
		return resultado;
	}
	
	public List<Movimentacao> listarPorTipoTransacao(String tipoTransacao) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Movimentacao where tipoTransacao='" + tipoTransacao + "'");
		List<Movimentacao> resultado = query.getResultList();
		em.close();
		return resultado; 
	}

	public List<Movimentacao> listarPorCpf(String cpf) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Movimentacao where cpfCorrentista='" + cpf + "'");
		List<Movimentacao> resultado = query.getResultList();
		em.close();
		return resultado;
	}
	
	public List<Movimentacao> listarPorDataTransacao(LocalDate dataTransacao) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Movimentacao where FUNCTION('DATE', dataTransacao) = FUNCTION('DATE', " + dataTransacao + ")");
		List<Movimentacao> resultado = query.getResultList();
		em.close();
		return resultado;
	}
	
	public List<Movimentacao> listarPorCpfEDataTransacao(String cpf, LocalDate dataTransacao) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Movimentacao where FUNCTION('DATE', dataTransacao) = " + dataTransacao + " AND cpfCorrentista ='" + cpf + "'");
		List<Movimentacao> resultado = query.getResultList();
		em.close();
		return resultado;
	}
	
	public List<Movimentacao> listarExtratoPeriodico(String cpfCorrentista, LocalDate dataInicial, LocalDate dataFinal) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Movimentacao where cpfCorrentista = " + cpfCorrentista + " AND FUNCTION('DATE', dataTransacao) >= " + dataInicial + " AND FUNCTION('DATE', dataTransacao) <= "+ dataFinal);
		List<Movimentacao> resultado = query.getResultList();
		em.close();
		return resultado;
	}
	
	public List<Movimentacao> listarExtratoMensal(String cpfCorrentista, int mes, int ano) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("FROM Movimentacao WHERE cpfCorrentista = " + cpfCorrentista + " AND FUNCTION('MONTH', dataTransacao) = " + mes + " AND FUNCTION('YEAR', dataTransacao) = " + ano);
		List<Movimentacao> resultado = query.getResultList();
		em.close();
		return resultado;
	}

	public Movimentacao buscarPorId(Long id) {
		EntityManager em = emf.createEntityManager();
		Movimentacao conta = em.find(Movimentacao.class, id);
		em.close();
		return conta;
	}
}
