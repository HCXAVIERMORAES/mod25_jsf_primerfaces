package managedBean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import dao.DaoTelefones;
import dao.DaoUsuario;
import model.TelefoneUser;
import model.UsuarioPessoa;



@ManagedBean(name = "telefoneManageBean")
@ViewScoped
public class TelefoneManageBean {

	private UsuarioPessoa user = new UsuarioPessoa();
	private DaoUsuario<UsuarioPessoa> daoUser = new DaoUsuario<UsuarioPessoa>();
	private DaoTelefones<TelefoneUser> daoTelefone = new DaoTelefones<TelefoneUser>();
	
	private TelefoneUser telefone = new TelefoneUser();
		
	
	/*vaipegar o parametrro da tela para passar para o telefone */
	@PostConstruct
	public void init() {
		//pegando parametro da tela
		String coduser = FacesContext.getCurrentInstance().getExternalContext().
				getRequestParameterMap().get("codigouser");
		user = daoUser.pesquisar(Long.parseLong(coduser), UsuarioPessoa.class);
		
	}
	
	//método de salvar telefones
	public String salvar() {
		telefone.setUsuarioPessoa(user);
		daoTelefone.salvar(telefone);
		telefone = new TelefoneUser();
		user = daoUser.pesquisar(user.getId(), UsuarioPessoa.class);//recarregar a tela apos salvar o tel.
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", " Telefone Salvo com sucesso!"));
		
		return "";
	}
	
	//método para remover telefone
	public String removeTelefone() throws Exception {
		daoTelefone.deletarPoId(telefone);
		user = daoUser.pesquisar(user.getId(), UsuarioPessoa.class);
		telefone = new TelefoneUser();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", " Telefone Removido com sucesso!"));
		
		return "";
	}
	
	//get e set
	
	public void setTelefone(TelefoneUser telefone) {
		this.telefone = telefone;
	}
	
	public TelefoneUser getTelefone() {
		return telefone;
	}
	
	public void setUser(UsuarioPessoa user) {
		this.user = user;
	}
	
	public UsuarioPessoa getUser() {
		return user;
	}
	
	public void setDaoTelefone(DaoTelefones<TelefoneUser> daoTelefone) {
		this.daoTelefone = daoTelefone;
	}
	
	public DaoTelefones<TelefoneUser> getDaoTelefone() {
		return daoTelefone;
	}

}
