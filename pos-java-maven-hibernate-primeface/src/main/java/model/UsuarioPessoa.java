package model;

import java.util.List;
import java.util.Objects;

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
	private String email;
	private String login;
	private String senha;
	private int idade;
	private String sexo;
	
	//deve-se criar a lista de telefone, onde 1 usuario pode ter varios telefones
	@OneToMany(mappedBy = "usuarioPessoa",fetch = FetchType.EAGER)
	private List<TelefoneUser> telefoneUsers;
	
	//set e get
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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

	//to string
	@Override
	public String toString() {
		return "UsuarioPessoa [id=" + id + ", nome=" + nome + ", sobrenome=" + sobrenome + ", email=" + email
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
