package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class GenericoDAO<T> implements InterfaceDAO<T> {
	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");
	private final Class<T> classeEntidade;
	
	public GenericoDAO(Class<T> classe) {
		this.classeEntidade = classe;
	}
	
	public T inserir(T objeto) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(objeto);
			em.getTransaction().commit();
			return objeto;
		}
		finally {
			em.close();
		}
	}
	
	public T alterar(T objeto) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(objeto);
			em.getTransaction().commit();
			return objeto;
		}
		finally {
			em.close();
		}
	}
	
	public void excluir(Long id) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			T objeto = em.find(classeEntidade, id);
			if(objeto != null) {
				em.remove(objeto);
			}
			em.getTransaction().commit();
		}
		finally {
			em.close();
		}
	}
	
	public List<T> listarTodos() {
		EntityManager em = getEntityManager();
		try {
			List<T> objetos = em.createQuery("from T").getResultList();
			return objetos;
		}
		finally {
			em.close();
		}
	}
	
	public T buscarPorId(Long id) {
		EntityManager em = getEntityManager();
		try {
			T objeto = em.find(classeEntidade, id);
			return objeto;
		}
		finally {
		em.close();
		}
	}
	
	protected EntityManager getEntityManager() {
		return emf.createEntityManager();
	}
}
