package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TelefoneUser {
	//classe que representa um relacionamento um para muitos, pois pode-se ter mais de 1 telefone
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false) //torna obrigatorio informar o atributo
	private String tipo;
	
	@Column(nullable = false)
	private String numero;
	
	//dono do  telefone, obrigatorio	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private UsuarioPessoa usuarioPessoa;

	//set e get
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}

	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}
	
	
}
