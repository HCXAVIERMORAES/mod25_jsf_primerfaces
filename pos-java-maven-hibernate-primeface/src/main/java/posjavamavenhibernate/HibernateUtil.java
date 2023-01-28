package posjavamavenhibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class HibernateUtil {
	//padrão singtonw e statico
	public static EntityManagerFactory factory = null;
	
	//chamar o metodo
	static {
		init();
	}
	
	//metodo statico inicial
	private static void init() {
			
		try {
			//vverificação para ler apenas uma  vez
			if(factory == null) {
				factory = Persistence.createEntityManagerFactory("pos-java-maven-hibernate-primeface");//name do persistence-unit
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//fim metodo init
	
	//retorno
	public static EntityManager getEntityManager() {
		return factory.createEntityManager();/*prové a parte de persistencia*/
	}
	
	//metodo que retorna a primery key
	public static Object getpPrimaryKey(Object entit) { //retorna a primary key
		
		return factory.getPersistenceUnitUtil().getIdentifier(entit);		
	}
	
	
}//fim HibernateUtil
