package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;



/*anotação para nomear query sql para chamar em classe sem precisar faze-la na classe
 * */
//anotação para trabalhar com banco de dado
@Entity
@NamedQueries({
	@NamedQuery(name ="UsuarioPessoa.todos" , query = "select u from UsuarioPessoa u"),
	@NamedQuery(name ="UsuarioPessoa.buscaPorNome" , query = "select u from UsuarioPessoa u where u.nome = :nome	")
})
public class UsuarioPessoa {
	//deve-se tbm colocar um ID e uma strategiade geração depois os atributos normais
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nome;
	private String sobrenome;
	//private String email;
	private String login;
	private String senha;
	private int idade;
	private String sexo;
	
	private Double salario;
	
	//consumo de api viacep
	private  String cep;
	private  String logradouro;
	private  String complemento;
	private  String bairro;
	private  String localidade;
	private  String  uf;
	private  String  ibge;
	private  String  gia;
	private  String  ddd;
	
	//deve-se criar a lista de telefone, onde 1 usuario pode ter varios telefones
	@OneToMany(mappedBy = "usuarioPessoa",fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true )
	private List<TelefoneUser> telefoneUsers = new ArrayList<TelefoneUser>();
	
	@OneToMany(mappedBy = "usuarioPessoa",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<EmailUser> emails = new ArrayList<EmailUser>();
	
	//set e get
	
	public void setEmails(List<EmailUser> emails) {
		this.emails = emails;
	}
	
	public List<EmailUser> getEmails() {
		return emails;
	}
	
	public void setSalario(Double salario) {
		this.salario = salario;
	}
	
	public Double getSalario() {
		return salario;
	}
	
	
	public List<TelefoneUser> getTelefoneUsers() {
		return telefoneUsers;
	}
	
	public void setTelefoneUsers(List<TelefoneUser> telefoneUsers) {
		this.telefoneUsers = telefoneUsers;
	}
	
	public void setIdade(int idade) {
		this.idade = idade; 	
	}
	
	public int getIdade() {
		return idade;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSobrenome() {
		return sobrenome;
	}
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public String getSexo() {
		return sexo;
	}

	//set e gets de cep
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getIbge() {
		return ibge;
	}

	public void setIbge(String ibge) {
		this.ibge = ibge;
	}

	public String getGia() {
		return gia;
	}

	public void setGia(String gia) {
		this.gia = gia;
	}
	
	public void setDdd(String ddd) {
		this.ddd = ddd;
	}
	public String getDdd() {
		return ddd;
	}

	//to string
	@Override
	public String toString() {
		return "UsuarioPessoa [id=" + id + ", nome=" + nome + ", sobrenome=" + sobrenome + ","
				+ ", login=" + login + ", senha=" + senha + ", idade=" + idade + "]";
	}

	//para trabalhar com a tabela com o metodo init() construido
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
		
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioPessoa other = (UsuarioPessoa) obj;
		return Objects.equals(id, other.id);
	}
		

}
