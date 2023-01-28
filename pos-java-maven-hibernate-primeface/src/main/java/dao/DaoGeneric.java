package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import posjavamavenhibernate.HibernateUtil;

public class DaoGeneric<E> {
	
	/*O <E> na classe principal é um padrao java insserido manualmente
	 * */
	private EntityManager entityManager = HibernateUtil.getEntityManager();
	
	//método de salvar, ou persistir,  de modo generico
	public void salvar(E entidade) {
		
		EntityTransaction transaction = entityManager.getTransaction(); //iniciar uma transação 
		transaction.begin();
		entityManager.persist(entidade); //persistir no hibernate
		transaction.commit(); //salvar  no banco		
	}
	
	//metodo de update
	public E updateMerge(E entidade) { //salva ou atualiza
		
		EntityTransaction transaction = entityManager.getTransaction(); 
		transaction.begin();
		E entidadeSalva = entityManager.merge(entidade);
		transaction.commit();
		
		return entidadeSalva;
	}
	
	//metodo que retorna a entidade com  id
	@SuppressWarnings("unchecked")
	public E pesquisar(E entidade) {
		
		Object id = HibernateUtil.getpPrimaryKey(entidade);
		
		E e = (E) entityManager.find(entidade.getClass(), id);
		
		return e;		
	}
	
	//2ª metodo de pesquisa passando o id
	//metodo que retorna a entidade com  id
		@SuppressWarnings("unchecked")
		public E pesquisar(Long id ,Class<E> entidade) {			
			//E e = (E) entityManager.find(entidade, id); tela ñ carrega, logo, mudar o metodo de pesquisa
			entityManager.clear(); //limpar memoria
			E e = (E) entityManager.createQuery("from "+ entidade.getSimpleName() + " where id= "+id).
					getSingleResult();
			return e;		
		}
		
	//metodo de exclusão
		public void deletarPoId(E entidade) throws Exception {
			
			Object id = HibernateUtil.getpPrimaryKey(entidade); //obtem o ID do objeto pk
			
			EntityTransaction transaction = entityManager.getTransaction();//objeto de transação
			transaction.begin(); //começa uma transaçao no banco de dados
			//passando um sql para evitar alguns possiveis erros
			entityManager.createNativeQuery("delete from "+ entidade.getClass(). //monta a query para delete
					getSimpleName().toLowerCase() + " where id = " + id).executeUpdate(); //faz o delete
			transaction.commit(); //salva no BD			
		}
		
 //metodo de consulta todos
		@SuppressWarnings("unchecked")
		public List<E> listar(Class<E> entidade) {
			
			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin(); 
			
			List<E> lista = entityManager.createQuery("from "+ entidade.getName()).getResultList();
			transaction.commit();
			
			return lista;			
		}
		
	//criando um metodo para chamar o EntityManagerr de qualquer lugar do pprojeto
		public EntityManager getEntityManager() {
			return entityManager;
		}
	

}//fim daoGeneric
