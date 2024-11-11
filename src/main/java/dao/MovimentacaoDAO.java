package dao;

import java.util.List;

import java.time.*;
import javax.persistence.*;

import entidade.Conta;
import entidade.Movimentacao;

public class MovimentacaoDAO {

	EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");
	ContaDAO daoConta = new ContaDAO();

	public Movimentacao inserirMovimentacao(Movimentacao movimentacao) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(movimentacao);
		em.getTransaction().commit();
		em.close();
		return movimentacao;
	}

	public Movimentacao alterarMovimentacao(Movimentacao conta) {
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

	public void excluirMovimentacao(Long idMovimentacao) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Movimentacao movimentacao = em.find(Movimentacao.class, idMovimentacao);
		if (movimentacao != null) {
			em.remove(movimentacao);
		}
		em.getTransaction().commit();
		em.close();
	}
	
	public void excluirPorConta(Long idConta) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Query query = em.createQuery("delete from Movimentacao m where m.conta.id = " + idConta);
		query.executeUpdate();
		em.getTransaction().commit();
		em.close();
	}
	
	public void excluirPorCliente(Long idCliente) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Query query = em.createQuery("delete from Movimentacao m where m.conta.id in (select c.id from Conta c where c.cliente.id = " + idCliente + ")");
		query.executeUpdate();
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

	public List<Movimentacao> listarPorCliente(Long idCliente) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Movimentacao m where m.conta.cliente.id=" + idCliente);
		List<Movimentacao> resultado = query.getResultList();
		em.close();
		return resultado;
	}
	
	public List<Movimentacao> listarPorConta(Long idConta) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Movimentacao m where m.conta.id = " + idConta);
		List<Movimentacao> resultado = query.getResultList();
		em.close();
		return resultado;
	}
	
	public List<Movimentacao> listarPorDataTransacao(LocalDate dataTransacao) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Movimentacao where FUNCTION('DATE', dataTransacao) = " + dataTransacao);
		List<Movimentacao> resultado = query.getResultList();
		em.close();
		return resultado;
	}
	
	public List<Movimentacao> listarExtratoDiarioCliente(Long idCliente, LocalDate dataTransacao) {
		return listarExtratoPeriodicoCliente(idCliente, dataTransacao, dataTransacao);
	}
	
	public List<Movimentacao> listarExtratoDiarioConta(Long idConta, LocalDate dataTransacao) {
		return listarExtratoPeriodicoConta(idConta, dataTransacao, dataTransacao);
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
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Movimentacao m where function('DATE', m.dataTransacao) between " + dataInicial + " and " + dataFinal + " and m.conta.id in (select c.id from Conta c where c.cliente.id =" + idCliente +")");
		List<Movimentacao> resultado = query.getResultList();
		em.close();
		return resultado;
	}
	
	public List<Movimentacao> listarExtratoPeriodicoConta(Long idConta, LocalDate dataInicial, LocalDate dataFinal) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Movimentacao m where m.conta.id=" + idConta + " and function('DATE', m.dataTransacao) between "+ dataInicial + "and " + dataFinal);
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
