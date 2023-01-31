package managedBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import com.google.gson.Gson;

import dao.DaoUsuario;
import model.UsuarioPessoa;

/*@ManagedBean -> indica que é uma classe controller, dar um nome, mesmo da classe começando em minusculo
 * @ViewScoped -> scopo da tela, segura os dados enqunato a tela estiver ativa 
 * */

@ManagedBean(name = "usuarioPessoaManagedBean")
@ViewScoped
public class UsuarioPessoaManagedBean {

	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	//	private DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
	private List<UsuarioPessoa> list = new ArrayList<UsuarioPessoa>(); // receber os dados salvos
	private DaoUsuario<UsuarioPessoa> daoGeneric = new DaoUsuario<UsuarioPessoa>(); //para deletar telefone
	
	//apos o managedBean ser construido na memoria esse metodo é chamado apenas uma vez
	@PostConstruct
	public void init() {
		list = daoGeneric.listar(UsuarioPessoa.class);
	}
	
	//méetodo de pesquisar cep pelo ajax
	public void pesquisaCep(AjaxBehaviorEvent event) {
		
		try { //System.out.println("cep digitado: "+usuarioPessoa.getCep());teste
			URL url = new URL("https://viacep.com.br/ws/"+usuarioPessoa.getCep()+"/json/"); //url do via cep
			URLConnection connection = url.openConnection(); //inicia a conexão
			InputStream is =connection.getInputStream();//abre conexão
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8")); //pega o  retorno
			
			String cep = ""; //variavel auxiliar 
			StringBuilder  jsonCep = new StringBuilder(); //concatena a string
			
			while((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}
			
			//objeto auxiliar  
			UsuarioPessoa userCepPessoa = new Gson().fromJson(jsonCep.toString(), UsuarioPessoa.class);
			
			usuarioPessoa.setCep(userCepPessoa.getCep());
			usuarioPessoa.setLogradouro(userCepPessoa.getLogradouro());
			usuarioPessoa.setComplemento(userCepPessoa.getComplemento());
			usuarioPessoa.setBairro(userCepPessoa.getBairro());
			usuarioPessoa.setLocalidade(userCepPessoa.getLocalidade());
			usuarioPessoa.setUf(userCepPessoa.getUf());
			usuarioPessoa.setIbge(userCepPessoa.getIbge());
			usuarioPessoa.setGia(userCepPessoa.getGia());
			usuarioPessoa.setDdd(userCepPessoa.getDdd());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	// set e get
	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}

	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}

	// é necessario ter o get da lista
	public List<UsuarioPessoa> getList() {
		//list = daoGeneric.listar(UsuarioPessoa.class); // inserido manualmente, para carregar a lista de dados
		return list;
	}

//método salvar - obs: é bom sempre retornar uma string
	public String salvar() {
		daoGeneric.salvar(usuarioPessoa);
		list.add(usuarioPessoa); //adicionar na lista apos ser salvo
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Salvo com sucesso!"));// messagem com
																									// Detail
		return "usuario-salvo";// retorna pra mesma tela
	}

	// metodo para novo objeto - forrmulario em branco
	public String novo() {
		usuarioPessoa = new UsuarioPessoa(); // instancia um novo objetos para ter tela limpa
		return "";
	}

	// metodo de remover da tabela
	public String remover() {
		try {

			//daoGeneric.deletarPoId(usuarioPessoa);
			daoGeneric.removerUsuario(usuarioPessoa);	
			list.remove(usuarioPessoa); //remove da lista
			usuarioPessoa = new UsuarioPessoa();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Removido com sucesso!"));

		} catch (Exception e) {
			if(e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, 
								 "Informação: ", "Existem telefones para este usuáreo!"));
			} else {
				e.printStackTrace(); 
			}
		}
		return "";
	}

}
