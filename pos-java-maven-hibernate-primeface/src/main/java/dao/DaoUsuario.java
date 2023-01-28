package dao;

import model.UsuarioPessoa;

public class DaoUsuario<E> extends DaoGeneric<UsuarioPessoa> {
/*classe para deletar telefone que deve extender do DaoGeneric tendo a classe concreta de UsuarioPessoa*/
	public DaoUsuario() {
		// TODO Auto-generated constructor stub
	}
	
	public void removerUsuario(UsuarioPessoa pessoa) throws Exception {
		getEntityManager().getTransaction().begin();//inicia a transação
		String sqlDeleteFone = "delete from telefoneuser where usuariopessoa_id = " + pessoa.getId();
		getEntityManager().createNativeQuery(sqlDeleteFone).executeUpdate();
		getEntityManager().getTransaction().commit();
		
		super.deletarPoId(pessoa);
	}
	
}
