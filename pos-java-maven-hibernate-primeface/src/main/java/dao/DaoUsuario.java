package dao;

import model.UsuarioPessoa;

public class DaoUsuario<E> extends DaoGeneric<UsuarioPessoa> {
/*classe para deletar telefone que deve extender do DaoGeneric tendo a classe concreta de UsuarioPessoa*/
	public DaoUsuario() {
		// TODO Auto-generated constructor stub
	}
	/* metodo sem usar cascata
	public void removerUsuario(UsuarioPessoa pessoa) throws Exception {
		getEntityManager().getTransaction().begin();//inicia a transação
		
		String sqlDeleteFone = "delete from telefoneuser where usuariopessoa_id = " + pessoa.getId();
		getEntityManager().createNativeQuery(sqlDeleteFone).executeUpdate();
		
		String sqlDeleteEmail = "delete from emailuser where usuariopessoa_id = " + pessoa.getId();
		getEntityManager().createNativeQuery(sqlDeleteEmail).executeUpdate();		
		
		getEntityManager().getTransaction().commit();
		
		super.deletarPoId(pessoa);
	}*/
	
	//metodo com cascata
	public void removerUsuario(UsuarioPessoa pessoa) throws Exception {
		getEntityManager().getTransaction().begin();//inicia a transação
		
		getEntityManager().remove(pessoa);
		
		getEntityManager().getTransaction().commit();
		
	}
	
}
