package managedBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;

import dao.DaoEmail;
import dao.DaoUsuario;
import datatablelazy.LazyDataTableModelUserPessoa;
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
	//private List<UsuarioPessoa> list = new ArrayList<UsuarioPessoa>(); // receber os dados salvos(antes de implementar paginação)
	//metodo para paginação
	private LazyDataTableModelUserPessoa<UsuarioPessoa> list = new LazyDataTableModelUserPessoa<UsuarioPessoa>(); 
	private DaoUsuario<UsuarioPessoa> daoGeneric = new DaoUsuario<UsuarioPessoa>(); //para deletar telefone
	
	private BarChartModel barChartModel = new BarChartModel();
	
	private EmailUser emailUser = new EmailUser(); //instancia-se o email 
	
	private DaoEmail<EmailUser> daoEmail = new DaoEmail<EmailUser>();//para salvar os emails
	
	private String campoPesquisa;//variavel para fazer a pesquisa de usuario
	
	//apos o managedBean ser construido na memoria esse metodo é chamado apenas uma vez
	@PostConstruct
	public void init() {
		//list = daoGeneric.listar(UsuarioPessoa.class);
		list.load(0, 5, null, null, null);//modificado devido a paginação
		
		montarGrafico();
	}

	private void montarGrafico() {
		barChartModel = new BarChartModel();
				
		//iniciar para o grafico
		ChartSeries userSalario = new ChartSeries(); //grupo de funcionarios
		
		for (UsuarioPessoa usuarioPessoa : list.list) { //adiciona salarios para o grupo
						
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
	
	public void setCampoPesquisa(String campoPesquisa) {
		this.campoPesquisa = campoPesquisa;
	}
	
	public String getCampoPesquisa() {
		return campoPesquisa;
	}
	
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

	/* é necessario ter o get da lista
	public List<UsuarioPessoa> getList() {
		//list = daoGeneric.listar(UsuarioPessoa.class); // inserido manualmente, para carregar a lista de dados
		return list;
	}*/
	
	public LazyDataTableModelUserPessoa<UsuarioPessoa> getList() {		
		//list = daoGeneric.listar(UsuarioPessoa.class); // inserido manualmente, para carregar a lista de dados
		montarGrafico();
		return list;
	}

//método salvar - obs: é bom sempre retornar uma string
	public String salvar() {
		daoGeneric.salvar(usuarioPessoa);
		list.list.add(usuarioPessoa); //adicionar na lista apos ser salvo
		usuarioPessoa = new UsuarioPessoa();
		init(); //recarrega o grafico apos salvar
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
			list.list.remove(usuarioPessoa); //remove da lista
			init();
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
	
	//método  de pesquisar usuarios
	public void pesquisar() {
		//list = daoGeneric.pesquisar(campoPesquisa);
		list.pesquisar(campoPesquisa);//da classe LazyData...
		montarGrafico(); //monta o grafico  comforme pesquisa
	}
	
	//método de upload
	public void upload(FileUploadEvent image) {
		String imagem = "data:image/png;base64," +
				DatatypeConverter.printBase64Binary(image.getFile().getContents());//pega a imagem
		usuarioPessoa.setImagem(imagem);
		
	}
	
	//metodo de Download
	@SuppressWarnings("static-access")
	public void download() throws IOException {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().
				getRequestParameterMap(); //para passar o fileDownloadId da pag.
		
		String fileDownloadID = params.get("fileDownloadId"); //chama o name do h:param
		
		UsuarioPessoa pessoa = daoGeneric.
				pesquisar(Long.parseLong(fileDownloadID), UsuarioPessoa.class);//consulta ao BD	
		
		byte[] imagem = new Base64().decodeBase64(pessoa.getImagem().split("\\,")[1]);//prepara a imagem
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().
				getResponse();
		response.addHeader("Content-Disposition", "attachment; filename=download.png");//adicionando o cabeçalho a resposta
		response.setContentType("application/octet-stream");//padrao
		response.setContentLength(imagem.length);//tamanho da resposta
		response.getOutputStream().write(imagem);//escrever a resposta
		response.getOutputStream().flush();//manda tudo para a resposta
		FacesContext.getCurrentInstance().responseComplete();//informa q resposta esta completa
		
	}

}
