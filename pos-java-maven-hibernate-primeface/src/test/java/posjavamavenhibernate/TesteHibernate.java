package posjavamavenhibernate;

import java.util.List;

import org.junit.Test;

import dao.DaoGeneric;
import model.TelefoneUser;
import model.UsuarioPessoa;

public class TesteHibernate {

	//para testar se esta lendo o arquivo
	@Test
	public void testeHibernateUtil() {
		
		//HibernateUtil.getEntityManager();	teste de criação e update do banco
		
		//teste de inserção de dados
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>(); //instancia a classe
		UsuarioPessoa pessoa = new UsuarioPessoa(); //instancia a classe usuario
		  
		 //inser os dados da classe
		 pessoa.setNome("José");
		 pessoa.setSobrenome("arruda");
		 pessoa.setEmail("pc@gmail.com");
		 pessoa.setIdade(33);
		 pessoa.setLogin("teste");
		 pessoa.setSenha("1234");
		 
		 daoGeneric.salvar(pessoa); //salva os dados		
	}
	
	//teste de id
	@Test
	public void testeBuscar() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>(); 
		UsuarioPessoa pessoa = new UsuarioPessoa(); 
		pessoa.setId(2L);
		
		pessoa = daoGeneric.pesquisar(pessoa);
		
		System.out.println(pessoa); //mostrar natel		
	}
	
	//teste de id passando o id
		@Test
		public void testeBuscar2() {
			DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>(); 
						
			UsuarioPessoa pessoa = daoGeneric.pesquisar(1L,UsuarioPessoa.class);
			
			System.out.println(pessoa); //mostrar natel		
		}
		
		//delete
	/*	@Test
		public void testeDelete() {
			DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>(); 
						
			UsuarioPessoa pessoa = daoGeneric.pesquisar(6L,UsuarioPessoa.class);
			
			daoGeneric.deletarPoId(pessoa);
					
		}metodo modificado*/
		
		//teste de update
		@Test
		public void testeUpdate() {
			DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>(); 
						
			UsuarioPessoa pessoa = daoGeneric.pesquisar(1L,UsuarioPessoa.class);
			
			//fazendo atualização dos dados
			pessoa.setIdade(99);
			pessoa.setNome("nome atualizado hobernate");
			pessoa.setSenha("7896");
			pessoa = daoGeneric.updateMerge(pessoa);
			
			
			System.out.println(pessoa); //mostrar natel		
		}
		
		//consulta no Bd
		@Test
		public void testeConsultar() {
			DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>(); 
						
			//instanciando lista
			List<UsuarioPessoa> list = daoGeneric.listar(UsuarioPessoa.class);
			
			for (UsuarioPessoa usuarioPessoa : list) {
				System.out.println(usuarioPessoa);
				System.out.println("----------------------------------------");
						
			}
		}
		
		//teste de HQL
		@Test
		public void testeQuerylist() {
			
			DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
			//iniciando uma lista
			List<UsuarioPessoa> list = daoGeneric.getEntityManager()
					  .createQuery(" from UsuarioPessoa where  nome = 'Julio'").getResultList(); /*retorna toda a lista
					   												para retorna um especifico usar where*/
				for (UsuarioPessoa usuarioPessoa : list) {
				
				System.out.println(usuarioPessoa);
			}				
		}
		
	//teste do sql q chama lista com quantidade definida
		@Test
		public void testeQuerylistMaxReslt() {
			
			DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
			//iniciando uma lista
			@SuppressWarnings("unchecked")
			List<UsuarioPessoa> list = daoGeneric.getEntityManager()
					  .createQuery(" from UsuarioPessoa order by id")
					  .setMaxResults(2).getResultList(); /*retorna toda a lista
					   									 para retorna um especifico usar where, ex: createQuery(" from UsuarioPessoa where  nome = 'Julio'")*/
				for (UsuarioPessoa usuarioPessoa : list) {
				
				System.out.println(usuarioPessoa);
			}				
		}
		
		@Test
		public void testeQueryListParameter() {
			
			DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
			//iniciando uma lista
			@SuppressWarnings("unchecked")
			List<UsuarioPessoa> list = daoGeneric.getEntityManager()
				.createQuery("from UsuarioPessoa where nome = :nome or sobrenome = :sobrenome")
				.setParameter("nome", "José").setParameter("sobrenome", "Alfi").getResultList();
			for (UsuarioPessoa usuarioPessoa : list) {
				System.out.println(usuarioPessoa);
			}	
		}
		
	//soma das idades
		@Test
		public void testeQuerySomaIdade() {
			DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
			
			Long SomaIdade = (Long) daoGeneric.getEntityManager()
					.createQuery("select sum(u.idade)from UsuarioPessoa u").getSingleResult();
			System.out.println("Soma das idades  --> "+ SomaIdade);
		}
		
	//teste de namedQuery, todo os usuarios
		@Test
		public void testeNamedQuery1() {
			DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
			
			@SuppressWarnings("unchecked")
			List<UsuarioPessoa> list = daoGeneric.getEntityManager().
					createNamedQuery("UsuarioPessoa.todos").getResultList();
			
			for (UsuarioPessoa usuarioPessoa : list) {
				
				System.out.println(usuarioPessoa);
			}
			
		}
		
		//teste de namedQuery, busca por nome
		@Test
		public void testeNamedQuery2() {
			DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
			
			@SuppressWarnings("unchecked")
			List<UsuarioPessoa> list = daoGeneric.getEntityManager().
					createNamedQuery("UsuarioPessoa.buscaPorNome")
					.setParameter("nome", "José").getResultList();
			
			for (UsuarioPessoa usuarioPessoa : list) {
				
				System.out.println(usuarioPessoa);
			}					
		}
				
//teste de salvar telefone
	@Test
	public void testeGravarTelefone() {
		DaoGeneric  daoGeneric = new DaoGeneric(); //generico
		
		UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar(7L , UsuarioPessoa.class);
		
		TelefoneUser telefoneUser = new TelefoneUser();
		
		telefoneUser.setTipo("casa");
		telefoneUser.setNumero("31-3595-7694");
		telefoneUser.setUsuarioPessoa(pessoa);
		
		daoGeneric.salvar(telefoneUser);
	}
	
	@Test
	public void testeConsultaTelefones() {
		DaoGeneric  daoGeneric = new DaoGeneric(); //generico
		
		UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar(7L , UsuarioPessoa.class);
		
		for (TelefoneUser fone : pessoa.getTelefoneUsers()) {
			System.out.println(fone.getNumero());
			System.out.println(fone.getTipo());
			System.out.println(fone.getUsuarioPessoa().getNome());
			System.out.println("----------------------------------------");
		}
	}
		
		
	
}//fim TesteHibernate
