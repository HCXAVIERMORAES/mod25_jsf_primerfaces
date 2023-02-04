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

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;

import dao.DaoEmail;
import dao.DaoUsuario;
import model.EmailUser;
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
	
	private BarChartModel barChartModel = new BarChartModel();
	
	private EmailUser emailUser = new EmailUser(); //instancia-se o email 
	
	private DaoEmail<EmailUser> daoEmail = new DaoEmail<EmailUser>();//para salvar os emails
	
	//apos o managedBean ser construido na memoria esse metodo é chamado apenas uma vez
	@PostConstruct
	public void init() {
		list = daoGeneric.listar(UsuarioPessoa.class);
		
		//iniciar para o grafico
		ChartSeries userSalario = new ChartSeries(); //grupo de funcionarios
		//userSalario.setLabel("Users");
		
		for (UsuarioPessoa usuarioPessoa : list) { //adiciona salarios para o grupo
						
			userSalario.set(usuarioPessoa.getNome(), usuarioPessoa.getSalario());//adiciona salarios	
			
		}
		barChartModel.addSeries(userSalario);//adiciona ao grupo
		barChartModel.setTitle("Gráfico de salários");//titulo do grafico
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
	
	public void setEmailUser(EmailUser emailUser) {
		this.emailUser = emailUser;
	}
	
	public EmailUser getEmailUser() {
		return emailUser;
	}
	
	public void setBarChartModel(BarChartModel barChartModel) {
		this.barChartModel = barChartModel;
	}
	
	public BarChartModel getBarChartModel() {
		return barChartModel;
	}
	
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
	
	//metodo para adicionar emails
	public void addEmail() {
		emailUser.setUsuarioPessoa(usuarioPessoa); //passando o usuario da linha
		emailUser = daoEmail.updateMerge(emailUser); //salva e retorna a PK
		usuarioPessoa.getEmails().add(emailUser);//adiciona o email na lista
		emailUser = new EmailUser(); //prepara para cadastrar outor email
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, 
						 "Resultado: ", "Email salvo com sucesso!"));
	}
	
	//método para remover email
	public void removerEmail() throws Exception {
		String codigoemail = FacesContext.getCurrentInstance().getExternalContext().
				getRequestParameterMap().get("codigoemail");//pegar o codigo do email
		
		EmailUser remover = new EmailUser();		
		remover.setId(Long.parseLong(codigoemail)); //pega o id do banco
		daoEmail.deletarPoId(remover);
		usuarioPessoa.getEmails().remove(remover);	
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, 
						 "Resultado: ", "Email removido com sucesso!"));
		
	}

}
